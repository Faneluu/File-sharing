package atm.proiect.filesharingbackend.controller;

import atm.proiect.filesharingbackend.dto.FileMetadataDTO;
import atm.proiect.filesharingbackend.dto.FolderDTO;
import atm.proiect.filesharingbackend.service.FolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<FolderDTO>> getFoldersByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<FolderDTO> folders = folderService.getFoldersByUsername(username);
        return ResponseEntity.ok(folders);
    }

    @PostMapping("/")
    public ResponseEntity<FolderDTO> createFolder(@RequestBody FolderDTO folderDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        folderDTO.setOwnerUsername(username);

        FolderDTO createdFolder = folderService.createFolder(folderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFolder);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteFolder(@RequestBody FolderDTO folderDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String name = folderDTO.getName();
        folderService.deleteFolder(folderDTO, username);
        return ResponseEntity.ok("Folder deleted successfully: " + name);
    }

    @PutMapping("/")
    public ResponseEntity<FolderDTO> updateFolder(@RequestBody FolderDTO folderDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        FolderDTO updatedFolder = folderService.updateFolder(username, folderDTO);
        return ResponseEntity.ok(updatedFolder);
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileMetadataDTO>> getFilesInFolder(@RequestBody FolderDTO folderDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FileMetadataDTO> files = folderService.getFilesInFolder(folderDTO, username);

        return ResponseEntity.ok(files);
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getFolderId(@RequestBody FolderDTO folderDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long folderID = folderService.getFolderId(folderDTO, username);

        return ResponseEntity.ok(folderID);
    }
}

