package igc.mirror.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.util.Map;

public class TemplateDto {
    private String title;
    @JsonIgnore
    private Resource resource;
    private String fileName;
    private Long fileSize;
    private LocalDateTime fileCreateDate;
    private Map<String, String> variables;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getFileCreateDate() {
        return fileCreateDate;
    }

    public void setFileCreateDate(LocalDateTime fileCreateDate) {
        this.fileCreateDate = fileCreateDate;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }
}
