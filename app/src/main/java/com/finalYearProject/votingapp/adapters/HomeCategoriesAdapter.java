package com.finalYearProject.votingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalYearProject.votingapp.R;

import java.util.List;

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.HomeCategoriesViewHolder> {
    private List<String> categories;
    private OnCategoryClickListener onCategoryClickListener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public HomeCategoriesAdapter(List<String> categories, OnCategoryClickListener onCategoryClickListener) {
        this.categories = categories;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public HomeCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_election_layout, parent, false);
        return new HomeCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoriesViewHolder holder, int position) {
        String category = categories.get(position);
        holder.bind(category, onCategoryClickListener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class HomeCategoriesViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTextView;

        public HomeCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.ogHpost);
        }

        public void bind(final String category, final OnCategoryClickListener listener) {
            categoryTextView.setText(category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
}
