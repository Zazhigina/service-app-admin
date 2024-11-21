package igc.mirror.poi.model;

import igc.mirror.poi.view.CellType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@Slf4j
public class CellDate implements Cell {
    final transient String date_iso8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private CellType cellType = CellType.DATE;
    private Integer num;
    private Date val;
    private Integer styleID;


    public CellDate(int currentCol, Date dateValue) {
        this.num = currentCol;
        this.val = dateValue;
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
        log.debug("DATE CELL[{}] = {}", getNum(), getVal());
    }
}
