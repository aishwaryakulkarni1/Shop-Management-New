
package com.inevitablesol.www.shopmanagement.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shopId")
    @Expose
    private String shopId;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ImageLink")
    @Expose
    private String imageLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "DocumentList{" +
                "id=" + id +
                ", shopId='" + shopId + '\'' +
                ", imageName='" + imageName + '\'' +
                ", type='" + type + '\'' +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}
