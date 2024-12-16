package atm.proiect.filesharingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileSharingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileSharingBackendApplication.class, args);
	}

}
