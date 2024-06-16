package com.finalYearProject.votingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.model.Candidate;

import java.util.List;

public class CandidatesAdapter extends RecyclerView.Adapter<CandidatesAdapter.CandidateViewHolder> {
    private List<Candidate> candidates;

    public CandidatesAdapter(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidate, parent, false);
        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        Candidate candidate = candidates.get(position);
        holder.bind(candidate);
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    static class CandidateViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView votesTextView;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            votesTextView = itemView.findViewById(R.id.votesTextView);
        }

        public void bind(Candidate candidate) {
            nameTextView.setText(candidate.getName());
            //votesTextView.setText(String.valueOf(candidate.getVotes()));
        }
    }
}

