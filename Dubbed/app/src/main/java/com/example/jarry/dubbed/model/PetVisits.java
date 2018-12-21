package com.example.jarry.dubbed.model;

public class PetVisits {

    private int pet_id;
    private String date_of_visit;
    private String cause_of_visit;

    public void setPet_id(int pet_id){
        this.pet_id = pet_id;
    }

    public int getPet_id(){
        return pet_id;
    }

    public void setDate_of_visit(String date_of_visit){
        this.date_of_visit = date_of_visit;
    }

    public String getDate_of_visit(){
        return date_of_visit;
    }

    public void setCause_of_visit(String cause_of_visit){
        this.cause_of_visit = cause_of_visit;
    }

    public String getCause_of_visit(){
        return cause_of_visit;
    }
}
