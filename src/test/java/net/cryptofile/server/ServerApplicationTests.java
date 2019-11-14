package net.cryptofile.server;

import net.cryptofile.server.Repositories.MainRepository;
import net.cryptofile.server.RestControllers.MainRestController;
import net.cryptofile.server.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;

@SpringBootTest
class ServerApplicationTests {

	@Autowired
	FileService fileService;

	@Test
	void contextLoads() {
	}

	@Test
	void testRestAddFile() throws Exception{
		File testFile = new File("C:\\Users\\frode\\Projects\\Cryptofile-Server\\src\\test\\java\\net\\cryptofile\\server\\testFile.txt");
		byte[] fileBytes = Files.readAllBytes(testFile.toPath());

		fileService.addCryptoFile(fileBytes, "Test title 1");
		//MainRestController.addFile(fileBytes, "Test title 1");
		//System.out.println("Response: " + response);
	}
}
