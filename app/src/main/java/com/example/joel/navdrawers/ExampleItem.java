package com.example.joel.navdrawers;


public class ExampleItem {
    private String mImageUrl;
    private String mCreator;
    private String mId;

   // private int mLikes;




    public ExampleItem(String mImageUrl, String mCreator, String mId) {

        this.mImageUrl = mImageUrl;
        this.mCreator = mCreator;
        this.mId = mId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return mCreator;
    }
    public String getmId() {
        return mId;
    }

 /* public int getLikeCount() {
        return mLikes;
    }*/
}