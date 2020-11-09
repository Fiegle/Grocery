package com.example.grocery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {

    //SET OUR STATIC VALUES SO CHANGES TO STRING VALUES THEMSELVES WILL
    // AUTOUPDATE FOR EVERYTHING AS THESE VARIABLES ARE WHAT ARE USED
    public static final String GROCERY_TABLE = "GROCERY_TABLE";
    public static final String COLUMN_ITEM = "ITEM";
    public static final String ITEM_TABLE = "ITEM_TABLE";
    public static final String COLUMN_ID = "Recipe_ID";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_R_NAME = "Recipe_Name";
    public static final String COLUMN_TYPE = "Type";
    public static final String RECIPE_LIST_TABLE = "RECIPE_LIST_TABLE";
    public static final String COLUMN_RECIPE_IMAGE = "Recipe_Image";

    //CREATE DATABASE CONSTRUCTOR
    public DataBaseHelper(@Nullable Context context) {
        //Assign Database file name and version
        super(context, "grocery.db", null, 1);
    }

    //WHAT TABLES I NEED TO MAKE UPON CREATION FOR FIRST ACCESS
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Used to store the items such as Chicken, beans, White Rice, etc...
        //This table will be used for our autocomplete to pull from and will add any new values the user types
        String createTableStatement = "CREATE TABLE " + GROCERY_TABLE + " (" + COLUMN_ITEM + " TEXT PRIMARY KEY)";
        //execute the table creation
        db.execSQL(createTableStatement);
        //This table will be used to store the ingredients for the recipes and will have a name like the grocery table does but also
        //it will have an amount, a recipe name for it to be linked to and a type which will be oz lb can etc.
        //This table will for our intents and purposes be comprised of Items from the Item class as those objects have been made to make
        //communication with the database easier
        createTableStatement = "CREATE TABLE " + ITEM_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_ITEM + " TEXT, " + COLUMN_R_NAME + " TEXT, "+ COLUMN_TYPE + " TEXT, " + COLUMN_AMOUNT + " FLOAT)";
        //execute the table creation
        db.execSQL(createTableStatement);
        //Recipe List Table will be used to store the unique name for each recipe as a primary key as well as a blob that will house the image that has been assigned to it
        //This table will be checked when someone is attempting to hit done on a new recipe to make sure the name is unique before adding all the Items to the Item table
        createTableStatement = "CREATE TABLE " + RECIPE_LIST_TABLE + " (" + COLUMN_R_NAME + " TEXT PRIMARY KEY, " + COLUMN_RECIPE_IMAGE + " BLOB NOT NULL)";
        //Execute the creation of the RECIPE_LIST_TABLE
        db.execSQL(createTableStatement);
        Log.d("GROCERY","Database was created for first time use");
    }

    //WHAT TO DO WHEN I CHANGE COLUMNS, VERSIONS, ETC FOR THE TABLES
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //SIMPLY DROP THE TABLES AND START ANEW
        db.execSQL("DROP TABLE IF EXISTS " + GROCERY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RECIPE_LIST_TABLE);
    }

    //ADDING NEW ENTRIES TO THE GROCERY LIST TABLE
    public boolean addgrocery(String name){
        //Get the database
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new contentvalues to store the data to be inserted
        ContentValues cv = new ContentValues();
        //put the name from the argument into the ContentValues
        cv.put(COLUMN_ITEM, name);
        //Store an int to test whether entry was successful or not and then call insert function to put data into database
        long insert = db.insert(GROCERY_TABLE, null, cv);
        //if the insert returns -1 to insert variable then it has failed
        if(insert == -1){
            return false;
        }
        return true;
    }

    //add an item to recipe table
    public boolean addItem(Item item){
        //GET CURRENT DATABASE
        SQLiteDatabase db = this.getWritableDatabase();
        //make a contentvalues to be used to store values to be inserted as a row into the database
        ContentValues cv = new ContentValues();

        //Put the values from the item object passed by the argument of the function by using its getter functions
        cv.put(COLUMN_ITEM, item.getItem_Name());
        cv.put(COLUMN_R_NAME, item.getRecipe_Name());
        cv.put(COLUMN_TYPE, item.getType());
        cv.put(COLUMN_AMOUNT, item.getAmount());

        //Store an int to test whether entry was successful or not and then call insert function to put data into database
        long insert = db.insert(ITEM_TABLE, null, cv);
        //if the insert long that we assigned to get the return value from the insert function is -1 then the insert failed
        if(insert == -1){
            return false;
        }
        return true;
    }

    //Get all the items stored for all recipes
    //AKA get every row from the ITEM_TABLE in our database
    public ArrayList<Item> getallItems(){
        //make the arraylist that will be returned
        ArrayList<Item> returnList = new ArrayList<>();

        //set query string that can be changed and used for other function queries
        String queryString = "SELECT * FROM " + ITEM_TABLE;

        //get the readable database as we do not need to write to it only view the set information
        SQLiteDatabase db = this.getReadableDatabase();

        //set up the cursor which will hold the result of our query
        Cursor cursor = db.rawQuery(queryString, null);

        //make sure there are actual values in cursor aka results from the query
        if(cursor.moveToFirst()){
            //loop though results
            do{
                //set column to its type (for complex objects remember to know your index for each column and make sure to make a new object then add that to the returnList)
                String name = cursor.getString(1);
                String recipe_name = cursor.getString(2);
                String type = cursor.getString(3);
                Double amount = cursor.getDouble(4);
                //Use the values stored from the columns of each row to be used in the Item constructor to get our Item object that we can put into our returnlist of items
                Item placeholder = new Item(amount,name,recipe_name,type);
                //Add the newly created item to the returnlist
                returnList.add(placeholder);

                //while there are other values still in cursor
            }while(cursor.moveToNext());
        }
        else{

        }
        //close the cursor and then database
        cursor.close();
        db.close();
        //return our list of items
        return returnList;
    }

    //Get the items for as set recipe
    public ArrayList<Item> getItemsforrecipe(String recipe){
        //allocate memory for the returnlist we will be using to return an ArrayList of Items
        ArrayList<Item> returnList = new ArrayList<>();

        //set query string that can be changed and used for other function queries
        String queryString = "SELECT * FROM " + ITEM_TABLE;

        //get the readable database as we do not need to write to it only view the set information
        SQLiteDatabase db = this.getReadableDatabase();

        //set up the cursor which will hold the result of our query
        Cursor cursor = db.rawQuery(queryString, null);

        //make sure there are actual values in cursor aka results from the query
        if(cursor.moveToFirst()){
            //loop though results
            do{
                //set column to its type (for complex objects remember to know your index for each column and make sure to make a new object then add that to the returnList)
                String name = cursor.getString(1);
                String recipe_name = cursor.getString(2);
                String type = cursor.getString(3);
                Double amount = cursor.getDouble(4);

                //check to see if the recipe for the item we have matches our recipe_name we are passing as an argument
                if(recipe == recipe_name){
                    //if it matches then we create an Item Object using tha constructor with values pulled from our cursor
                    Item placeholder = new Item(amount,name,recipe_name,type);
                    //add the item to the returnlist
                    returnList.add(placeholder);
                }
                //while there are other values still in cursor
            }while(cursor.moveToNext());
        }
        else{

        }
        //close the cursor and database
        cursor.close();
        db.close();
        //return our list of Items
        return returnList;
    }

    //Check to see if a recipename is unique so that a new recipe can then be added to the table
    //since recipe names are primary keys in our table we want to make sure we are unique before we add values to our database
    public boolean IsUnique(String recipe_name){
        //create our query statement
        //we could use the SQL code to actually pass recipe in as a parameter to check but have not
        String queryString = "SELECT * FROM " + RECIPE_LIST_TABLE;
        //get our database, NOTE: We only need it to be readable as we are comparing against and not actually writing
        SQLiteDatabase db = this.getReadableDatabase();
        //create our cursor to pass through each item
        Cursor cursor = db.rawQuery(queryString, null);
        //check if there exists a row (is table not empty)
        if(cursor.moveToFirst()){
            do{
                //simply get the recipe name
                String name = cursor.getString(0);
                if(name == recipe_name){
                    return false;
                }
                //while there are still row
            }while(cursor.moveToNext());
        }
        return true;
    }

    //add a recipe to the database aka add Recipe and image to RECIPE_LIST_TABLE and then add each subsequent entered item to the ITEM_TABLE
    public boolean addRecipe(ArrayList<Item> item, String recipe_name, String x){
        //GET CURRENT DATABASE
        SQLiteDatabase db = this.getWritableDatabase();
        //make a contentvalues to be used to store values to be inserted as a row into the database
        ContentValues cv = new ContentValues();
        ContentValues item_cv = new ContentValues();
        long insert;
        //First insert the unique recipe name into recipe name table
        /*boolean success = add_to_recipe_list(recipe_name,x);
        if(!success){
            Log.d("SUCCESS","function failed");
        }*/

        //COMMENT THESE 6 lines out after we figure out image issue
        cv.put(COLUMN_R_NAME, recipe_name);
        cv.put(COLUMN_RECIPE_IMAGE, R.drawable.ic_launcher_background);
        long test = db.insert(RECIPE_LIST_TABLE, null, cv);
        if(test == -1){
            Log.d("SUCCESS", "INSERT INTO RECIPE_LIST_TABLE FAILED");
        }


        //Now to add to the Item Table from the list

        //Get the size of the list
        int size = item.size();
        //loop through the list
        for(int i =0; i<size;i++){
            //make the contentvalues
            cv = new ContentValues();

            //Get the name
            cv.put(COLUMN_ITEM, item.get(i).getItem_Name());

            //if the name is unique we will add it to our grocery table first
            if(IsNewItem(item.get(i).getItem_Name())){
                //get the contentvalues
                item_cv = new ContentValues();
                //store the name
                item_cv.put(COLUMN_ITEM, item.get(i).getItem_Name());
                //insert the name into the GROCERY_TABLE
                insert = db.insert(GROCERY_TABLE,null,item_cv);
                //if it fails
                if(insert == -1){
                    return false;
                }

            }
            //put the rest of the values in the other ContentValues (cv) to then be inserted into our ITEM_TABLE
            cv.put(COLUMN_R_NAME, recipe_name);
            cv.put(COLUMN_TYPE, item.get(i).getType());
            cv.put(COLUMN_AMOUNT, item.get(i).getAmount());

            //Insert the Contents into the ITEM_TABLE
            insert = db.insert(ITEM_TABLE, null, cv);
            if(insert == -1){
                return false;
            }
        }


        return true;
    }

    //Check if a value entered in the name of an item is currently in the Grocery table
    //this is important because we use the grocery table to autocomplete their ingredient/items for each recipe
    //if an item is new, we want to know so that in other functions we can add it to the grocery table so the next time that name is used it is autocompleted for the user
    public boolean IsNewItem(String item_name){
        //Pull all from the GROCERY_TABLE
        String querystring = "SELECT * FROM " + GROCERY_TABLE;
        //Get the database NOTE: We only need readable as we are not changing anything here only comparing against the argument item_name
        SQLiteDatabase db = this.getReadableDatabase();
        //create the cursor to be used to parse the returned query
        Cursor cursor = db.rawQuery(querystring, null);
        //check if any rows exist
        if(cursor.moveToFirst()){
            do{
                //get the name of the row in GROCERY_TABLE
                String name = cursor.getString(0);
                //Check if the name matches what was passed
                if(name == item_name){
                    return false;
                }
                //while there are still rows left to check against
            }while(cursor.moveToNext());
        }
        //if we got to the end of our loop then we know that no matching name in the grocery_table was found so this must be a new entry
        return true;
    }


    //Get all values in grocery_table
    public List<String> getGroceryTable(){
        //create our return type
        List<String> returnList = new ArrayList<>();

        //set query string that can be changed and used for other function queries
        String queryString = "SELECT * FROM " + GROCERY_TABLE;

        //get the readable database as we do not need to write to it only view the set information
        SQLiteDatabase db = this.getReadableDatabase();

        //set up the cursor which will hold the result of our query
        Cursor cursor = db.rawQuery(queryString, null);

        //make sure there are actual values in cursor aka results from the query
        if(cursor.moveToFirst()){
            //loop though results
            do{
                //set column to its type (for complex objects remember to know your index for each column and make sure to make a new object then add that to the returnList)
                String name = cursor.getString(0);
                returnList.add(name);

            //while there are other values still in cursor
            }while(cursor.moveToNext());
        }
        else{

        }
        //close the cursor and database
        cursor.close();
        db.close();
        return returnList;
    }

    //This will be used for our RecyclerView on the Mainactivity to get all the recipes and their images to be displayed in a modern fashion
    public ArrayList<Recipe> getrecipes(){
        //create our return type
        ArrayList<Recipe> returnList = new ArrayList<>();

        //set query string that can be changed and used for other function queries
        String queryString = "SELECT * FROM " + RECIPE_LIST_TABLE;

        //get the readable database as we do not need to write to it only view the set information
        SQLiteDatabase db = this.getReadableDatabase();

        //set up the cursor which will hold the result of our query
        Cursor cursor = db.rawQuery(queryString, null);
        Recipe recipe = new Recipe();
        //make sure there are actual values in cursor aka results from the query
        if(cursor.moveToFirst()){
            //loop though results
            do{
                //set column to its type (for complex objects remember to know your index for each column and make sure to make a new object then add that to the returnList)
                String name = cursor.getString(0);
                recipe.setName(name);
                //currently we are using the ic_launcher_background instead of the image itself, this will change
                Log.d("GROCERY", "REMEMBER WE ARE USING THE IC BACKGROUND IN GET RECIPES !!NEED TO CHANGE!!");
                recipe.setImage(R.drawable.ic_launcher_background);
                //add the recipe to the list
                returnList.add(recipe);

                //while there are other values still in cursor
            }while(cursor.moveToNext());
        }
        else{

        }
        //close the cursor and the database
        cursor.close();
        db.close();
        return returnList;
    }

    //USE THIS TO SETUP HOW OUR AUTOCOMPLETE FORM WILL
    public List<String> read(String searchTerm) {

        List<String> recordsList = new ArrayList<String>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + GROCERY_TABLE;
        sql += " WHERE " + COLUMN_ITEM + " LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY " + COLUMN_ITEM + " DESC";
        sql += " LIMIT 0,5";

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // IF OBJECT: int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String objectName = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM));
                //IF OBJECT: MyObject myObject = new MyObject(objectName);

                // add to list
                recordsList.add(objectName);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return the list of records
        return recordsList;
    }
    
    //INSERTING DEFAULT GROCERY ITEMS
    public boolean insert_default(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ITEM,"Chicken");
        long insert = db.insert(GROCERY_TABLE, null,cv);
        if(insert == -1){
            return false;
        }
        return true;
    }
    
    //USE THIS TO ADD RECIPE NAME AND IMAGE TO RECIPE_LIST_TABLE
    public boolean add_to_recipe_list(String recipe_name, String x){
        Log.d("SUCCESS","Got to 1");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("SUCCESS","Got to Database");

        try {
            long success;
            Log.d("SUCCESS","Got to the variable");
            Log.d("SUCCESS", x);
            //THIS IS WHERE I AM GETTING MY ERROR
            //W/System.err: java.io.FileNotFoundException: /storage/emulated/0/Download/william-moreland-auijD19Byq8-unsplash.jpg: open failed: EACCES (Permission denied)
            FileInputStream fs = new FileInputStream(x);
            Log.d("SUCCESS","Got to 2");
            byte[] imgByte = new byte[fs.available()];
            Log.d("SUCCESS","Got to 3");
            fs.read(imgByte);
            Log.d("SUCCESS","Got to 4");
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_R_NAME, recipe_name);
            contentValues.put(COLUMN_RECIPE_IMAGE, imgByte);
            Log.d("SUCCESS","Got to here");
            success = db.insert(RECIPE_LIST_TABLE, null, contentValues);


            fs.close();
            if (success == -1) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }
    
    //updating a recipe image
    public boolean updateImage(String recipe_name, String x){
        String queryString = "SELECT * FROM " + RECIPE_LIST_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        try {
            long success;
            FileInputStream fs = new FileInputStream(x);
            byte[] imgByte = new byte[fs.available()];
            fs.read(imgByte);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_R_NAME, recipe_name);
            contentValues.put(COLUMN_RECIPE_IMAGE, imgByte);
            success = db.update(RECIPE_LIST_TABLE, contentValues, "EMPLOYEE_EMAIL = ?", new String[]{recipe_name});

            fs.close();
            if (success == -1) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;


    }
    
    //checking if an image exists
    public boolean imageExists(String recipe_name){
        String queryString = "SELECT * FROM " + RECIPE_LIST_TABLE;
        boolean exists = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(0);
                if(recipe_name.equals(name)){
                    exists = true;
                    break;
                }
            }while(cursor.moveToNext());
        }
        return exists;
    }
    
    //getting the bitmap from a recipe in the database
    public Bitmap getImage(String name){
        //get our database
        SQLiteDatabase db = this.getWritableDatabase();
        //declare our return variable
        Bitmap bt = null;
        //find the recipe name to get the image from
        Cursor cursor = db.rawQuery("SELECT * FROM " + RECIPE_LIST_TABLE + " WHERE " + COLUMN_R_NAME + "=?", new String[]{name});
        //if it exists
        if(cursor.moveToNext()){
            //store the blob as a bytearray to then be made into a bitmap
            byte[] imag = cursor.getBlob(1);
            //store the bytearray into the bitmap by decoding it
            bt = BitmapFactory.decodeByteArray(imag, 0, imag.length);
        }
        return bt;
    }
}
