package com.example.location_basedvotingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList pollTitle;
    ArrayList pollOwner;

    Context context;

    // Constructor for initialization
    public Adapter(Context context, ArrayList pollTitle, ArrayList pollOwner) {
        this.context = context;
        this.pollTitle = pollTitle;
        this.pollOwner = pollOwner;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_card, parent, false);

        // Passing view to ViewHolder
        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.title.setText((String) pollTitle.get(position));
        holder.owner.setText((String) pollOwner.get(position));
    }

    @Override
    public int getItemCount() {
        // Returns number of items
        // currently available in Adapter
        return pollTitle.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView owner;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.pollTitle);
            owner = (TextView) view.findViewById(R.id.pollOwner);

        }
    }
}

