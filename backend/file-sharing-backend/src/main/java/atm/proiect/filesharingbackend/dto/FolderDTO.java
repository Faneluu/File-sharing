package atm.proiect.filesharingbackend.dto;

public class FolderDTO {
    private Long id;
    private String name;
    private String ownerUsername;
    private String parentPath;
    private String currentPath;

    public FolderDTO() {}

    public FolderDTO(Long id, String name, String ownerUsername, String parentPath) {
        this.id = id;
        this.name = name;
        this.ownerUsername = ownerUsername;
        this.parentPath = parentPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getParentPath() { return parentPath; }

    public void setPathFolder(String s) { this.parentPath = s; }

    public String getCurrentPath() { return currentPath; }

    public void setCurrentPath(String currentPath) { this.currentPath = currentPath; }
}
