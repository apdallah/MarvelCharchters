package com.apdallahy3.marvelcharcters.Models;

public class Item {
    String name,thumbinalUrl,thumbinalPath,type;
    int id,character_id;

    public int getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(int character_id) {
        this.character_id = character_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbinalUrl() {
        return thumbinalUrl;
    }

    public void setThumbinalUrl(String thumbinalUrl) {
        this.thumbinalUrl = thumbinalUrl;
    }

    public String getThumbinalPath() {
        return thumbinalPath;
    }

    public void setThumbinalPath(String thumbinalPath) {
        this.thumbinalPath = thumbinalPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
