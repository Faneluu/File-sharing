package atm.proiect.filesharingbackend.service;

import atm.proiect.filesharingbackend.entity.Authority;
import atm.proiect.filesharingbackend.entity.User;
import atm.proiect.filesharingbackend.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        System.out.println("Initializing admin user...");

        // Fetch admin credentials from environment variables
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String adminUsername = dotenv.get("ADMIN_USERNAME");
        String adminPassword = dotenv.get("ADMIN_PASSWORD");
        String adminEmail = dotenv.get("ADMIN_EMAIL");

        if (adminUsername == null || adminPassword == null || adminEmail == null) {
            throw new IllegalStateException("Admin credentials not found in .env file");
        }

        // Check if admin user exists
        if (userRepository.findById(adminUsername).isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setEmail(adminEmail);
            adminUser.setEnabled(true);
            adminUser.setUserSecret(UUID.randomUUID().toString()); // Example for required field
            adminUser.setTotalSpace(1024); // Example value for required field
            adminUser.setUsedSpace(0); // Example value for required field
            adminUser.setUserBucketId(UUID.randomUUID().toString()); // Example for required field
            Authority adminAuthority = new Authority();
            adminAuthority.setAuthority("ADMIN");

            adminAuthority.setUser(adminUser);
            adminUser.getAuthorities().add(adminAuthority);

            // Save the admin user
            userRepository.save(adminUser);
            System.out.println("Admin user created successfully.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}

