package com.star.schulte.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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
        globalAnimation = new CellAnimation(300);
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
        start(null);
    }

    /**
     * 直接根据地图开始游戏，不需要点击开始
     */
    public void start(SchulteCell[][] cells) {
        if (game == null) {
            return;
        }
        blind = false;
        game.ready();
        SchulteConfig config = game.getConfig();
        if (config.isAnimation()) {
            globalAnimation.start();
        }
        update();
        if (cells != null) {   //有地图或盲玩直接开始游戏
            if (game.getListener() != null) {
                game.getListener().onReady();
            }
            startGame(cells);
        }
    }

    /**
     * 倒计时结束
     * 游戏进行中
     */
    private void startGame(SchulteCell[][] cells) {
        game.start(cells);
        update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getDeviceId() == 0) {
            return false;
        }
        if (game == null) {
            return true;
        }
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            SchulteStatus status = game.getStatus();
            //准备状态点击直接开始
            if (status == SchulteStatus.Ready) {
                startGame(null);
                return true;
            }
            //隐藏方块
            if (status == SchulteStatus.Gaming) {
                if (game.isBlind()) {
                    blind = true;
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
            int actionIndex = event.getActionIndex();
            float x = event.getX(actionIndex);
            float y = event.getY(actionIndex);
            float halfLineSize = borderSize / 2;
            float oneSize = cellSize + borderSize;
            //触点方块
            int column = (int)((x - offsetX - halfLineSize) / oneSize);
            int row = (int)((y - offsetY - halfLineSize) / oneSize);
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
        if (game == null || game.getConfig() == null) {
            return;
        }
        SchulteConfig config = game.getConfig();
        borderPaint.setColor(config.getBorderColor());
        cellFontPaint.setColor(config.getFontColor());
        cellPaint.setColor(config.getCellColor());
        borderSize = defaultLineSize * config.getBorderSize();
        if (config.getBorderSize() > 0 && borderSize < 1) {
            borderSize = 1;
        }
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
        try {
            update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (game == null || game.getConfig() == null) {
            return;
        }
        try {
            drawGame(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawGame(Canvas canvas) {
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
