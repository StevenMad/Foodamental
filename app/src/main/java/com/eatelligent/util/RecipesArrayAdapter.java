package com.eatelligent.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eatelligent.R;

import java.util.List;

/**
 * Created by Madhow on 18/07/2016.
 */
public class RecipesArrayAdapter extends ArrayAdapter<RecipeItem>{

    public RecipesArrayAdapter(Context context, int resource, List<RecipeItem> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_row_layout,parent,false);
        }
        RecipesListViewHolder rlvh = (RecipesListViewHolder) convertView.getTag();
        if(rlvh==null)
        {
            rlvh = new RecipesListViewHolder();
            rlvh.image = (ImageView) convertView.findViewById(R.id.imageRecipe);
            rlvh.Title = (TextView) convertView.findViewById(R.id.RecipeNameInList);
            rlvh.cookingTime = (TextView) convertView.findViewById(R.id.RecipeCookTime);
            rlvh.nbServe = (TextView) convertView.findViewById(R.id.RecipeNumberService);
            convertView.setTag(rlvh);
        }

        RecipeItem item = getItem(position);

        rlvh.Title.setText(item.getName());
        rlvh.image.setImageBitmap(item.getImage());
        rlvh.cookingTime.setText(item.getCookingTime()+" min");
        rlvh.nbServe.setText(item.getNbServe()+" pers");
        return convertView;
    }
}
