package igc.mirror.poi.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Row implements Comparable<Row> {
    private Double height;
    private Integer num;
    private ArrayList<Cell> cells = new ArrayList<>();

    public Row(Double height, Integer num) {
        this.height = height;
        this.num = num;
    }

    public void addCell(Cell cell){
        cells.add(cell);
    }
    @Override
    public int compareTo(Row o) {
        return this.num.compareTo(o.getNum());
    }
}
