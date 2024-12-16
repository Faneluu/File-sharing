package atm.proiect.filesharingbackend.dto;

public class FileMetadataDTO {

    private long fileId;
    private String name;
    private String fileType;
    private double fileSize;
    private String createdAt;
    private String updatedAt;
    private String username;



    public FileMetadataDTO(long fileId, String name, String fileType, double fileSize, String createdAt, String updatedAt, String username) {
        this.fileId = fileId;
        this.name = name;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
    }


    public long getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Metodă de reprezentare a obiectului sub formă de String
    @Override
    public String toString() {
        return "FileFileMetadataDTO{" +
                "fileId=" + fileId +
                ", name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

