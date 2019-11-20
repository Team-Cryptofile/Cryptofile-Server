package net.cryptofile.server;

import net.cryptofile.server.Objects.Cryptofile;
import net.cryptofile.server.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;

@SpringBootTest
class ServerApplicationTests {

	@Autowired
	FileService fileService;


	@Test
	void testRestAddFile() throws Exception{
		File testFile = new File("C:\\Users\\frode\\Projects\\Cryptofile-Server\\src\\test\\java\\net\\cryptofile\\server\\testFile.txt");
		byte[] fileBytes = Files.readAllBytes(testFile.toPath());

		String response = fileService.addCryptofile(fileBytes, "Test title 1");
		System.out.println("Response: " + response);

		boolean match = response.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
		Assert.isTrue(match, "did not return UUID string");

		Cryptofile fileObject = fileService.getCryptofileObject(response);
		System.out.println("Title: " + fileObject.getTitle() +
				"\nID: " + fileObject.getId() +
				"\nTime added: " + fileObject.getTimeAdded() +
				"\nTime deletes: " + fileObject.getTimeDeletes());
		fileService.deleteFile(response);
	}
}
