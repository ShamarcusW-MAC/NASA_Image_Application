
package com.example.nasagery_application.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("secondary_creator")
    @Expose
    private String secondaryCreator;
    @SerializedName("center")
    @Expose
    private String center;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("nasa_id")
    @Expose
    private String nasaId;
    @SerializedName("keywords")
    @Expose
    private List<String> keywords = null;
    @SerializedName("description_508")
    @Expose
    private String description508;
    @SerializedName("album")
    @Expose
    private List<String> album = null;
    @SerializedName("location")
    @Expose
    private String location;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSecondaryCreator() {
        return secondaryCreator;
    }

    public void setSecondaryCreator(String secondaryCreator) {
        this.secondaryCreator = secondaryCreator;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNasaId() {
        return nasaId;
    }

    public void setNasaId(String nasaId) {
        this.nasaId = nasaId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getDescription508() {
        return description508;
    }

    public void setDescription508(String description508) {
        this.description508 = description508;
    }

    public List<String> getAlbum() {
        return album;
    }

    public void setAlbum(List<String> album) {
        this.album = album;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
