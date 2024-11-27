package igc.mirror.poi.model;

import igc.mirror.poi.view.FileType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class Workbook {
    private ArrayList<Sheet> sheets = new ArrayList<>();
    private FileType type = null;
    private Map<Integer, CellStyle> styles = new HashMap<>();

    public void addSheet(Sheet sheet) {
        sheets.add(sheet);
    }

    public void recalculateSheetSize() {
        this.sheets.forEach(
                Sheet::recalculateSize
        );
    }

    public void printForDebug() {
        log.debug("PRINT CONTENT OF WORKBOOK");
        this.sheets.forEach(
            s -> {
                log.debug("SHEET[{}] = {}", s.getNum(), s.getName());
                s.getRows().forEach(
                        r -> {
                            log.debug(".ROW[{}]", r.getNum());
                            r.getCells().forEach(
                                    c -> {
                                        switch (c.getCellType()) {
                                            case NUMBER -> {
                                                CellNumber cd = (CellNumber)c;
                                                log.debug("..CELL[{}] = {}", cd.getNum(), cd.getVal());
                                            }
                                            case DATE -> {
                                                CellDate ct = (CellDate)c;
                                                log.debug("..CELL[{}] = {}", ct.getNum(), ct.getVal());
                                            }
                                            case STRING -> {
                                                CellString cs = (CellString)c;
                                                log.debug("..CELL[{}] = {}", cs.getNum(), cs.getVal());
                                            }
                                        }
                                    }
                            );
                        }
                );
            }
        );

    }
}
