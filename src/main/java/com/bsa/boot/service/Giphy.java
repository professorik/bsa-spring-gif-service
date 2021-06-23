package com.bsa.boot.service;

import com.bsa.boot.dto.GifFile;
import com.bsa.boot.dto.Query;
import com.bsa.boot.entity.GIF;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author professorik
 * @created 22/06/2021 - 09:22
 * @project boot
 */
@Service
public class Giphy {

    private Environment environment;

    @Autowired
    public Giphy(Environment environment){
        this.environment = environment;
    }

    public GIF searchGif(String id, Query query){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("giphy.search.url"))
                .queryParam("api_key", environment.getProperty("giphy.api.key"))
                .queryParam("tag", query.getQuery())
                .queryParam("random_id", id);

        GifFile gifFile = restTemplate.getForObject(builder.toUriString(), GifFile.class);

        JSONObject json = new JSONObject(gifFile);
        json = json.getJSONObject("data");

        GIF gifEntity = new GIF();
        gifEntity.setId(json.getString("id"));

        StringBuilder url = new StringBuilder(json.getJSONObject("images").getJSONObject("downsized").getString("url"));
        url.replace(8, 14, "i");

        gifEntity.setUrl(url.toString());
        gifEntity.setQuery(query.getQuery());

        return gifEntity;
    }
}
