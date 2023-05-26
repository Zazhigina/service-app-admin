package igc.mirror.doc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;

public class DocumentDto {
    private Long id;
    private String filename;
    private String fileMimeType;
    @JsonIgnore
    private Resource resource;
    @JsonIgnore
    private ContentDisposition contentDisposition;

    public DocumentDto() {
    }

    public DocumentDto(Long id, String filename, String fileMimeType) {
        this.id = id;
        this.filename = filename;
        this.fileMimeType = fileMimeType;
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
}
