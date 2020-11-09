package com.example.grocery;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {
DataBaseHelper db;
ArrayAdapter<String> myAdapter;
CustomAutoCompleteView myAutoComplete;
EditText edtName, edtAmount;
ListView lstItems;
Button btnAdd, btnDone;
ImageView imgRecipe;
Spinner spnType;
List<String> spinnerArray =  new ArrayList<String>();
ArrayList<Item> local_recipe = new ArrayList<Item>();
String[] item = new String[] {"Please search..."};

private static final int PICK_IMAGE = 100;
String x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initialize();
        fill_spinner();

        try{

            // instantiate database handler
            db = new DataBaseHelper(RecipeActivity.this);

            // autocompletetextview is in activity_main.xml
            myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

            // set our adapter
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete.setAdapter(myAdapter);


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Construct the data source




        //ADDING AN ITEM TO A RECIPE
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item(Double.parseDouble(edtAmount.getText().toString()),myAutoComplete.getText().toString(),spnType.getSelectedItem().toString());
                local_recipe.add(item);
                myAutoComplete.setText("");
                edtAmount.setText("");
                RecView_For_Recipes groceryArrayAdapter = new RecView_For_Recipes(RecipeActivity.this,local_recipe);
                lstItems.setAdapter(groceryArrayAdapter);
            }
        });
        //WHAT HAPPENS ONCE TEY HAVE FINISHED WITH THEIR RECIPE
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.IsUnique(edtName.getText().toString())){
                    Toast.makeText(RecipeActivity.this, "Unique Recipe Name",Toast.LENGTH_SHORT).show();
                    db.addRecipe(local_recipe, edtName.getText().toString(),x);
                    Intent intent = new Intent(RecipeActivity.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(RecipeActivity.this, "That Recipe Name Already Exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //WHEN IMAGE IS CLICKED
        imgRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(
                        "content://media/internal/images/media"
                ));
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


    }
    //USE TO AUTOFILL WITH RESULTS FROM THE GROCERY TABLE
    public String[] getItemsFromDb(String searchTerm){

        // add items on the array dynamically
        List<String> products = db.read(searchTerm);
        int rowCount = products.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (String record : products) {

            item[x] = record;
            x++;
        }

        return item;
    }
    void initialize(){
        edtName = findViewById(R.id.edtName);
        edtAmount = findViewById(R.id.edtAmount);
        btnAdd = findViewById(R.id.btnAdd);
        btnDone = findViewById(R.id.btnDone);
        spnType = findViewById(R.id.spnType);
        lstItems = findViewById(R.id.lstItems);
        imgRecipe = findViewById(R.id.imgRecipe);
    }
    void fill_spinner(){
        spinnerArray.add("oz");
        spinnerArray.add("Lb");
        spinnerArray.add("Cup");
        spinnerArray.add("Tsp");
        spinnerArray.add("Tbsp");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri uri = data.getData();
            x = getPath(uri);
            //db.add_to_recipe_list(edtName.getText().toString(),x);
                /*boolean worked = db.add_to_recipe_list(edtName.getText().toString(),x);

            if(worked){
                Toast.makeText(this, "Succesfully Added Image", Toast.LENGTH_SHORT).show();
                //refresh(currentEmployee.getEmail());

            }
            else{
                Toast.makeText(this, "Failed to Update Profile", Toast.LENGTH_SHORT).show();
            }*/

        }
    }

    public String getPath(Uri uri){
        if(uri == null){
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

}
