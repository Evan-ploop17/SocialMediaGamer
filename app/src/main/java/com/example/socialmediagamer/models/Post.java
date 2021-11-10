package com.example.socialmediagamer.models;

public class Post {

    public String id;
    public String title;
    public String description;
    public String idUser;
    public String image1;
    public String image2;
    public String category;

    public Post() {

    }

    public Post(String id, String title, String description, String idUser, String image1, String image2, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.idUser = idUser;
        this.image1 = image1;
        this.image2 = image2;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
