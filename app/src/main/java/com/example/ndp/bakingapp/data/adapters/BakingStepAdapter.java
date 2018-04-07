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
import com.example.ndp.bakingapp.data.models.BakingSteps;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingStepAdapter extends RecyclerView.Adapter
        <BakingStepAdapter.BakingStepViewHolder> {
    ArrayList<BakingSteps> steps = new ArrayList<>();
    private Context mContext;
    private OnItemClickedListener onItemClickedListener;

    public BakingStepAdapter(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setSteps(ArrayList<BakingSteps> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BakingStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.baking_steps_item_layout;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new BakingStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BakingStepViewHolder holder, int position) {
        holder.textViewBakingStep.setText(steps.get(position).getShortDescription());
        holder.textViewStepNumber.setText(steps.get(position).getId()+"");
        if(ValidationUtils.isStringEmptyOrNull(steps.get(position).getVideoURL())){
            holder.imageViewIsVideoPresent.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return ValidationUtils.isListEmptyOrNull(steps) ? 0 : steps.size();
    }

    public class BakingStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textViewShortDescription)
        TextView textViewBakingStep;

        @BindView(R.id.textViewStepNumber)
        TextView textViewStepNumber;

        @BindView(R.id.imageViewIsVideoPresent)
        ImageView imageViewIsVideoPresent;

        public BakingStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickedListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemClickedListener{
        void onItemClicked(int position);
    }
}
