package igc.mirror.poi.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FileType {
    XLS("xls", "application/vnd.ms-excel", "xls"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    PDF("pdf", "application/pdf", "pdf");

    private final String name;
    private final String mediaType;
    private final String ext;

    @JsonCreator
    public static FileType lookup(String name) {
        return Arrays.stream(FileType.values())
                .filter(s -> s.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
