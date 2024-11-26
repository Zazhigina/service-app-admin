package igc.mirror.poi.model;

import igc.mirror.poi.view.CellType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CellNumber implements Cell {
    private CellType cellType = CellType.NUMBER;
    private Integer num;
    private Double val;
    private Integer styleID;
    private String formattedValue;

    public CellNumber(int num, Double val) {
        this.num = num;
        this.val = val;
    }

    public CellNumber(int num, Double val, String formattedValue) {
        this.num = num;
        this.val = val;
        this.formattedValue = formattedValue;
    }

    @Override
    public CellType getCellType() {
        return this.cellType;
    }

    @Override
    public int getNum() {
        return this.num;
    }

    @Override
    public void printValue() {
        log.debug("NUMBER CELL[{}] = {}", getNum(), getVal());
    }
}
