package com.finalYearProject.votingapp.model;

public class Candidate {
    String name;
    String party;
    String position;
    String id;
    int count=0;
    public Candidate(String name,String party,String position,String id){
        this.name=name;
        this.party=party;
        this.position=position;
        this.id=id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
