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

    public CellNumber(Integer num, Double val) {
        this.num = num;
        this.val = val;
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
