package com.apdallahy3.marvelcharcters.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ChracterItem implements Serializable {
    private String name, thumbnailUrl,description,details,wiki,comicLink;
    private int id;





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getComicLink() {
        return comicLink;
    }

    public void setComicLink(String comicLink) {
        this.comicLink = comicLink;
    }
}
