
package com.example.nasagery_application.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("links")
    @Expose
    private List<Link> links = null;
    @SerializedName("href")
    @Expose
    private String href;

    @SerializedName("expand")
    @Expose
    private boolean expand = false;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isNotExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }


}
