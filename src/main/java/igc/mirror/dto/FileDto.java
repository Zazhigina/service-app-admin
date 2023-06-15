package igc.mirror.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import igc.mirror.doc.dto.DocumentDto;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;

public class FileDto {
    @JsonIgnore
    private Resource resource;
    private String fileName;
    private Long fileSize;
    private LocalDateTime fileCreateDate;

    private String fileType;

    public FileDto() {
    }

    public FileDto (DocumentDto document) {
        resource = document.getResource();
        fileName = document.getFilename();
        fileSize = document.getFileSize();
        fileCreateDate = document.getCreateDate();
        fileType = document.getFileMimeType();
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
