package com.example.grocery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnRecipe, btnView, btnGroceryList;
    private ListView lstItem;
    DataBaseHelper db;

    RecyclerView recyclerView;
    Rec_View_Card_View rec_view_card_view;
    ArrayList<Recipe> recipe_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the IDs for widgets
        initialize();


        try{
            //setup database to use Databasehelper functions
            db = new DataBaseHelper(MainActivity.this);
            //add sample data to make sure that there exists at least 1 item in Grocery_list
            insert_sample_data();
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }


        //Button Listener for New Recipe
        btnRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(intent);
            }
        });

        //Button Listener for Viewing Grocery_Table
        btnView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //DataBaseHelper db = new DataBaseHelper(MainActivity.this);

                //Get the list of all items in GROCERY_TABLE and store it in a list to use for our ListView Adapter
                List<String> everyone = db.getGroceryTable();
                //Create the ArrayAdapter to show the listview of all grocery items
                ArrayAdapter groceryArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, everyone);
                //set the adapter so it shows
                lstItem.setAdapter(groceryArrayAdapter);
            }

        });

        //Button Listener for changing activity to the Grocery_List_Activity
        btnGroceryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make an intent to change the activity to the Grocery_List_activity
                Intent intent = new Intent(MainActivity.this, Grocery_List_Activity.class);
                //start it so that it takes us to the next activity
                startActivity(intent);
            }
        });

    }
    //configure all xml widgets
    void initialize(){
        btnRecipe = findViewById(R.id.btnRecipe);
        btnView = findViewById(R.id.btnView);
        btnGroceryList = findViewById(R.id.btnGroceryList);
        lstItem = findViewById(R.id.lstItems);
    }
    //put 1 value in grocery table so that the auto complete will not malfunction
    void insert_sample_data(){
        db = new DataBaseHelper(MainActivity.this);
        db.addgrocery("Chicken");
    }

}