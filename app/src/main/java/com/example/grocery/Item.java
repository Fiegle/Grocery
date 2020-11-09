package com.example.grocery;

public class Item {
    //private member data
    private double Amount;
    private String Item_Name;
    private String Recipe_Name;
    private String Type;

    //Default Constructor, used for errors or lacking data
    public Item(){
        Item_Name = "ERROR";
        Amount = -1;
    }

    //Constructor we will use

    public Item(double amount, String item_Name, String recipe_Name, String type) {
        Amount = amount;
        Item_Name = item_Name;
        Recipe_Name = recipe_Name;
        Type = type;
    }

    public Item(double amount, String item_Name, String type) {
        Amount = amount;
        Item_Name = item_Name;
        Type = type;
        Recipe_Name= "Bonker";
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public String getRecipe_Name() {
        return Recipe_Name;
    }

    public void setRecipe_Name(String recipe_Name) {
        Recipe_Name = recipe_Name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    //TO STRING FUNCTION FOR DEBUGGING

    @Override
    public String toString() {
        return "Item{" +
                "Amount=" + Amount +
                ", Item_Name='" + Item_Name + '\'' +
                ", Recipe_Name='" + Recipe_Name + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
