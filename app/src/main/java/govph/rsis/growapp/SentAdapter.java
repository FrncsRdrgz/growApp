package govph.rsis.growapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import govph.rsis.growapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SentAdapter extends RecyclerView.Adapter<SentAdapter.ViewHolder> {
    public static final String TAG = "SentAdapter";
    private List<SeedGrower> seedGrowers = new ArrayList<>();
    private viewBtn viewBtnClickedListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SentAdapter.ViewHolder holder, int position) {
        SeedGrower seedGrower = seedGrowers.get(position);
        holder.textViewVariety.setText(seedGrower.getVariety());
        holder.textViewDateplanted.setText(seedGrower.getDateplanted());
    }

    public void setSeedGrowers(List<SeedGrower> seedGrowers) {
        this.seedGrowers = seedGrowers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return seedGrowers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVariety, textViewDateplanted;
        Button viewBtn;
        public ViewHolder(View itemView) {
            super(itemView);

            textViewVariety = itemView.findViewById(R.id.textViewVariety);
            textViewDateplanted = itemView.findViewById(R.id.textViewDateplanted);
            viewBtn = itemView.findViewById(R.id.viewBtn);

            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(viewBtnClickedListener != null && position != RecyclerView.NO_POSITION){
                        viewBtnClickedListener.viewSelectedForm(seedGrowers.get(position));
                    }
                }
            });
        }
    }

    public interface viewBtn {
        void viewSelectedForm(SeedGrower seedGrower);
    }

    public void setViewBtnClickedListener(SentAdapter.viewBtn viewBtnClickedListener) {
        this.viewBtnClickedListener = viewBtnClickedListener;
    }
}
