package atm.proiect.filesharingbackend.repository;

import atm.proiect.filesharingbackend.entity.FileBackup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface FileBackupRepository extends JpaRepository<FileBackup, Integer> {
    List<FileBackup> findAllByFileUserUsername(String username);
}
