package com.finalYearProject.votingapp.model;

public class PreviousElection {
    private String numberOfVote_s;
    private String post;
    private String user_s_FullName;
    private String user_s_Level;
    private String firstRunnerUp;
    private String secondRunnerUp;
    private String thirdRunnerUp;
    private String electionYear;

    // No-argument constructor for Firestore
    public PreviousElection(){

    }

    public PreviousElection(String numberOfVote_s, String post, String electionYear, String user_s_FullName, String user_s_Level, String firstRunnerUp, String secondRunnerUp, String thirdRunnerUp) {
        this.numberOfVote_s = numberOfVote_s;
        this.post = post;
        this.electionYear = electionYear;
        this.user_s_FullName = user_s_FullName;
        this.user_s_Level = user_s_Level;
        this.firstRunnerUp = firstRunnerUp;
        this.secondRunnerUp = secondRunnerUp;
        this.thirdRunnerUp = thirdRunnerUp;
    }

    public String getNumberOfVote_s() {
        return numberOfVote_s;
    }

    public void setNumberOfVote_s(String numberOfVote_s) {
        this.numberOfVote_s = numberOfVote_s;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getElectionYear() {
        return electionYear;
    }

    public void setElectionYear(String electionYear) {
        this.electionYear = electionYear;
    }

    public String getUser_s_FullName() {
        return user_s_FullName;
    }

    public void setUser_s_FullName(String user_s_FullName) {
        this.user_s_FullName = user_s_FullName;
    }

    public String getUser_s_Level() {
        return user_s_Level;
    }

    public void setUser_s_Level(String user_s_Level) {
        this.user_s_Level = user_s_Level;
    }

    public String getFirstRunnerUp() {
        return firstRunnerUp;
    }

    public void setFirstRunnerUp(String firstRunnerUp) {
        this.firstRunnerUp = firstRunnerUp;
    }

    public String getSecondRunnerUp() {
        return secondRunnerUp;
    }

    public void setSecondRunnerUp(String secondRunnerUp) {
        this.secondRunnerUp = secondRunnerUp;
    }

    public String getThirdRunnerUp() {
        return thirdRunnerUp;
    }

    public void setThirdRunnerUp(String thirdRunnerUp) {
        this.thirdRunnerUp = thirdRunnerUp;
    }
}
