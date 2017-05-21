package com.firozmemon.wallet.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firozmemon.wallet.R;
import com.firozmemon.wallet.models.User_Credentials;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firoz on 20/5/17.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MyHolder> {

    private final Context context;
    private final List<User_Credentials> list;
    private final LayoutInflater inflater;
    AdapterItemClickListener itemClickListener;

    public MainActivityAdapter(Context context, List<User_Credentials> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_main, parent, false);
        MyHolder viewHolder = new MyHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String site_name = list.get(position).getSite_name();
        if (site_name == null) {
            site_name = "";
        }

        String user_name = list.get(position).getUser_name();
        if (user_name == null || user_name.equalsIgnoreCase("")) {
            user_name = list.get(position).getEmail();
            // if user_name is still null or blank
            if (user_name == null || user_name.equalsIgnoreCase("")) {
                user_name = "";
            }
        }
        holder.siteName.setText(site_name);
        holder.username.setText(user_name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // convenience method for getting data at click position
    public User_Credentials getItem(int id) {
        return list.get(id);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.siteName)
        TextView siteName;
        @BindView(R.id.username)
        TextView username;

        public MyHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // If activity is subscribed to adapter Click,
                    // notify activity about it
                    if (itemClickListener != null) {
                        itemClickListener.onAdapterItemClick(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }

    // allows clicks events to be caught
    public void setAdapterItemClickListener(AdapterItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public interface AdapterItemClickListener {
        void onAdapterItemClick(View view, int position);
    }
}
