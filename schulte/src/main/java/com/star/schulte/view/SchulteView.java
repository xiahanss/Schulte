package com.star.schulte.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.star.schulte.model.SchulteCell;
import com.star.schulte.model.SchulteConfig;
import com.star.schulte.model.SchulteGame;
import com.star.schulte.model.SchulteListener;
import com.star.schulte.model.SchulteUtil;

/**
 * 说明：舒尔特游戏界面
 * 时间：2019/9/4 9:18
 */
public class SchulteView extends View {

    private SchulteConfig config;
    private SchulteGame game;
    private SchulteListener listener;

    private Paint borderPaint;
    private Paint cellPaint;
    private Paint cellFontPaint;

    private float defaultLineSize;
    private float cellSize;
    private float borderSize;

    private float offsetX;
    private float offsetY;
    private float width;
    private float height;

    private int currentIndex;
    private long startCountDownTime;

    private int totalTap;
    private int correctTap;

    private int downIndex = -1;

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
        defaultLineSize = getResources().getDisplayMetrics().density * 10 + 0.5F;
        borderPaint = new Paint();
        cellFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellFontPaint.setTextAlign(Paint.Align.CENTER);
        cellPaint = new Paint();
        config = new SchulteConfig();
        game = new SchulteGame();
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

    /**
     * 设置配置
     */
    public void setConfig(SchulteConfig config) {
        this.config = config;
        update();
    }

    /**
     * 设置监听器
     */
    public void setListener(SchulteListener listener) {
        this.listener = listener;
    }

    public void start() {
        currentIndex = 0;
        totalTap = 0;
        correctTap = 0;
        startCountDownTime = System.currentTimeMillis();
        game.setCells(null);
        update();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis() - startCountDownTime;
                if (time >= config.getCountDownTime()) {
                    game.setCells(SchulteUtil.createCell(game.getRow(), game.getColumn()));
                    update();
                    if (listener != null) {
                        listener.onStart();
                    }
                } else {
                    if (listener != null) {
                        listener.onCountDown(time);
                    }
                    postDelayed(this, 32);
                }
            }
        }, 32);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                totalTap++;
                SchulteCell cell = game.getCells()[row][column];
                downIndex = cell.getValue();
                if (cell.getValue() == currentIndex + 1) {  //点击正确
                    correctTap++;
                    currentIndex++;
                    if (listener != null) {
                        listener.onProgress(currentIndex);
                    }
                    if (currentIndex == game.getRow() * game.getColumn()) {
                        if (listener != null) {
                            listener.onFinish(totalTap, correctTap);
                        }
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
    protected void onDraw(Canvas canvas) {
        int row = game.getRow();
        int column = game.getColumn();
        SchulteCell[][] cells = game.getCells();
        if (cells != null) {
            canvas.drawRect(offsetX, offsetY, offsetX + width, offsetY + height, borderPaint);
            for(int i=0; i<row; i++) {
                for (int j=0; j<column; j++) {
                    SchulteCell cell = cells[i][j];
                    float x = offsetX + borderSize * (j + 1) + cellSize * j;
                    float y = offsetY + borderSize * (i + 1) + cellSize * i;
                    if (cell.getValue() == downIndex) {
                        cellPaint.setColor(config.getCellPressColor());
                    } else {
                        cellPaint.setColor(config.getCellColor());
                    }
                    canvas.drawRect(x, y, x + cellSize, y + cellSize, cellPaint);
                    Paint.FontMetrics fontMetrics = cellFontPaint.getFontMetrics();
                    float fontOffset = (fontMetrics.top + fontMetrics.bottom) / 2; //基准线
                    int baseLineY = (int) (y +  cellSize / 2 - fontOffset);
                    canvas.drawText(cell.getValue() + "", x + cellSize / 2, baseLineY, cellFontPaint);
                }
            }
        }
    }
}