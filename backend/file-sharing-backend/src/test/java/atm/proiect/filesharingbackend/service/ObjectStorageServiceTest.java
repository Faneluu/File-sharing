package atm.proiect.filesharingbackend.service;

import atm.proiect.filesharingbackend.entity.Authority;
import atm.proiect.filesharingbackend.entity.User;
import atm.proiect.filesharingbackend.util.CryptoUtil;
import io.minio.*;
import io.minio.errors.MinioException;
import org.aspectj.weaver.patterns.HasMemberTypePattern;
import org.junit.*;
import org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ObjectStorageServiceTest {

    private ObjectStorageService objectStorageService;

    @Before
    public void setUp() throws Exception {
        MinioClient minioClientMock = mock(MinioClient.class);
        objectStorageService = new ObjectStorageService(minioClientMock);
    }

    @Test
    public void uploadFileUsers_ShouldUploadSuccessfully() throws MinioException, IOException, NoSuchAlgorithmException {
        byte[] fileContent = "test file content".getBytes();

        File tempFile = File.createTempFile("testFile", ".txt");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileContent);
        }

        FileInputStream fileStream = new FileInputStream(tempFile);

        Authority authority = new Authority();
        authority.setAuthority("USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        User user = new User("username",
                "asdasd",
                true,
                "razvan@test.ro",
                CryptoUtil.generateUserSecretKey(),
                100000.0f,
                0.0f
        );
        user.setAuthorities(authorities);

        atm.proiect.filesharingbackend.entity.File file = new atm.proiect.filesharingbackend.entity.File(
                "test-file.txt",
                234234.3f,
                "text/plain",
                user
        );

        try {
            objectStorageService.uploadFileUsers(user, file, fileStream);
        } catch (Exception e) {
            Assert.fail("File upload failed: " + e.getMessage());
        } finally {
            tempFile.delete();
        }
    }



}