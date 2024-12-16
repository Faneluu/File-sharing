package atm.proiect.filesharingbackend.repository;

import atm.proiect.filesharingbackend.entity.File;
import atm.proiect.filesharingbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("SELECT f FROM File f WHERE f.user.username = :username")
    List<File> findAllByUsername(@Param("username") String username);

    @Query("SELECT f FROM File f WHERE f.name = :filename AND f.user.username = :username")
    Optional<File> findByFilenameAndUsername(@Param("filename") String filename, @Param("username") String username);

    @Query("SELECT f FROM File f WHERE f.name LIKE %:filename% AND f.user = :user")
    Optional<atm.proiect.filesharingbackend.entity.File> findByFileNameAndUser(String filename, User user);

    Optional<atm.proiect.filesharingbackend.entity.File> findByName(String name);

    boolean existsByName(String fileName);

    Optional<File> findFileByName(String filename);

    @Query("SELECT AVG(f.fileSize) FROM File f")
    Double findAverageFileSize();
}
