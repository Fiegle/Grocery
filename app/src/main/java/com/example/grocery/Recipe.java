package com.example.grocery;

import java.util.List;

public class Recipe {

    //Private Member Data
    private String Name;
    private List<Item> item_list;
    private int image;

    //Default Constructor
    public Recipe(){
        Name = "ERROR";
    }


    //Actual constructor which will be used for recipes
    public Recipe(String name, List<Item> item_list) {
        Name = name;
        this.item_list = item_list;
    }

    //Getter for Name
    public String getName() {
        return Name;
    }
    //Setter for Name
    public void setName(String name) {
        Name = name;
    }
    //Getter for Item List
    public List<Item> getItem_list() {
        return item_list;
    }
    //Setter for Item List
    public void setItem_list(List<Item> item_list) {
        this.item_list = item_list;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    //ToString method for debugging and printing
    @Override
    public String toString() {
        return "Recipe{" +
                "Name='" + Name + '\'' +
                ", item_list=" + item_list +
                '}';
    }
}
