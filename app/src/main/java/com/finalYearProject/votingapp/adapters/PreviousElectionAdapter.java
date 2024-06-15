package com.finalYearProject.votingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.model.PreviousElection;

import java.util.List;

public class PreviousElectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<PreviousElection> previousElections;
    private View.OnClickListener footerClickListener;
    private final boolean showFooter;

    public PreviousElectionAdapter(List<PreviousElection> previousElections, View.OnClickListener footerClickListener, boolean showFooter) {
        this.previousElections = previousElections;
        this.footerClickListener = footerClickListener;
        this.showFooter = showFooter;
    }

    @Override
    public int getItemViewType(int position) {
        if (showFooter && position >= Math.min(previousElections.size(), 4)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.election_card, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            PreviousElection election = previousElections.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.usernameTextView.setText(election.getUser_s_FullName());
            itemViewHolder.postTextView.setText(election.getPost());
            itemViewHolder.numberOfVotesTextView.setText(String.valueOf(election.getNumberOfVote_s()));
            itemViewHolder.levelTextView.setText(election.getUser_s_Level());
//            itemViewHolder.genderTextView.setText(election.getCandidate2());
//            itemViewHolder.postTextView.setText(election.getCandidate3());
        } else {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.viewAllTextView.setOnClickListener(footerClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (showFooter) {
            return Math.min(previousElections.size(), 4) + 1; // Show up to 4 items + footer
        } else {
            return previousElections.size(); // Show all items without footer
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView levelTextView;
        public TextView numberOfVotesTextView;
        public TextView departmentTextView;
        public TextView genderTextView;
        public TextView postTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.UserFullName);
            levelTextView = itemView.findViewById(R.id.user_sLevel);
            numberOfVotesTextView = itemView.findViewById(R.id.voteCount);
            postTextView = itemView.findViewById(R.id.post);
//            genderTextView = itemView.findViewById(R.id.genderTextView);
//            postTextView = itemView.findViewById(R.id.postTextView);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView viewAllTextView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            viewAllTextView = itemView.findViewById(R.id.viewAllTextView);
        }
    }
}
