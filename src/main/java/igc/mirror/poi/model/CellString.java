package igc.mirror.poi.model;

import igc.mirror.poi.view.CellType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CellString implements Cell {
    private CellType cellType = CellType.STRING;
    private Integer num;
    private String val;
    private Integer styleID;

    public CellString(int currentColumnNumber, String textValue) {
        this.num = currentColumnNumber;
        this.val = textValue;
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
        log.debug("TEXT CELL[{}] = {}", getNum(), getVal());
    }
}
