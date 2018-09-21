package com.example.joel.navdrawers;

public class MessageInfo {
    private String mTitle;
    private String mGenre;
    private String mSource;
    private String mAuthor;
   // private String mImage;



    public MessageInfo(String mTitle, String mGenre, String mSource, String mAuthor) {
        this.mTitle = mTitle;
        this.mGenre = mGenre;
        this.mSource = mSource;
        this.mAuthor = mAuthor;
        //this.mImage = mMessageImage;
    }

    public String getmTitle() {

        return mTitle;
    }

    public String getmGenre() {
        return mGenre;
    }

    public String getmSource() {
        return mSource;
    }

    public String getmAuthor() {
        return mAuthor;
    }
//    public String getmMessageImage() {
//        return mImage;
//    }



}
