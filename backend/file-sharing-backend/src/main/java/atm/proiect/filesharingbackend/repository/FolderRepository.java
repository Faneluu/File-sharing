package atm.proiect.filesharingbackend.repository;

import atm.proiect.filesharingbackend.entity.Folder;
import atm.proiect.filesharingbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    /**
     * Finds all folders owned by a specific user.
     *
     * @param username the username of the folder's owner
     * @return a list of folders owned by the specified user
     */
    List<Folder> findByUserUsername(String username);

    /**
     * Finds a folder by its name and owner's username.
     *
     * @param name the name of the folder
     * @param username the username of the folder's owner
     * @return an optional containing the folder if found, or empty otherwise
     */
    Optional<Folder> findByNameAndUserUsername(String name, String username);

    /**
     * Finds all folders that are children of a specific parent folder.
     *
     * @param parentId the ID of the parent folder
     * @return a list of child folders
     */
    List<Folder> findByParentId(Folder parentId);

    /**
     * Checks if a folder with the given name, owner, and parent folder exists.
     *
     * @param name the name of the folder
     * @param user the owner of the folder
     * @param parentId the parent folder (can be null for root folders)
     * @return true if such a folder exists, false otherwise
     */
    boolean existsByNameAndUserAndParentId(String name, User user, Folder parentId);

    Optional<Folder> findByNameAndUserAndParentId(String name, User user, Folder parentFolder);
}
