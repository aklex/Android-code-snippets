package com.flexdecision.ak_lex.listview;

/**
 * Created by a_Lex on 3/22/2018.
 */

public class DataItem {
    private String headline;
    private String description;
    private int image;

    public DataItem(String headline, String description, int image) {
        this.headline = headline;
        this.description = description;
        this.image = image;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataItem data = (DataItem) o;

        if (image != data.image) return false;
        if (headline != null ? !headline.equals(data.headline) : data.headline != null)
            return false;
        return description != null ? description.equals(data.description) : data.description == null;
    }

    @Override
    public int hashCode() {
        int result = headline != null ? headline.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + image;
        return result;
    }
}
