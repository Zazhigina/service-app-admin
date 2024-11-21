package igc.mirror.poi.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
public class Sheet {
    String name;
    int num;
    int lastRowNum;
    int lastCellNum;
    private ArrayList<Row> rows = new ArrayList<>();
    private Map<Integer, Integer> cellsWidth  = new HashMap<>();
    private int freezeCell;
    private int freezeRow;
    private String autoFilter;

    public void addRow(Row row) {
        rows.add(row);
    }

    public void recalculateSize() {
        Collections.sort(rows);
        this.lastRowNum = getMaxRowNum();
        this.lastCellNum = getMaxColNum();
    }

    public int getMaxColNum() {
        int maxColNum = 0;
        for(Row row : this.rows) {
            for(Cell cell: row.getCells()) {
                maxColNum = Math.max(cell.getNum(), maxColNum);
            }
        }
        return maxColNum;
    }

    public int getMinColNum(int maxColNum) {
        int minColNum = maxColNum;
        for(Row row:rows) {
            for(Cell cell: row.getCells()) {
                minColNum = Math.min(cell.getNum(), minColNum);
            }
        }
        return minColNum;
    }

    public int getMaxRowNum() {
        int maxRowNum = 0;
        for(Row row:rows) {
            maxRowNum = Math.max(row.getNum(), maxRowNum);
        }
        return maxRowNum;
    }
}
