package com.iit.rentals.models;

public class UserBookmark {
    public String id;
    public String user_id;
    public String post_id;
    public String categoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public UserBookmark() {
    }

    public UserBookmark(String id, String user_id, String post_id, String categoryName) {
        this.id = id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.categoryName = categoryName;
    }
}
