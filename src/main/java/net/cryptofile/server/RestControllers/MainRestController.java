package net.cryptofile.server.RestControllers;

import net.cryptofile.server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is the rest controller for this server.
 */
@RestController
public class MainRestController {

    @Autowired
    private FileService fileService;

    /**
     * POST request for uploading a file.
     * @param file file as a byte array.
     * @param title title of the file.
     * @return file id as a UUID string.
     */
    @PostMapping(value = "/add")
    public String addFile(@RequestParam("file") byte[] file,
                          @RequestParam("title") String title) {
        return fileService.addCryptoFile(file, title);
    }

    /**
     * GET request for downloading a file attached to the given UUID.
     * @param fileId UUID as a string
     * @return response containing file id and the file itself.
     */
    @GetMapping(value = "/get/{fileId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> serveFile(@PathVariable String fileId) {
        ByteArrayResource file = new ByteArrayResource(fileService.getCryptoFile(fileId));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; fileId=\"" + fileId + "\"").body(file);
    }
}
