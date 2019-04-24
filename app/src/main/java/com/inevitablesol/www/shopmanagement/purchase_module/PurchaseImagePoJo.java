package com.inevitablesol.www.shopmanagement.purchase_module;

/**
 * Created by Pritam on 25-04-2018.
 */

public class PurchaseImagePoJo
{
    private  String id;
    private  String imageName;
    private  String link;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getPurchaseId()
    {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    private  String purchaseId;

}
