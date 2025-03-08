package com.example.cosmoart.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArtworkWithImageUrl {

    private ArtWork artwork;
    private String imageUrl;

}
