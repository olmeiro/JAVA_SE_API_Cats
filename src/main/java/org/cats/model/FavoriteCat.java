package org.cats.model;

import io.github.cdimascio.dotenv.Dotenv;

import java.awt.*;

public class FavoriteCat {

    Dotenv dotenv = Dotenv.load();

    String id;
    String imageId;
    String apikey = dotenv.get("API_KEY");
    public ImageX image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public ImageX getImage() {
        return image;
    }

    public void setImage(ImageX image) {
        this.image = image;
    }
}
