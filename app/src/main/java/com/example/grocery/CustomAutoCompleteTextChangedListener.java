package com.example.grocery;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        // if you want to see in the logcat what the user types
        Log.e(TAG, "User input: " + userInput);

        RecipeActivity recipeActivity = ((RecipeActivity) context);

        // query the database based on the user input
        recipeActivity.item = recipeActivity.getItemsFromDb(userInput.toString());

        // update the adapater
        recipeActivity.myAdapter.notifyDataSetChanged();
        recipeActivity.myAdapter = new ArrayAdapter<String>(recipeActivity, android.R.layout.simple_dropdown_item_1line, recipeActivity.item);
        recipeActivity.myAutoComplete.setAdapter(recipeActivity.myAdapter);

    }

}
