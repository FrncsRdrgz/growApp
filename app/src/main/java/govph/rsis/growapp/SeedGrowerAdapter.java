package govph.rsis.growapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeedGrowerAdapter extends RecyclerView.Adapter<SeedGrowerAdapter.ViewHolder> {
    private static final String TAG = "SeedGrowerAdapter";
    private List<SeedGrower> seedGrowers = new ArrayList<>();

    private sendBtnClicked sendBtnClickedListener;
    private sgFormClicked sgFormClickedListener;
    private finalizedBtnClicked finalizedBtnClickedListener;
    private removeBtnClicked removeBtnClickedListener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seeds, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SeedGrower seedGrower = seedGrowers.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//set format of date you receiving from db
        Date date = null;
        try {
            date = (Date) sdf.parse(seedGrower.getDateplanted());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("MMMM dd, yyyy");

        holder.tvVariety.setText(seedGrower.getVariety());
        holder.tvDateplanted.setText(newDate.format(date));
        holder.tvSeedClass.setText(seedGrower.getSeedclass()+ " seeds");

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
    public interface  finalizedBtnClicked{
        void finalizeBeforeSending(SeedGrower seedGrower);
    }
    public interface removeBtnClicked{
        void removeSeedGrower(SeedGrower seedGrower);
    }
    public void setSgFormClickedListener(SeedGrowerAdapter.sgFormClicked sgFormClickedListener) {
        this.sgFormClickedListener = sgFormClickedListener;
    }

    public void setSendBtnClickedListener(SeedGrowerAdapter.sendBtnClicked sendBtnClickedListener) {
        this.sendBtnClickedListener = sendBtnClickedListener;
    }
    public void setFinalizedBtnClickedListener(SeedGrowerAdapter.finalizedBtnClicked finalizedBtnClickedListener){
        this.finalizedBtnClickedListener = finalizedBtnClickedListener;
    }
    public void setRemoveBtnClickedListener(SeedGrowerAdapter.removeBtnClicked removeBtnClickedListener){
        this.removeBtnClickedListener = removeBtnClickedListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvVariety, tvDateplanted,tvSeedClass;
        Button sendBtn,editBtn,finalizeBtn,removeBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            tvVariety = (TextView) itemView.findViewById(R.id.tvVariety);
            tvDateplanted = (TextView) itemView.findViewById(R.id.tvDateplanted);
            tvSeedClass = (TextView) itemView.findViewById(R.id.tvSeedClass);
            //sendBtn = (Button) itemView.findViewById(R.id.sendBtn);
            editBtn = (Button) itemView.findViewById(R.id.editBtn);
            finalizeBtn = (Button) itemView.findViewById(R.id.finalizeBtn);
            removeBtn = (Button) itemView.findViewById(R.id.removeBtn);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (sgFormClickedListener != null && position != RecyclerView.NO_POSITION) {
                        sgFormClickedListener.editSGDetails(seedGrowers.get(position));
                    }
                }
            });

            finalizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(finalizedBtnClickedListener != null && position != RecyclerView.NO_POSITION){
                        finalizedBtnClickedListener.finalizeBeforeSending(seedGrowers.get(position));
                    }
                }
            });
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(removeBtnClickedListener != null && position != RecyclerView.NO_POSITION){
                        removeBtnClickedListener.removeSeedGrower(seedGrowers.get(position));
                    }
                }
            });
            /*sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (sendBtnClickedListener != null && position != RecyclerView.NO_POSITION) {
                        sendBtnClickedListener.sendToServer(seedGrowers.get(position));
                    }
                }
            });*/
        }
    }

}
