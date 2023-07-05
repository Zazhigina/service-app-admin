package igc.mirror.template.dto;

import igc.mirror.doc.dto.DocumentDto;

import java.time.LocalDateTime;

public class FileDto {
    /**
     * Содержимое файла в Base64
     */
    private String content;

    /**
     * Имя файла
     */
    private String fileName;

    /**
     * Размер файла
     */
    private Long fileSize;

    /**
     * Дата создания файла
     */
    private LocalDateTime fileCreateDate;

    /**
     * Тип файла
     */
    private String fileType;

    public FileDto() {
    }

    public FileDto (DocumentDto document) {
        fileName = document.getFilename();
        fileSize = document.getFileSize();
        fileCreateDate = document.getCreateDate();
        fileType = document.getFileMimeType();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
