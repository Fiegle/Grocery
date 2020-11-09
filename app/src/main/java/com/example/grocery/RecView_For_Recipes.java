package com.example.grocery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class RecView_For_Recipes extends ArrayAdapter<Item> {
    public RecView_For_Recipes(Context context, ArrayList<Item> items) {

        super(context, 0, items);

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        Item item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_rec_view, parent, false);

        }

        // Lookup view for data population

        EditText edtItem_Name = (EditText) convertView.findViewById(R.id.edtItem_Name_rec);
        EditText edtAmount_rec = (EditText) convertView.findViewById(R.id.edtAmount_rec);
        EditText edtType_rec = (EditText) convertView.findViewById(R.id.edtType_rec);


        // Populate the data into the template view using the data object
        edtItem_Name.setText(item.getItem_Name());
        edtAmount_rec.setText(String.valueOf(item.getAmount()));
        edtType_rec.setText(item.getType());

        // Return the completed view to render on screen

        return convertView;

    }
}
