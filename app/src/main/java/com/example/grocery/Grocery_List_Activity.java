package com.example.grocery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Grocery_List_Activity extends AppCompatActivity {

    private ListView lstGrocery;
    ArrayList<Item> local_recipe = new ArrayList<Item>();
    DataBaseHelper db = new DataBaseHelper(Grocery_List_Activity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery__list_);
        initialize();
        local_recipe = db.getallItems();
        RecView_For_Recipes groceryArrayAdapter = new RecView_For_Recipes(Grocery_List_Activity.this,local_recipe);
        lstGrocery.setAdapter(groceryArrayAdapter);

    }

    private void initialize() {
        lstGrocery = findViewById(R.id.lstGrocery);
    }
}