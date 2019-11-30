package net.cryptofile.server;

import net.cryptofile.server.Objects.Cryptofile;
import net.cryptofile.server.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServerApplicationTests {

	@Autowired
	FileService fileService;

	private String fileDestination = System.getProperty("user.home") +"/cryptofiles/";

	@LocalServerPort
	private int port;

	/*
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

	 */


	@Test
	void testRestRequestAddFile() {
		HttpURLConnection c = null;
		File testFile = new File("C:\\Users\\frode\\Projects\\Cryptofile-Server\\src\\test\\java\\net\\cryptofile\\server\\testFile.txt");
		String title = "Testfile";
		try {
			URL url = new URL("http://localhost:" + port + "/add");
			String boundary = UUID.randomUUID().toString();
			c = (HttpURLConnection) url.openConnection();

			c.setDoOutput(true);
			c.setRequestMethod("POST");
			c.setRequestProperty("Content-Type", "multipart/form-data;charset=UTF-8;boundary=----WebKitFormBoundary" + boundary);

			DataOutputStream request = new DataOutputStream(c.getOutputStream());

			// Title
			System.out.println("Sending title: " + title);
			request.writeBytes("------WebKitFormBoundary" + boundary + "\r\n");
			request.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n");
			request.writeBytes("Content-Type: text/plain\r\n\r\n");
			request.writeBytes(title + "\r\n");
			System.out.println("Sent title: " + title);
			// File
			System.out.println("Sending file");
			request.writeBytes("------WebKitFormBoundary" + boundary + "\r\n");
			request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"binary\"\r\n");
			request.writeBytes("Content-Type: application/octet-stream\r\n\r\n");

			FileInputStream fileInputStream = new FileInputStream(testFile);

			int availableBytes = fileInputStream.available();
			int maxBufferSize = 4 * 1024;
			int bufferSize = Math.min(availableBytes, maxBufferSize);
			byte[] buffer = new byte[bufferSize];
			int readBytes = fileInputStream.read(buffer, 0, bufferSize);

			while (readBytes > 0){
				request.write(buffer, 0, bufferSize);
				availableBytes = fileInputStream.available();
				bufferSize = Math.min(availableBytes, maxBufferSize);
				readBytes = fileInputStream.read(buffer, 0, bufferSize);
			}


			//request.write(testFile);
			request.writeBytes( "\r\n");
			System.out.println("Sent file");

			request.writeBytes("------WebKitFormBoundary" + boundary + "--\r\n");
			request.flush();

			if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
				String response = bufferedReader.readLine();
				System.out.println("Success response: " + response);



				Cryptofile fileObject = fileService.getCryptofileObject(response);
				System.out.println("Title: " + fileObject.getTitle() +
						"\nID: " + fileObject.getId() +
						"\nTime added: " + fileObject.getTimeAdded() +
						"\nTime deletes: " + fileObject.getTimeDeletes());

				String testFileString = new String(Files.readAllBytes(Paths.get(testFile.getPath())), StandardCharsets.UTF_8);
				String uploadedFileString = new String(Files.readAllBytes(Paths.get(fileDestination + response)), StandardCharsets.UTF_8);

				System.out.println("Test file: " + testFileString);
				System.out.println("Uploaded file: " + uploadedFileString);

				boolean match = response.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
				Assert.isTrue(match, "did not return UUID string");
				Assert.isTrue(testFileString.contentEquals(uploadedFileString), "uploaded content is different");



				fileService.deleteFile(response);

				c.getInputStream().close();
			}
			System.out.println(new IOException("Error uploading file: " + c.getResponseMessage()));
		} catch (Exception e) {
			System.err.println("Failed to call "+ e);
			System.out.println(new IOException("Error creating item", e));
		} finally {
			if(c != null) c.disconnect();
		}


	}
}
