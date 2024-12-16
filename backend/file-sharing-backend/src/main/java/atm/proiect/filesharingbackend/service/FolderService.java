package atm.proiect.filesharingbackend.service;

import atm.proiect.filesharingbackend.dto.FileMetadataDTO;
import atm.proiect.filesharingbackend.dto.FolderDTO;
import atm.proiect.filesharingbackend.entity.File;
import atm.proiect.filesharingbackend.entity.Folder;
import atm.proiect.filesharingbackend.entity.User;
import atm.proiect.filesharingbackend.repository.FolderRepository;
import atm.proiect.filesharingbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public FolderService(FolderRepository folderRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    public List<FolderDTO> getFoldersByUsername(String username) {
        List<Folder> folders = folderRepository.findByUserUsername(username);

        return folders.stream()
                .map(folder -> {
                    FolderDTO folderDTO = convertToDTO(folder);
                    folderDTO.setPathFolder(buildFolderPath(folder));
                    return folderDTO;
                })
                .collect(Collectors.toList());
    }


    public FolderDTO createFolder(FolderDTO folderDTO) {
        User user = userRepository.findByUsername(folderDTO.getOwnerUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + folderDTO.getOwnerUsername()));

        Folder parentFolder = getFolderParentName(folderDTO.getParentPath(), user);

        boolean folderExists = folderRepository.existsByNameAndUserAndParentId(
                folderDTO.getName(), user, parentFolder);

        if (folderExists) {
            throw new RuntimeException("A folder with the name '" + folderDTO.getName() +
                    "' already exists under the specified parent.");
        }

        Folder folder = new Folder();
        folder.setName(folderDTO.getName());
        folder.setUser(user);
        folder.setParentId(parentFolder);

        Folder savedFolder = folderRepository.save(folder);

        return convertToDTO(savedFolder);
    }


    public void deleteFolder(FolderDTO folderDTO, String username) {
        Folder folder = getFolderName(folderDTO.getCurrentPath(), username);

        deleteChildFolders(folder);

        folderRepository.delete(folder);
    }

    private void deleteChildFolders(Folder parentFolder) {
        List<Folder> childFolders = folderRepository.findByParentId(parentFolder);
        for (Folder child : childFolders) {
            deleteChildFolders(child);
            folderRepository.delete(child);
        }
    }

    public FolderDTO updateFolder(String username, FolderDTO folderDTO) {
        Folder folder = getFolderName(folderDTO.getCurrentPath(), username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (folderDTO.getName() != null) {
            folder.setName(folderDTO.getName());
        }

        Folder newParentFolder = getFolderParentName(folderDTO.getParentPath(), user);

        if (folder.equals(newParentFolder)) {
            throw new RuntimeException("A folder cannot be its own parent.");
        }

        folder.setParentId(newParentFolder);

        Folder updatedFolder = folderRepository.save(folder);

        return convertToDTO(updatedFolder);
    }


    public List<FileMetadataDTO> getFilesInFolder(FolderDTO folderDTO, String username) {
        Folder folder = getFolderName(folderDTO.getCurrentPath(), username);

        if (!folder.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied to folder: " + folderDTO.getCurrentPath());
        }

        return folder.getFiles().stream()
                .map(this::convertToFileDTO)
                .collect(Collectors.toList());
    }

    public Long getFolderId(FolderDTO folderDTO, String username) {
        Folder folder = getFolderName(folderDTO.getCurrentPath(), username);

        return folder.getId();
    }

    private FolderDTO convertToDTO(Folder folder) {
        String parentPath = buildFolderPath(folder);

        return new FolderDTO(
                folder.getId(),
                folder.getName(),
                folder.getUser().getUsername(),
                parentPath
        );
    }

    private FileMetadataDTO convertToFileDTO(File file) {
        return new FileMetadataDTO(
                file.getId(),
                file.getName(),
                file.getFileType(),
                file.getFileSize(),
                file.getCreatedAt() != null ? String.format(String.valueOf(FORMATTER)) : null,
                file.getUpdatedAt() != null ? String.format(String.valueOf(FORMATTER)) : null,
                file.getUser().getUsername()
        );
    }

    private String buildFolderPath(Folder folder) {
        StringBuilder pathBuilder = new StringBuilder();
        folder = folder.getParentId();

        while (folder != null) {
            pathBuilder.insert(0, "/" + folder.getName());
            folder = folder.getParentId();
        }
        return pathBuilder.toString();
    }

    private Folder getFolderName(String path, String username) {
        
        String[] pathParts = path.split("/");
        Folder parentFolder = null;
        Folder currentFolder = null;
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        for (String part : pathParts) {
            if (part.isEmpty()) continue;
            currentFolder = folderRepository.findByNameAndUserAndParentId(part, user, parentFolder)
                    .orElseThrow(() -> new RuntimeException("Folder not found for path segment: " + part));
            parentFolder = currentFolder;
        }
        return currentFolder;
    }

    private Folder getFolderParentName(String parentPath, User user) {
        if (parentPath == null || parentPath.isEmpty()) {
            return null;
        }

        String[] pathParts = parentPath.split("/");
        Folder parentFolder = null;

        for (String part : pathParts) {
            if (part.isEmpty()) continue;
            parentFolder = folderRepository.findByNameAndUserAndParentId(part, user, parentFolder)
                    .orElseThrow(() -> new RuntimeException("Parent folder not found for path: " + parentPath));
        }
        return parentFolder;
    }



}
