package com.bsa.boot.controller;

import com.bsa.boot.dto.CacheDTO;
import com.bsa.boot.dto.Query;
import com.bsa.boot.entity.GIF;
import com.bsa.boot.processor.FS;
import com.bsa.boot.processor.Parser;
import com.bsa.boot.service.Giphy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    private Giphy giphyService;
    private FS fileSystemProcessor;
    private Parser parser;
    private Logger logger = LoggerFactory.getLogger(ApiUser.class);

    @Autowired
    public MainController(Giphy giphyServ, FS fileProcessor, Parser parser) {
        this.giphyService = giphyServ;
        this.fileSystemProcessor = fileProcessor;
        this.parser = parser;
    }

    @GetMapping("/gifs")
    public ResponseEntity<?> getAllGifs() {
        logger.info("GET request, CLASS: GeneralController, METHOD: getAllGifs(...)");
        String[] arrayOfAllGifs = parser.parseOnlyFiles(fileSystemProcessor.getFullCache(null));
        return new ResponseEntity<>(arrayOfAllGifs, HttpStatus.OK);
    }

    @GetMapping("/cache")
    public ResponseEntity<?> getDiskCache(String query) {
        logger.info("GET request, CLASS: GeneralController, METHOD: getDiskCache(...), QUERY: " + query);
        CacheDTO[] fullCache = parser.parseFullCache(fileSystemProcessor.getFullCache(query));
        return new ResponseEntity<>(fullCache, HttpStatus.OK);
    }

    @DeleteMapping("/cache")
    public ResponseEntity<?> clearDiskCache() {
        logger.info("DELETE request, CLASS: GeneralController, METHOD: clearDickCache(...)");
        return fileSystemProcessor.clearCache()? ResponseEntity.ok().build(): ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/cache/generate")
    public ResponseEntity<?> generateGif(@RequestBody Query query) {
        logger.info("POST request, CLASS: GeneralController, METHOD: generateGif(...), QUERY:" + query.getQuery() + ", FORCE: " + query.getForce());
        GIF giphy = giphyService.searchGif(null, query);
        fileSystemProcessor.downloadGifByUrl(giphy);
        CacheDTO cache = parser.parseFullCache(fileSystemProcessor.getFullCache(query.getQuery()))[0];
        return new ResponseEntity<>(cache, HttpStatus.OK);
    }
}
