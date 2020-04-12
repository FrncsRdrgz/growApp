package com.example.growapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SeedGrowerAdapter extends RecyclerView.Adapter<SeedGrowerAdapter.ViewHolder> {
    private static final String TAG = "SeedGrowerAdapter";
    private Context c;
    private ArrayList<SeedGrower> mSeedGrower;
    private SGClicked sgClickedListener;
    private SGEditClicked sgEditClickedListener;
    public interface SGClicked {
        void sendToServer(String sgId);
    }

    public interface SGEditClicked{
        void editSGDetails(String sgId);
    }

    public void setSgEditClickedListener(SeedGrowerAdapter.SGEditClicked sgEditClickedListener){
        this.sgEditClickedListener = sgEditClickedListener;
    }

    public void setSgClickedListener(SeedGrowerAdapter.SGClicked sgClickedListener) {
        this.sgClickedListener = sgClickedListener;
    }

    public SeedGrowerAdapter(Context c, ArrayList<SeedGrower> mSeedGrower){
        this.c = c;
        this.mSeedGrower = mSeedGrower;

        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView tvVariety,tvDateplanted;
        Button sendBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.lineSeeds);
            tvVariety = (TextView) itemView.findViewById(R.id.tvVariety);
            tvDateplanted = (TextView) itemView.findViewById(R.id.tvDateplanted);
            sendBtn = (Button) itemView.findViewById(R.id.sendBtn);
        }
    }
    @NonNull
    @Override
    public SeedGrowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_seeds, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SeedGrowerAdapter.ViewHolder holder,final int position) {
        SeedGrower seedGrower = mSeedGrower.get(position);

        LinearLayout linearLayout = holder.linearLayout;
        TextView textView1 = holder.tvVariety;
        textView1.setText(seedGrower.getVariety());
        TextView textView2 = holder.tvDateplanted;
        Button button = holder.sendBtn;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");//set format of date you receiving from db
        Date date = null;
        try {
            date = (Date) sdf.parse(seedGrower.getDateplanted());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("MMMM dd, yyyy");//set format of new date
        textView2.setText(newDate.format(date));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sgClickedListener != null) {
                    sgClickedListener.sendToServer(Integer.toString(mSeedGrower.get(position).getSgId()));
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sgEditClickedListener != null) {
                    sgEditClickedListener.editSGDetails(Integer.toString(mSeedGrower.get(position).getSgId()));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mSeedGrower.size();
    }
}
