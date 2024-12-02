package com.example.passwordmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter  extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList _id, title, username, password, url;
    CustomAdapter(Context context, ArrayList _id, ArrayList title, ArrayList username, ArrayList password, ArrayList url){
        this.context = context;
        this._id = _id;
        this.title = title;
        this.username = username;
        this.password = password;
        this.url = url;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder._id_text.setText(String.valueOf(_id.get(position)));
        holder.title_text.setText(String.valueOf(_id.get(position)));
        holder.username_text.setText(String.valueOf(_id.get(position)));

    }

    @Override
    public int getItemCount() {
        return _id.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView _id_text, title_text, username_text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            _id_text = itemView.findViewById(R.id._id_txt);
            title_text = itemView.findViewById(R.id.title_text);
            username_text = itemView.findViewById(R.id.username_text);

        }
    }

}
