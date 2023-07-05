package igc.mirror.template.dto;

import igc.mirror.doc.dto.DocumentDto;

import java.time.LocalDateTime;

public class FileInfoDto {

    /**
     * Идентификатор файла в сервисе документации
     */
    private Long fileId;

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

    public FileInfoDto() {
    }

    public FileInfoDto(DocumentDto document) {
        fileId = document.getId();
        fileName = document.getFilename();
        fileSize = document.getFileSize();
        fileCreateDate = document.getCreateDate();
        fileType = document.getFileMimeType();
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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
