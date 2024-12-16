package atm.proiect.filesharingbackend.service;

import atm.proiect.filesharingbackend.dto.FileMetadataDTO;
import atm.proiect.filesharingbackend.entity.File;
import atm.proiect.filesharingbackend.entity.Folder;
import atm.proiect.filesharingbackend.entity.User;
import atm.proiect.filesharingbackend.repository.FileRepository;
import atm.proiect.filesharingbackend.repository.FolderRepository;
import atm.proiect.filesharingbackend.repository.UserRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final ObjectStorageService objectStorageService;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public FileService(FileRepository fileRepository, ObjectStorageService objectStorageService, UserRepository userRepository, FolderRepository folderRepository) {
        this.fileRepository = fileRepository;
        this.objectStorageService = objectStorageService;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
    }

    public List<FileMetadataDTO> getFilesMetadataByUsername(String username) {
        return fileRepository.findAllByUsername(username)
                .stream()
                .map(file -> new FileMetadataDTO(file.getId(), file.getName(),file.getFileType(),  file.getFileSize(),file.getCreatedAt().toString(), file.getUpdatedAt().toString(), file.getUser().getUsername()))
                .toList();
    }


    private ResponseEntity<Resource> downloadFile(String username, String filename, Folder folder) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        File file;
        if (folder == null) {
            file = fileRepository.findFileByName(filename)
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));
        } else {
            if (!folder.getUser().equals(user)) {
                throw new RuntimeException("Access denied: Folder does not belong to the user.");
            }

            file = folder.getFiles().stream()
                    .filter(f -> f.getName().equals(filename))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("File not found in the specified folder: " + filename));
        }

        try {
            java.io.File tempFile = java.io.File.createTempFile("downloaded_", ".tmp");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

            objectStorageService.downloadFileUsers(user, file, fileOutputStream);

            InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("Error occurred during file download: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<Resource> downloadFileByUsername(String filename, String username) {
        return downloadFile(username, filename, null);
    }

    public ResponseEntity<Resource> downloadFileFromFolder(Long folderId, String filename, String username) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found: " + folderId));

        return downloadFile(username, filename, folder);
    }


    private void uploadFile(String username, String fileName, String fileType, InputStream fileStream, Folder folder) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        boolean fileExists = (folder == null)
                ? fileRepository.existsByName(fileName)
                : folder.getFiles().stream().anyMatch(file -> file.getName().equals(fileName));

        if (fileExists) {
            throw new RuntimeException("A file with the name '" + fileName + "' already exists" +
                    (folder != null ? " in the folder." : "."));
        }

        File file = new File();
        file.setName(fileName);
        file.setFileType(fileType);
        file.setFileKey(fileName + UUID.randomUUID());
        file.setFileSize(objectStorageService.uploadFileUsers(user, file, fileStream));
        file.setUser(user);
        file.setCreatedAt(LocalDateTime.now());

        if (folder != null) {
            file.setFolders(List.of(folder));
        }

        fileRepository.save(file);

        if (folder != null) {
            folder.getFiles().add(file);
            folderRepository.save(folder);
        }
    }

    public void uploadFileUsername(String username, String fileName, String fileType, InputStream fileStream) {
        uploadFile(username, fileName, fileType, fileStream, null);
    }

    public void uploadFileToFolder(String username, Long folderId, String fileName, String fileType, InputStream fileStream) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found: " + folderId));

        if (!folder.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied: Folder does not belong to the user.");
        }

        uploadFile(username, fileName, fileType, fileStream, folder);
    }

    private void updateFile(String username, String filename, InputStream fileStream, Folder folder) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        File file;
        if (folder == null) {
            file = fileRepository.findByFileNameAndUser(filename, user)
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));
        } else {
            if (!folder.getUser().equals(user)) {
                throw new RuntimeException("Access denied: Folder does not belong to the user.");
            }

            file = folder.getFiles().stream()
                    .filter(f -> f.getName().equals(filename))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("File not found in the specified folder: " + filename));
        }

        objectStorageService.updateFileUsers(user, file, fileStream);

        file.setUpdatedAt(LocalDateTime.now());
        fileRepository.save(file);

        System.out.println("Successfully updated file: " + filename);
    }

    public void updateFileUsername(String username, String filename, InputStream fileStream) {
        updateFile(username, filename, fileStream, null);
    }

    public void updateFileInFolder(String username, Long folderId, String filename, InputStream fileStream) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found: " + folderId));

        updateFile(username, filename, fileStream, folder);
    }

    private void deleteFile(String username, String filename, Folder folder) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        File file;
        if (folder == null) {
            file = fileRepository.findByFileNameAndUser(filename, user)
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename + " for user: " + username));
        } else {
            if (!folder.getUser().equals(user)) {
                throw new RuntimeException("Access denied: Folder does not belong to the user.");
            }

            file = folder.getFiles().stream()
                    .filter(f -> f.getName().equals(filename))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("File not found in the specified folder: " + filename));
        }

        objectStorageService.deleteFileUsers(user, file);

        if (folder != null) {
            folder.getFiles().remove(file);
            folderRepository.save(folder);
        }

        fileRepository.delete(file);

        System.out.println("Successfully deleted file: " + filename);
    }

    public void deleteFileUsername(String username, String filename) {
        deleteFile(username, filename, null);
    }

    public void deleteFileFromFolder(String username, Long folderId, String filename) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found: " + folderId));

        deleteFile(username, filename, folder);
    }

}
