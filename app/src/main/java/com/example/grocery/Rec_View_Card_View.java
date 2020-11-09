package com.example.grocery;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocery.R;
import com.example.grocery.Recipe;

import java.util.List;




public class Rec_View_Card_View extends RecyclerView.Adapter<Rec_View_Card_View.ViewHolder> {

    List<Recipe> recipe_list;
    Context context;

    public Rec_View_Card_View(List<Recipe>TvShowList)
    {
        this.recipe_list = recipe_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_rec_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Recipe recipe = recipe_list.get(position);

        holder.txtName.setText(recipe.getName());
        holder.imgRecipe.setImageResource(recipe.getImage());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return recipe_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgRecipe;
        TextView txtName;
        CardView cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imgRecipe = (ImageView)itemView.findViewById(R.id.imgRecipe);
            txtName = (TextView)itemView.findViewById(R.id.txtName);
            cv = (CardView)itemView.findViewById(R.id.cv);
        }

    }
}