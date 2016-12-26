package com.mobilunity.bestlibraries;

import io.realm.RealmObject;

public class Emoji extends RealmObject {
    private String imageUrl;

    public Emoji() {}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
