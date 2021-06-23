package com.bsa.boot.controller;

import com.bsa.boot.dto.History;
import com.bsa.boot.dto.Query;
import com.bsa.boot.entity.Cache;
import com.bsa.boot.processor.FS;
import com.bsa.boot.processor.Parser;
import com.bsa.boot.service.Giphy;
import com.bsa.boot.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author professorik
 * @created 22/06/2021 - 09:09
 * @project boot
 */
@RestController
@RequestMapping("/user/")
public class ApiUser {

    private Giphy giphyService;
    private FS fileSystemProcessor;
    private Cache cache;
    private User userUtil;
    private Parser parser;
    private Logger logger = LoggerFactory.getLogger(ApiUser.class);

    @Autowired
    public ApiUser(Giphy giphyServ, FS fileProcessor, Cache cache, User userUtil, Parser parser) {
        this.giphyService = giphyServ;
        this.fileSystemProcessor = fileProcessor;
        this.cache = cache;
        this.userUtil = userUtil;
        this.parser = parser;
    }

    @PostMapping("{id}")
    public ResponseEntity<?> generateGif(@RequestParam @PathVariable String id,
                                         @NotBlank @RequestBody Query query,
                                         @RequestHeader(name = "X-BSA-GIPHY") boolean present) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("POST request by USER: " + id + ", CLASS: UserController, METHOD: generateGif(...); QUERY: " + query.getQuery());
        Map<String, String> response = new HashMap<>();
        File gifFile = fileSystemProcessor.getGifPath(query.getQuery());
        if (gifFile != null) {
            File result = fileSystemProcessor.copyToUserFolder(id, query.getQuery(), gifFile.getPath());
            response.put("gif", result.getAbsolutePath());
            response.put("query", query.getQuery());
            cache.updateCache(id, query.getQuery(), result.getName());
        } else {
            var gifEntity = giphyService.searchGif(id, query);
            fileSystemProcessor.addGifToUserFolder(id, gifEntity);
            gifFile = fileSystemProcessor.getGifPath(query.getQuery());
            response.put("gif", gifFile.getAbsolutePath());
            response.put("query", query.getQuery());
            cache.updateCache(id, query.getQuery(), gifEntity.getId());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> searchGif(@PathVariable String id, @NotBlank String query) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("GET request, USER: " + id + ", CLASS: UserController, METHOD: searchGif, QUERY: " + query);
        var response = new HashMap<String, String>();
        if (userUtil.validate(id) || query.isBlank() || query.isEmpty()) {
            response.put("message", "Invalid request ");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (cache.getGif(id, query) != null) {
            response.put("gif", fileSystemProcessor.getPATH() + cache.getGif(id, query) + ".gif");
            response.put("query", query);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            if (fileSystemProcessor.getFromUserFolder(id, query) != null) {
                System.out.println("Taken from file system");
                File gif = fileSystemProcessor.getFromUserFolder(id, query);
                response.put("gif", gif.toPath().toString());
                response.put("query", query);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Cant find this file!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping("{id}/all")
    public ResponseEntity<?> getAllGifs(@PathVariable String id) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("GET request, USER: " + id + ", CLASS: UserController, METHOD: getAllGifs(...)");
        var result = parser.parseFullCache(fileSystemProcessor.getFullUserCache(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}/history")
    public ResponseEntity<?> getHistory(@PathVariable String id) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("GET request, USER: " + id + ", CLASS: UserController, METHOD: getHistory(...)");
        History[] result = parser.parseHistory(fileSystemProcessor.getHistory(id));
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}/history/clean")
    public ResponseEntity<?> cleanHistory(@PathVariable String id) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("DELETE request, USER: " + id + ", CLASS: UserController, METHOD: clearHistory(...)");
        if (fileSystemProcessor.deleteHistory(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("{id}/search")
    public ResponseEntity<?> searchGif(@PathVariable String id, String query, boolean force) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("GET request, USER: " + id + ", CLASS: UserController, METHOD: searchGif(...), QUERY: " + query + ", FORCE: " + force);
        if (!force) {
            var gif = cache.getGif(id, query);
            if (gif != null) {
                return new ResponseEntity<>(gif, HttpStatus.OK);
            }
        }
        var gif = fileSystemProcessor.getGifPath(query);
        if (gif == null) {
            return ResponseEntity.notFound().build();
        }
        cache.updateCache(id, query, gif.getAbsolutePath());
        return new ResponseEntity<>(gif.getAbsolutePath(), HttpStatus.OK);
    }

    @PostMapping("{id}/generate")
    public ResponseEntity<?> createGif(@PathVariable String id, @RequestBody Query query) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("POST request, USER: " + id + ", CLASS: UserController, METHOD: createGif(...), QUERY: " + query.getQuery() + ", FORCE: " + query.getForce());
        File gifFile;
        if (!query.getForce()) {
            gifFile = fileSystemProcessor.getGifPath(query.getQuery());
            if (gifFile != null) {
                return new ResponseEntity<>(gifFile.getAbsolutePath(), HttpStatus.OK);
            }
        }
        var gifEntity = giphyService.searchGif(null, query);
        var resultFile = fileSystemProcessor.copyToUserFolder(id, query.getQuery(), fileSystemProcessor.downloadGifByUrl(gifEntity).getPath());
        if (resultFile == null) {
            return ResponseEntity.notFound().build();
        }
        gifFile = fileSystemProcessor.getGifPath(query.getQuery());
        return new ResponseEntity<>(gifFile, HttpStatus.OK);
    }

    @DeleteMapping("{id}/reset")
    public ResponseEntity<?> clearMemoryCache(@PathVariable String id, String query) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("DELETE request, USER: " + id + ", CLASS: UserController, METHOD: clearMemoryCache(...), QUERY:" + query);
        if (query == null) {
            cache.resetUser(id);
        } else {
            cache.resetUser(id, query);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/clean")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (validator(id) != null) {
            return validator(id);
        }
        logger.info("DELETE request, USER: " + id + ", CLASS: UserController, METHOD: deleteUser(...)");
        cache.resetUser(id);
        fileSystemProcessor.clearUserCache(id);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> validator(String id) {
        if (userUtil.validate(id)) {
            Map<String, String> tempResponse = new HashMap<>();
            tempResponse.put("message", "Invalid request");
            return new ResponseEntity<>(tempResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
