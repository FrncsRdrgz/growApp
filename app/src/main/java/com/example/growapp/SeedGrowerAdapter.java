package com.example.growapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SeedGrowerAdapter extends RecyclerView.Adapter<SeedGrowerAdapter.ViewHolder> {
    private static final String TAG = "SeedGrowerAdapter";
    private List<SeedGrower> seedGrowers = new ArrayList<>();

    private sendBtnClicked sendBtnClickedListener;
    private sgFormClicked sgFormClickedListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seeds, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SeedGrower seedGrower = seedGrowers.get(position);
        holder.tvVariety.setText(seedGrower.getVariety());
        holder.tvDateplanted.setText(seedGrower.getDateplanted());

    }

    @Override
    public int getItemCount() {
        return seedGrowers.size();
    }

    public void setSeedGrowers(List<SeedGrower> seedGrowers) {
        this.seedGrowers = seedGrowers;
        notifyDataSetChanged();
    }

    public SeedGrower getSeedGrowerAt(int position) {
        return seedGrowers.get(position);
    }

    public interface sendBtnClicked {
        void sendToServer(SeedGrower seedGrower);
    }

    public interface sgFormClicked {
        void editSGDetails(SeedGrower seedGrower);
    }

    public void setSgFormClickedListener(SeedGrowerAdapter.sgFormClicked sgFormClickedListener) {
        this.sgFormClickedListener = sgFormClickedListener;
    }

    public void setSendBtnClickedListener(SeedGrowerAdapter.sendBtnClicked sendBtnClickedListener) {
        this.sendBtnClickedListener = sendBtnClickedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvVariety, tvDateplanted;
        Button sendBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            tvVariety = (TextView) itemView.findViewById(R.id.tvVariety);
            tvDateplanted = (TextView) itemView.findViewById(R.id.tvDateplanted);
            sendBtn = (Button) itemView.findViewById(R.id.sendBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (sgFormClickedListener != null && position != RecyclerView.NO_POSITION) {
                        sgFormClickedListener.editSGDetails(seedGrowers.get(position));
                    }
                }
            });

            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (sendBtnClickedListener != null && position != RecyclerView.NO_POSITION) {
                        sendBtnClickedListener.sendToServer(seedGrowers.get(position));
                    }
                }
            });
        }
    }

}
