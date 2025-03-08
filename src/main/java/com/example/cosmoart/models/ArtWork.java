package com.example.cosmoart.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ArtWork {

    private Integer id;
    private String title;

    // El nombre que devuelve la API es "artist_display" y lo mapeamos a artistDisplay
    @JsonProperty("artist_display")
    private String artistDisplay;

    @JsonProperty("date_display")
    private String dateDisplay;

    @JsonProperty("medium_display")
    private String mediumDisplay;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_id")
    private String imageId;

    @JsonProperty("dimensions")
    private String dimensions;

    @JsonProperty("department_title")
    private String departmentTitle;

    @JsonProperty("classification_title")
    private String classificationTitle;

    @JsonProperty("artwork_type_title")
    private String artworkTypeTitle;

    @JsonProperty("api_link")
    private String apiLink;

    @JsonIgnore // No queremos que iiif_url se serialice directamente
    private String iiifUrl;

    // Generar dinámicamente la URL de la imagen
    public String getImageUrl() {
        if (iiifUrl != null && imageId != null) {
            return iiifUrl + "/" + imageId + "/full/843,/0/default.jpg";
        }
        return null;
    }

}
