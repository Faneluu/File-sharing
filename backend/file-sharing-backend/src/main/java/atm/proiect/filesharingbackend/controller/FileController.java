package atm.proiect.filesharingbackend.controller;

import atm.proiect.filesharingbackend.dto.FileMetadataDTO;
import atm.proiect.filesharingbackend.service.FileService;
import atm.proiect.filesharingbackend.util.JwtUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @GetMapping("/")
    public ResponseEntity<List<FileMetadataDTO>> getFilesMetadataByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<FileMetadataDTO> fileMetadata = fileService.getFilesMetadataByUsername(username);

        return ResponseEntity.ok(fileMetadata);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFileByName(@PathVariable String filename) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return fileService.downloadFileByUsername(filename, username);
    }

    @GetMapping("/{folderId}/{filename}")
    public ResponseEntity<Resource> downloadFileFromFolder(@PathVariable Long folderId,
                                                           @PathVariable String filename) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return fileService.downloadFileFromFolder(folderId, filename, username);
    }


    @PostMapping("/{filename}")
    public ResponseEntity<String> uploadFile(@PathVariable String filename,
                                             @RequestParam("file") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            fileService.uploadFileUsername(username, filename, file.getContentType(), file.getInputStream());
            return ResponseEntity.ok("File uploaded successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }

    @PostMapping("/{folderId}/{filename}")
    public ResponseEntity<String> uploadFileToFolder(@PathVariable Long folderId,
                                                     @PathVariable String filename,
                                                     @RequestParam("file") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            fileService.uploadFileToFolder(username, folderId, filename, file.getContentType(), file.getInputStream());
            return ResponseEntity.ok("File uploaded successfully to folder: " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }


    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        fileService.deleteFileUsername(username, filename);
        return ResponseEntity.ok("File deleted successfully: " + filename);
    }

    @DeleteMapping("/{folderId}/{filename}")
    public ResponseEntity<String> deleteFileFromFolder(@PathVariable Long folderId,
                                                       @PathVariable String filename) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        fileService.deleteFileFromFolder(username, folderId, filename);
        return ResponseEntity.ok("File deleted successfully from folder: " + filename);
    }

    
    @PutMapping("/{filename}")
    public ResponseEntity<String> updateFile(@PathVariable String filename,
                                             @RequestParam("file") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            fileService.updateFileUsername(username, filename, (FileInputStream) file.getInputStream());
            return ResponseEntity.ok("File updated successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating file: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{folderId}/{filename}")
    public ResponseEntity<String> updateFileInFolder(@PathVariable Long folderId,
                                                     @PathVariable String filename,
                                                     @RequestParam("file") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            fileService.updateFileInFolder(username, folderId, filename, file.getInputStream());
            return ResponseEntity.ok("File updated successfully in folder: " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating file: " + e.getMessage());
        }
    }
}
