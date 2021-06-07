package com.pocket.notes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotesModel {

    @SerializedName("Title")
    @Expose
    public String title;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("Id")
    @Expose
    public int id;
    @SerializedName("Image")
    @Expose
    public String image;
    @SerializedName("Date")
    @Expose
    public String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
