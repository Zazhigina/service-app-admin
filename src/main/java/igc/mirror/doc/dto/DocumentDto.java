package igc.mirror.doc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;

import java.time.LocalDateTime;

public class DocumentDto {
    private Long id;
    private String filename;
    private String fileMimeType;
    private Long fileSize;
    @JsonIgnore
    private Resource resource;
    @JsonIgnore
    private ContentDisposition contentDisposition;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;

    public DocumentDto() {
    }

    public DocumentDto(Long id, String filename, String fileMimeType, Long fileSize) {
        this.id = id;
        this.filename = filename;
        this.fileMimeType = fileMimeType;
        this.fileSize = fileSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileMimeType() {
        return fileMimeType;
    }

    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
}
