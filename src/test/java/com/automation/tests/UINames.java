package com.automation.tests;

public class UINames {
    /**
     *     {
     *         "name": "Jose",
     *         "surname": "Pearson",
     *         "gender": "male",
     *         "region": "United States"
     *     }
     */
    private String name;
    private String surname;
    private String gender;
    private String region;

    public UINames(){
    }
    public UINames(String name, String surname, String gender, String region) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    @Override
    public String toString() {
        return "UINames{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender='" + gender + '\'' +
                ", region='" + region + '\'' +
                '}';
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
