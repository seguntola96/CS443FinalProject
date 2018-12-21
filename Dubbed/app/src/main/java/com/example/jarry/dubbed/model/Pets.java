package com.example.jarry.dubbed.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pets {

    private int pet_vet_id;
    private int pet_id;
    private String name;
    private String breed;
    private String animal;
    private String owner_name;
    private String imagePath;
    private String lastVisit;
    private int petAge;

    public int getPet_vet_id() {
        return pet_vet_id;
    }

    public void setPet_vet_id(int pet_vet_id) {
        this.pet_vet_id = pet_vet_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getAnimal() {
        return animal;
    }

    public void setPet_id(int pet_id){
        this.pet_id = pet_id;
    }

    public int getPet_id(){
        return pet_id;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setLastVisit(String lastVisit){
        this.lastVisit = lastVisit;
    }

    public String getLastVisit(){
        return lastVisit;
    }

    public int getPetAge(){
        return petAge;
    }

    public void setPetAge(int petAge){
        this.petAge = petAge;
    }

}
