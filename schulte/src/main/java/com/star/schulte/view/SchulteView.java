package com.star.schulte.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.star.schulte.bean.SchulteCell;
import com.star.schulte.bean.SchulteConfig;
import com.star.schulte.bean.SchulteGame;
import com.star.schulte.bean.SchulteStatus;
import com.star.schulte.listener.SchulteListener;
import com.star.schulte.util.CellAnimation;

/**
 * 说明：舒尔特游戏界面
 * 时间：2019/9/4 9:18
 */
public class SchulteView extends View {

    private SchulteGame game;

    //Paint
    private Paint borderPaint;
    private Paint cellPaint;
    private Paint cellFontPaint;

    //Prop
    private float defaultLineSize;
    private float cellSize;
    private float borderSize;

    //offset
    private float offsetX;
    private float offsetY;

    private float width;
    private float height;
    private RectF rect;

    private long startCountDownTime;

    private int downIndex = -1;

    private boolean blind;

    //动画
    private CellAnimation globalAnimation;

    public SchulteView(Context context) {
        this(context, null);
    }

    public SchulteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SchulteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        globalAnimation = new CellAnimation(600);
        rect = new RectF();
        defaultLineSize = getResources().getDisplayMetrics().density * 40 + 0.5F;
        borderPaint = new Paint();
        cellFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellFontPaint.setTextAlign(Paint.Align.CENTER);
        cellPaint = new Paint();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                update();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 设置游戏
     */
    public void setGame(SchulteGame game) {
        this.game = game;
        update();
    }

    public SchulteGame getGame() {
        return game;
    }

    public void setBlind(boolean blind) {
        this.blind = blind;
        invalidate();
    }

    /**
     * 开始游戏
     * 进入倒计时
     */
    public void start() {
        if (game == null) {
            return;
        }
        blind = false;
        startCountDownTime = SystemClock.elapsedRealtime();
        game.startCountDown();

        SchulteConfig config = game.getConfig();
        if (config.isAnimation()) {
            globalAnimation.start();
        }
        update();
        if (game.isBlind()) {
            startGame(true);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (game == null) {
                        return;
                    }
                    if (game.getStatus() == SchulteStatus.CountDown) {
                        long time = SystemClock.elapsedRealtime() - startCountDownTime;
                        long remainTime = game.getRow() * game.getColumn() * 1000 - time;
                        if (remainTime <= 0) {
                           callBlindStart();
                        } else {
                            if (game.getListener() != null) {
                                game.getListener().onCountDown(remainTime);
                            }
                            postDelayed(this, 32);
                        }
                    }
                }
            }, 32);
        }

    }

    /**
     * 倒计时结束
     * 游戏进行中
     */
    private void startGame(boolean blind) {
        game.start(blind);
        update();
    }

    private void callBlindStart() {
        game.setStatus(SchulteStatus.Gaming);
        blind = true;
        invalidate();
        SchulteListener listener = game.getListener();
        if (listener != null) {
            listener.onStart();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getDeviceId() == 0) {
            return false;
        }
        if (game == null) {
            return true;
        }
        SchulteStatus status = game.getStatus();
        //倒计时状态点击直接开始
        if (status == SchulteStatus.CountDown) {
            if (!game.isBlind()) {
                startGame(false);
                return true;
            } else {
               callBlindStart();
            }
        }
        //游戏完成状态
        if (status == SchulteStatus.Finished) {
            downIndex = -1;
            invalidate();
            return false;
        }
        SchulteCell[][] cells = game.getCells();
        if (cells == null) {
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        float halfLineSize = borderSize / 2;
        float oneSize = cellSize + borderSize;
        //触点方块
        int column = (int)((x - offsetX - halfLineSize) / oneSize);
        int row = (int)((y - offsetY - halfLineSize) / oneSize);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (row >= 0 && row < game.getRow() && column >= 0 && column < game.getColumn()) {
                game.setTapTotal(game.getTapTotal() + 1);
                SchulteCell cell = game.getCells()[row][column];
                downIndex = cell.getValue();
                int currentIndex = game.getIndex();
                SchulteListener listener = game.getListener();
                if (downIndex == currentIndex + 1) {  //点击正确
                    game.setTapCorrect(game.getTapCorrect() + 1);
                    currentIndex++;
                    game.setIndex(currentIndex);
                    if (listener != null) {
                        listener.onProgress(currentIndex + 1, game.getRow() * game.getColumn());
                    }
                    if (currentIndex == game.getRow() * game.getColumn()) {
                        blind = false;
                        if (listener != null) {
                            game.setStatus(SchulteStatus.Finished);
                            listener.onFinish(game.getTapTotal(), game.getTapCorrect());
                        }
                    }
                } else {
                    game.setTapError(game.getTapError() + 1);
                    if (listener != null) {
                        listener.onTapError(downIndex, currentIndex + 1);
                    }
                }
            } else {
                downIndex = -1;
            }
        } else if (action == MotionEvent.ACTION_MOVE) {

        } else {
            downIndex = -1;
        }
        invalidate();
        return true;
    }

    /**
     * 更新视图
     */
    public void update() {
        if (game == null) {
            return;
        }
        SchulteConfig config = game.getConfig();
        borderPaint.setColor(config.getBorderColor());
        cellFontPaint.setColor(config.getFontColor());
        cellPaint.setColor(config.getCellColor());
        borderSize = defaultLineSize * config.getBorderSize();
        float viewWidth = getWidth();
        float viewHeight = getHeight();
        int row = game.getRow();
        int column = game.getColumn();
        float horizontalSize = viewWidth / column;
        float verticalSize = viewHeight / row;
        boolean horizontal = horizontalSize < verticalSize;
        if (horizontal) {   //铺满横向
            cellSize = (viewWidth - (column + 1) * borderSize) / column;
            offsetX = 0;
            offsetY = (viewHeight - cellSize * row - borderSize * (row + 1)) / 2;
            width = viewWidth;
            height = viewHeight - offsetY * 2;
        } else {    //铺满纵向
            cellSize = (viewHeight - (row + 1) * borderSize) / row;
            offsetX = (viewWidth - cellSize * column - borderSize * (column + 1)) / 2;
            offsetY = 0;
            width = viewWidth - offsetX * 2;
            height = viewHeight;
        }
        cellFontPaint.setTextSize(cellSize * config.getFontSize());
        requestLayout();
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        update();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (game == null) {
            return;
        }
        SchulteConfig config = game.getConfig();
        int row = game.getRow();
        int column = game.getColumn();
        SchulteCell[][] cells = game.getCells();
        float radius = cellSize * config.getCorner();
        canvas.drawRect(offsetX, offsetY, offsetX + width, offsetY + height, borderPaint);
        if (cells == null || blind) {
            float progress = 1;
            if (!blind && config.isAnimation()) {
                progress = globalAnimation.progress();
            }
            float eachProgress = 1F / (row + column);
            cellPaint.setColor(config.getCellColor());
            for (int i=0; i<row; i++) {
                for (int j=0; j<column; j++) {
                    float x = offsetX + borderSize * (j + 1) + cellSize * j;
                    float y = offsetY + borderSize * (i + 1) + cellSize * i;
                    int index = i + j;
                    float start = index * eachProgress / 2;
                    float cellProgress = (progress - start) / eachProgress / 4;
                    if (cellProgress < 0) {
                        cellProgress = 0;
                    }
                    if (cellProgress > 1) {
                        cellProgress = 1;
                    }
                    float offset = cellSize / 2 * (1 - cellProgress);
                    rect.set(x + offset, y + offset,
                            x + cellSize - offset,
                            y + cellSize - offset);
                    float r = radius * cellProgress;
                    if (cells != null && cells[i][j].getValue() == downIndex) {
                        cellPaint.setColor(config.getCellPressColor());
                    } else {
                        cellPaint.setColor(config.getCellColor());
                    }
                    canvas.drawRoundRect(rect, r, r, cellPaint);
                }
            }
            if (config.isAnimation()) {
                globalAnimation.invalidate(this);
            }
        } else {
            for(int i=0; i<row; i++) {
                for (int j=0; j<column; j++) {
                    SchulteCell cell = cells[i][j];
                    float x = offsetX + borderSize * (j + 1) + cellSize * j;
                    float y = offsetY + borderSize * (i + 1) + cellSize * i;
                    rect.set(x, y, x + cellSize, y + cellSize);
                    if (cell != null) {
                        if (cell.getValue() == downIndex) {
                            cellPaint.setColor(config.getCellPressColor());
                        } else {
                            cellPaint.setColor(config.getCellColor());
                        }
                        canvas.drawRoundRect(rect, radius, radius, cellPaint);
                        Paint.FontMetrics fontMetrics = cellFontPaint.getFontMetrics();
                        float fontOffset = (fontMetrics.top + fontMetrics.bottom) / 2; //基准线
                        int baseLineY = (int) (y +  cellSize / 2 - fontOffset);
                        canvas.drawText(cell.getValue() + "", x + cellSize / 2, baseLineY, cellFontPaint);
                    }
                }
            }
        }

    }
}
