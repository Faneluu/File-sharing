package atm.proiect.filesharingbackend.util;

import atm.proiect.filesharingbackend.entity.Authority;
import atm.proiect.filesharingbackend.entity.User;
import atm.proiect.filesharingbackend.service.ObjectStorageService;
import io.minio.MinioClient;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class CryptoUtilTest {

    private ObjectStorageService objectStorageService;

    @Before
    public void setUp() throws Exception {
        MinioClient minioClientMock = mock(MinioClient.class);
        objectStorageService = new ObjectStorageService(minioClientMock);
    }

    @Test
    public void testEncryptAndDecryptFile() throws Exception {
        // Create a temporary file with original content
        File inputFile = File.createTempFile("testInput", ".txt");
        FileOutputStream inputFileStream = new FileOutputStream(inputFile);
        String originalContent = "This is a test file for encryption and decryption.";
        inputFileStream.write(originalContent.getBytes());
        inputFileStream.close();

        // Create authorities for the user
        Authority authority = new Authority();
        authority.setAuthority("USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        User user = new User("username",
                "password",
                true,
                "razvan@test.ro",
                CryptoUtil.generateUserSecretKey(),
                100000.0f,
                0.0f
        );
        user.setAuthorities(authorities);

        // Create temporary files for encryption and decryption
        File encryptedFile = File.createTempFile("testEncrypted", ".enc");
        File decryptedFile = File.createTempFile("testDecrypted", ".txt");

        // Encrypt the input file
        CryptoUtil.encryptFile(new FileInputStream(inputFile), new FileOutputStream(encryptedFile), user);

        // Decrypt the encrypted file
        CryptoUtil.decryptFile(new FileInputStream(encryptedFile), new FileOutputStream(decryptedFile), user);

        // Verify that the content after decryption matches the original content
        String decryptedContent = Files.readString(decryptedFile.toPath());
        assertEquals("Decrypted content should match the original content", originalContent, decryptedContent);

        // Clean up the temporary files
        inputFile.delete();
        encryptedFile.delete();
        decryptedFile.delete();
    }

}