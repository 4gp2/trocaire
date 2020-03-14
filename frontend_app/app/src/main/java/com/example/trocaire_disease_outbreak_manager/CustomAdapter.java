package com.example.trocaire_disease_outbreak_manager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> firstNames;
    ArrayList<String> lastNames;
    ArrayList<String> villages;
    ArrayList<String> DOBs;
    ArrayList<String> gender;

    Context context;

    public CustomAdapter(Context context, ArrayList<String> firstNames, ArrayList<String> lastNames, ArrayList<String> villages, ArrayList<String> DOBs, ArrayList<String> gender) {
        this.context = context;
        this.firstNames = firstNames;
        this.lastNames = lastNames;
        this.villages = villages;
        this.DOBs = DOBs;
        this.gender = gender;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.firstName.setText(firstNames.get(position));
        holder.lastName.setText(lastNames.get(position));
        holder.village.setText(villages.get(position));
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, firstNames.get(position), Toast.LENGTH_SHORT).show();

                Intent con = new Intent(context, Patient_details.class);
                con.putExtra("first", firstNames.get(position));
                con.putExtra("last", lastNames.get(position));
                con.putExtra("village", villages.get(position));
                con.putExtra("DOB", DOBs.get(position));
                con.putExtra("gender", gender.get(position));
                view.getContext().startActivity(con);
            }
        });

    }


    @Override
    public int getItemCount() {
        return firstNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, village;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            firstName = (TextView) itemView.findViewById(R.id.firstname);
            lastName = (TextView) itemView.findViewById(R.id.lastname);
            village = (TextView) itemView.findViewById(R.id.village);

        }
    }
}