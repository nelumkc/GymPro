package com.example.gympro.Model;

public class Exercise {

    private String name;
    private String type;
    private String muscle;
    private String difficulty;

    private String instructions;

    private String equipment;


    private String hashcode;

    private String imageUrl;

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public Exercise() {
    }



    public Exercise(String name, String type, String muscle, String difficulty) {
        this.name = name;
        this.type = type;
        this.muscle = muscle;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getInstructions() {
        return instructions;
    }
}
