package com.example.gympro.Model;

public class UserProfile {
    private String userId;
    private String name;
    private String email;

    private int weight;
    private int height;
    private int age;

    public UserProfile() {
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserProfile(String userId, String name, String email, int weight, int height, int age) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
