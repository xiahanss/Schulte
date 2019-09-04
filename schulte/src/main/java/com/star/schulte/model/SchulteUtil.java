package com.star.schulte.model;

import android.util.Log;

import java.util.Random;

/**
 * 说明：
 * 时间：2019/9/4 10:21
 */
public class SchulteUtil {

    public static SchulteCell[][] createCell(int row, int column) {
        SchulteCell[] cells = new SchulteCell[row * column];
        for (int i=0; i<cells.length; i++) {
            SchulteCell cell = new SchulteCell();
            cell.setValue(i+1);
            cell.setStatus(SchulteCellStatus.Normal);
            cells[i] = cell;
        }
        Random random = new Random();
        for (int i=cells.length - 1; i>0; i--) {
            int index = random.nextInt(i);
            SchulteCell temp = cells[i];
            cells[i] = cells[index];
            cells[index] = temp;
        }
        SchulteCell[][] newCells = new SchulteCell[row][column];
        for (int i=0; i<row; i++) {
            if (column >= 0) System.arraycopy(cells, i * column, newCells[i], 0, column);
        }
        return newCells;
    }

}
