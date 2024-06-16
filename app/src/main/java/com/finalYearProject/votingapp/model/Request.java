package com.finalYearProject.votingapp.model;

public class Request {
    private String userId;
    private String userName;
    private String category;
    private boolean isApproved;

    // Constructors
    public Request() {}

    public Request(String userId, String userName, String category, boolean isApproved) {
        this.userId = userId;
        this.userName = userName;
        this.category = category;
        this.isApproved = isApproved;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}

