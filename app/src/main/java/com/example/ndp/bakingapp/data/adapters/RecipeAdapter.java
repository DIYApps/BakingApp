package com.example.ndp.bakingapp.data.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final OnItemClickedListener mListener;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private Context mContext;
    private final int[] images =  {R.drawable.f1 , R.drawable.f2, R.drawable.f3 ,R.drawable.f4};

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
        mContext = parent.getContext();
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
        holder.recipeServingsTextView.setText("Servings " +recipe.getServings());
        String image = recipe.getImage();
        if(ValidationUtils.isStringEmptyOrNull(image)){
            image = "/";
        }
        Picasso.with(mContext).load(image).error(images[position%4])
                .into(holder.recipeImageView);
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

        public String getRecipeName(){
            return recipeNameTextView.getText().toString();
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
