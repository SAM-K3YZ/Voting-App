package com.finalYearProject.votingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.activities.AllCandidateActivity;
import com.finalYearProject.votingapp.activities.VotingActivity;
import com.finalYearProject.votingapp.model.Candidate;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {
    private AllCandidateActivity context;
    private List<Candidate> list;

    public CandidateAdapter(AllCandidateActivity context, List<Candidate> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ongoing_election_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getName());
        holder.department.setText(list.get(position).getDepartment());
        holder.position.setText(list.get(position).getPosition());

        //Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VotingActivity.class);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("post", list.get(position).getPosition());
                intent.putExtra("department", list.get(position).getDepartment());
                intent.putExtra("level", list.get(position).getLevel());
                intent.putExtra("manifesto", list.get(position).getManifesto());
                //intent.putExtra("image",list.get(position).getImage());
                intent.putExtra("id", list.get(position).getId());
                context.startActivity(intent);
                /*Activity activity= (Activity) context;
                activity.finish();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView image;
        private TextView name, position, department;
        //private Button voteBtn;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //image= itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.ogName);
            position = itemView.findViewById(R.id.post);
            department = itemView.findViewById(R.id.party);
            //voteBtn=itemView.findViewById(R.id.vote_btn);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}
