package com.example.cosmoart.services;

import com.example.cosmoart.dto.AicApiResponse;
import com.example.cosmoart.models.ArtWork;
import com.example.cosmoart.models.ArtworkWithImageUrl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtApiService {

    private final WebClient webClient;

    public ArtApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.artic.edu/api/v1").build();
    }

    public Mono<List<ArtWork>> getArtworks(int page, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/artworks")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .queryParam("fields", "id,title,image_id,artist_display,date_display,medium_display,description,dimensions,department_title,classification_title,artwork_type_title,api_link")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AicApiResponse<ArtWork>>() {})
                .map(response -> {
                    // Obtener la URL base IIIF desde config
                    String iiifUrlBase = response.getConfig().getIiif_url();

                    // Asignar iiif_url a cada obra
                    response.getData().forEach(artwork -> artwork.setIiifUrl(iiifUrlBase));

                    return response.getData();
                });
    }



}
