package com.example.cosmoart.controllers;

import com.example.cosmoart.dto.AicApiResponse;
import com.example.cosmoart.models.ArtWork;
import com.example.cosmoart.models.ArtworkWithImageUrl;
import com.example.cosmoart.services.ArtApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/art")
public class ArtController {

    private final ArtApiService artApiService;

    public ArtController(ArtApiService artApiService) {
        this.artApiService = artApiService;
    }

    @GetMapping("/data")
    public Mono<ResponseEntity<List<ArtWork>>> getArtworks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit) {
        return artApiService.getArtworks(page, limit)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
