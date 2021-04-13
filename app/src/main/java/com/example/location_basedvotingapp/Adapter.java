package com.example.location_basedvotingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.location_basedvotingapp.DBHelper.POLL_LAG;
import static com.example.location_basedvotingapp.DBHelper.POLL_LAT;
import static com.example.location_basedvotingapp.DBHelper.POLL_TITLE;
import static com.example.location_basedvotingapp.DBHelper.TABLE_POLL;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList pollTitle;
    ArrayList pollOwner;
    ArrayList <String>locations = new ArrayList<String>();
    DBHelper db;
    Context context;


    // Constructor for initialization
    public Adapter(Context context, ArrayList pollTitle, ArrayList pollOwner) throws IOException {
        this.context = context;
        this.pollTitle = pollTitle;
        this.pollOwner = pollOwner;
        db = new DBHelper(context);
        for (int i=0 ; i< getItemCount(); i++)
            locations.add(retrieveAddressLine(pollTitle.get(i).toString()));

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
        holder.owner.setText((String) pollOwner.get(position).toString().toUpperCase());
        holder.location.setText((String) locations.get(position));

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
        TextView location;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.pollTitle);
            owner = (TextView) view.findViewById(R.id.pollOwner);
            location  = (TextView) view.findViewById(R.id.Location);

        }
    }

    public String retrieveAddressLine(String poll ) throws IOException {

        //get poll coordinates

        SQLiteDatabase db_ = db.getWritableDatabase();
        Cursor res =  db_.rawQuery("  SELECT LAT , LAG  FROM "+TABLE_POLL+" WHERE "+POLL_TITLE+" = '"+poll+"'",null);
        res.moveToFirst();


        double lat_ = res.getDouble(res.getColumnIndex(POLL_LAT));
        double lag_ = res.getDouble(res.getColumnIndex(POLL_LAG));
        String addressStr = "";
        Geocoder myLocation = new Geocoder(context, Locale.getDefault());
        List<Address> myList = myLocation.getFromLocation(lat_/1000000,lag_, 1);
      try {
          Address address = (Address) myList.get(0);

          addressStr += address.getSubLocality();
      }catch (IndexOutOfBoundsException e){
          System.out.println(e);
      }

        return addressStr;
    }
}

