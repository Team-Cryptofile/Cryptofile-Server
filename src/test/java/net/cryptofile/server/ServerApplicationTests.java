package net.cryptofile.server;

import net.cryptofile.server.Repositories.MainRepository;
import net.cryptofile.server.RestControllers.MainRestController;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
		"security.basic.enabled=false"
})
class ServerApplicationTests {

	@Autowired
	MainRepository mainRepository;

	@Value("${local.server.port}")
	private int port;

	@Test
	void contextLoads() {
	}

	@Test
	void addFileWithoutTitleStatusCode() throws IOException {
		// Build
		String requestAddress = "http://localhost:" + port + "/add";
		File testFile = new File("C:\\Users\\frode\\Projects\\Cryptofile-Server\\src\\test\\java\\net\\cryptofile\\server\\testfile.txt");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpEntity postData = MultipartEntityBuilder.create().addBinaryBody("file", testFile).build();
		HttpUriRequest request = RequestBuilder.post().setUri(requestAddress).setEntity(postData).build();

		// Send
		System.out.println("Sending request: " + request.getRequestLine() + "\nMessage: " + request);
		HttpResponse response = httpClient.execute(request);

		// Response
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		if (response.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode() + "\nMessage: " + response);
		}

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}
		System.out.println("Response: \n" + result);
	}

	@Test
	void addFileWithTitleStatusCode() throws IOException {
		// Build
		String requestAddress = "http://localhost:" + port + "/add";
		File testFile = new File("C:\\Users\\frode\\Projects\\Cryptofile-Server\\src\\test\\java\\net\\cryptofile\\server\\testfile.txt");
		String testTitle = "FileTitle";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpEntity postData = MultipartEntityBuilder.create().addBinaryBody("file", testFile).build();
		HttpEntity fileTitle = new StringEntity(testTitle);
		HttpUriRequest request = RequestBuilder.post().setUri(requestAddress).setEntity(postData).setEntity(fileTitle).build();

		// Send
		System.out.println("Sending request: " + request.getRequestLine());
		HttpResponse response = httpClient.execute(request);

		// Response
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		if (response.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}
		System.out.println("Response: \n" + result);
	}

	@Test
	void testRestAddFile() throws Exception {
		File testFile = new File("C:\\Users\\frode\\Projects\\Cryptofile-Server\\src\\test\\java\\net\\cryptofile\\server\\testfile.txt");
		byte[] fileAsBytes = Files.readAllBytes(testFile.toPath());
		mainRepository.addCryptofile(fileAsBytes, "FileTitle 1");
	}

}
