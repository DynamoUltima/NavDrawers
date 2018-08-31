package com.example.joel.navdrawers;


public class ExampleItem {
    private String mImageUrl;
    private String mCreator;
   // private int mLikes;

    public ExampleItem(String imageUrl, String creator) {
        mImageUrl = imageUrl;
        mCreator = creator;
       // mLikes = likes;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return mCreator;
    }

 /* public int getLikeCount() {
        return mLikes;
    }*/
}