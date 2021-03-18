package govph.rsis.growapp.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import govph.rsis.growapp.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> users = new ArrayList<>();
    private userLogin userLoginClickedListener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_users, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvNameHolder.setText(user.getFullname());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users){
        this.users = users;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameHolder;

        public ViewHolder(View view){
            super(view);

            tvNameHolder = (TextView) view.findViewById(R.id.tvNameHolder);
            tvNameHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(userLoginClickedListener != null && position != RecyclerView.NO_POSITION){
                        userLoginClickedListener.userLogin(users.get(position));
                    }
                }
            });
        }
    }

    public interface userLogin{
        void userLogin(User user);
    }

    public void setUserLoginClickedListener(UserAdapter.userLogin userLoginClickedListener){
        this.userLoginClickedListener = userLoginClickedListener;
    }
}
