package atm.proiect.filesharingbackend.service;

import atm.proiect.filesharingbackend.entity.File;
import atm.proiect.filesharingbackend.entity.FileSharingLink;
import atm.proiect.filesharingbackend.enums.AccessType;
import atm.proiect.filesharingbackend.enums.Permissions;
import atm.proiect.filesharingbackend.repository.FileRepository;
import atm.proiect.filesharingbackend.repository.FileSharingLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileSharingLinkService {

    private final FileService fileService;
    FileSharingLinkRepository fileSharingLinkRepository;
    FileRepository fileRepository;

    @Autowired
    public FileSharingLinkService(FileSharingLinkRepository fileSharingLinkRepository,
                                  FileRepository fileRepository, FileService fileService) {
        this.fileSharingLinkRepository = fileSharingLinkRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    /**
     *
     * @param filename Numele fisierului pentru care se creeaza link-ul.
     * @param accessType Daca link-ul este public sau privat.
     * @param permissions Daca fisierul poate fi editat sau nu.
     * @param expiresAt Data de expirare a link-ului
     * @return id-ul unic al intrarii in tabelul cu link-uri
     * @throws RuntimeException in caz ca fisierul nu exista sau timpul este setat gresit
     */
    public String createFileSharingLink(String filename, AccessType accessType,
                                        Permissions permissions, LocalDateTime expiresAt)
            throws RuntimeException {

        Optional<File> file = fileRepository.findByName(filename);

        if (file.isEmpty()) {
            throw new RuntimeException("Fisierul nu exista");
        }

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Timpul este setat gresit");
        }

        FileSharingLink fileSharingLink = new FileSharingLink(
                UUID.randomUUID().toString(),
                file.orElse(null),
                accessType,
                expiresAt,
                permissions,
                file.get().getUser()
        );

        fileSharingLinkRepository.save(fileSharingLink);

        return fileSharingLink.getLinkId();
    }

    public String getFilenameBySharingLinkId(String sharingLinkId) {
        FileSharingLink fileSharingLink = fileSharingLinkRepository.findByLinkId(sharingLinkId).orElse(null);
        if (fileSharingLink == null) {
            throw new RuntimeException("Fisierul nu exista");
        }
        return fileSharingLink.getFile().getName();
    }

    /**
     * Sterge intrarea din baza de date pentru link (dar nu si fisierul).
     * @param linkId
     */
    public void deleteFileSharingLink(String linkId) {
        if (this.fileSharingLinkRepository.existsByLinkId(linkId)) {
            this.fileSharingLinkRepository.deleteByLinkId(linkId);
        }
    }
}
