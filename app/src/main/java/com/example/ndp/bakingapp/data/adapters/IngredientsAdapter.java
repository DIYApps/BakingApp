package com.example.ndp.bakingapp.data.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.data.models.Ingredient;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter
        <IngredientsAdapter.IngredientsViewHolder> {
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    private Context mContext;

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.ingredients_list_item_layout;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.textViewIngredientName.setText(ingredient.getIngredient().toUpperCase());
        String measure = ingredient.getMeasure();
        String quantity = ingredient.getQuantity() + " " + measure + "(s)";
        holder.textViewIngredientQuantity.setText(quantity);
        Drawable drawable = getDrawable(R.drawable.ic_spoon);

        //select the drawable
        Log.d("temp" , measure);
        if (!ValidationUtils.isStringEmptyOrNull(measure)) {
            Log.d("temp 1.1" , measure);
            if (measure.contains("CUP")) {
                drawable = getDrawable(R.drawable.ic_measuring_cup);
            } else if (measure.contains("TBLSP") || measure.contains("TSP")) {
                drawable = getDrawable(R.drawable.ic_spoon);
            } else {
                drawable = getDrawable(R.drawable.ic_weight);
            }
        }
        drawable.setBounds(0, 0, 48, 48);
        holder.textViewIngredientQuantity.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    public int getItemCount() {
        return ValidationUtils.isListEmptyOrNull(ingredients) ? 0 : ingredients.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewIngredientsName)
        TextView textViewIngredientName;

        @BindView(R.id.textViewIngredientsQuantity)
        TextView textViewIngredientQuantity;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Drawable getDrawable(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mContext.getDrawable(id);
        }
        return mContext.getResources().getDrawable(id);
    }
}
