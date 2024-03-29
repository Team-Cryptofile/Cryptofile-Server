package net.cryptofile.server.RestControllers;

import net.cryptofile.server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @PostMapping("/add")
    public String addFile(@RequestParam("file") MultipartFile file,
                          @RequestParam("title") String title,
                          @RequestParam("filetype") String filetype) throws IOException {
        return fileService.addCryptofile(file, title, filetype);
    }

    /**
     * GET request for downloading a file attached to the given UUID.
     * @param fileId UUID as a string
     * @return response containing file id and the file itself.
     */
    @GetMapping(value = "/get/{fileId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> serveFile(@PathVariable String fileId) throws IOException {
        ByteArrayResource file = new ByteArrayResource(fileService.getCryptofileBytes(fileId));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; fileId=\"" + fileId + "\"").body(file);
    }

    /**
     * GET request for getting the title of the file.
     * @param fileId UUID as a string.
     * @return title of the file.
     */
    @GetMapping(value = "/get/title/{fileId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    public String getFileTitle(@PathVariable String fileId) {
        return fileService.getCryptofileObject(fileId).getTitle();
    }

    /**
     * GET request for getting the filetype of the file.
     * @param fileId UUID as a string.
     * @return filetype of the file.
     */
    @GetMapping(value = "/get/filetype/{fileId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    public String getFiletype(@PathVariable String fileId){
        return fileService.getCryptofileObject(fileId).getFiletype();
    }
}
