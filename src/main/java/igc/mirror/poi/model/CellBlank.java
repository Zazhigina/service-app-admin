package igc.mirror.poi.model;

import igc.mirror.poi.view.CellType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CellBlank implements Cell {
    private CellType cellType = CellType.BLANK;
    private Integer num;
    private Integer styleID;

    public CellBlank(Integer currentColumnNumber) {
        this.num = currentColumnNumber;
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
        log.debug("BLANK CELL[{}]", getNum());
    }
}
