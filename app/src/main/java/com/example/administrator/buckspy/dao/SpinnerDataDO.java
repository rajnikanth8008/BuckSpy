package com.example.administrator.buckspy.dao;

public class SpinnerDataDO {
    int imageDrawable;
    String category;

    public SpinnerDataDO(int imageDrawable, String category) {
        this.imageDrawable = imageDrawable;
        this.category = category;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
