package com.finalYearProject.votingapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.model.Request;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {
    private List<Request> requests;
    private OnRequestActionListener listener;
    private OnRequestCardClickListener listener2;

    public interface OnRequestCardClickListener {
        void OnRequestCardClick(String category);
    }

    public interface OnRequestActionListener {
        void onApprove(Request request);
        void onDecline(Request request);
    }

    public RequestsAdapter(List<Request> requests, OnRequestActionListener listener) {
        this.requests = requests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.bind(request, listener, listener2);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserName;
        private TextView textViewCategory;
        private Button buttonApprove;
        private Button buttonDecline;
        private CardView notificationCardView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonDecline = itemView.findViewById(R.id.buttonDecline);
            notificationCardView = itemView.findViewById(R.id.notificationCard);
        }

        public void bind(final Request request, final OnRequestActionListener listener, final OnRequestCardClickListener listener2) {
            textViewUserName.setText(request.getUserName());
            textViewCategory.setText(request.getCategory());

            buttonApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onApprove(request);
                }
            });

            buttonDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDecline(request);
                }
            });

            notificationCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener2.OnRequestCardClick(request.getUserId());
                }
            });
        }
    }
}

