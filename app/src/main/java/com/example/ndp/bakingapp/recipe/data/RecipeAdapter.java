package com.example.ndp.bakingapp.recipe.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.recipe.data.models.Recipe;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private OnItemClickedListener mListener;
    private ArrayList<Recipe> mRecipeList = new ArrayList();

    public RecipeAdapter(OnItemClickedListener mListener) {
        this.mListener = mListener;
    }

    public void setRecipeList(ArrayList<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_item_layout;
        LayoutInflater inflater = LayoutInflater.from( mContext );
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate( layoutIdForListItem, parent, shouldAttachToParentImmediately );
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe =  mRecipeList.get(position);
        holder.recipeNameTextView.setText(recipe.getName());
        holder.recipeServingsTextView.setText("Servings" +recipe.getServings());
    }

    @Override
    public int getItemCount() {
        if(ValidationUtils.isListEmptyOrNull(mRecipeList)) {
            return 0;
        }
        return mRecipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.imageView)
        ImageView recipeImageView;

        @BindView(R.id.textViewRecipeName)
        TextView recipeNameTextView;

        @BindView(R.id.textViewServing)
        TextView recipeServingsTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemClickedListener{
        void onItemClicked(int position);
    }
}
