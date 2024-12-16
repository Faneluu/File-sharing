package atm.proiect.filesharingbackend.controller;

import atm.proiect.filesharingbackend.dto.FileSharingLinkDTO;
import atm.proiect.filesharingbackend.service.FileService;
import atm.proiect.filesharingbackend.service.FileSharingLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/links")
public class FileSharingLinkController {

    FileSharingLinkService fileSharingLinkService;
    FileService fileService;

    @Autowired
    FileSharingLinkController(FileSharingLinkService fileSharingLinkService, FileService fileService) {
        this.fileSharingLinkService = fileSharingLinkService;
        this.fileService = fileService;
    }


    @GetMapping("{linkId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String linkId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String filename = null;
        System.out.println("Cerere facuta de " + username + " pentru descarcare prin link");

        try {
            filename = fileSharingLinkService.getFilenameBySharingLinkId(linkId);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return fileService.downloadFileByUsername(filename, username);
    }

    @PostMapping
    public ResponseEntity<String> createFileSharingLink(@RequestBody FileSharingLinkDTO fileSharingLink) {
        String linkId = null;
        try {
            linkId = fileSharingLinkService.createFileSharingLink(
                    fileSharingLink.getFilename(), fileSharingLink.getAccessType(),
                    fileSharingLink.getPermissions(), fileSharingLink.getExpiresAt());

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(linkId);
    }
}


