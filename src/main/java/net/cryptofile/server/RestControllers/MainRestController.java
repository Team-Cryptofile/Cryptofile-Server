package net.cryptofile.server.RestControllers;

import net.cryptofile.server.Repositories.MainRepository;
import net.cryptofile.server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class MainRestController {

    private final MainRepository mainRepository;
    private FileService fileService;

    @Autowired
    public MainRestController(MainRepository mainRepository){
        this.mainRepository = mainRepository;
    }

    @PostMapping(value = "/add")
    public String addFile(@RequestParam("file") byte[] file,
                          @RequestParam("title") String title) {
        //return mainRepository.addCryptofile(file, title);
        return fileService.addCryptoFile(file, title);
    }

    @GetMapping(value = "/get/{fileId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> serveFile(@PathVariable String fileId) {
        ByteArrayResource file = new ByteArrayResource(mainRepository.getCryptofile(fileId));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; fileId=\"" + fileId + "\"").body(file);
    }
}
