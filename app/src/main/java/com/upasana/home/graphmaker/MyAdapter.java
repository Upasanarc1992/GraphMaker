package com.upasana.home.graphmaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Myholder> {

    private static String data_names_saved[];
    public int data_values_saved[];
    Cursor cursor;
    Context c;
    databaseHelper db;
     int type;
    int count_saved;
    private LayoutInflater inflater;

    public MyAdapter(databaseHelper db, Context c, int type) {
        this.db=db;
        this.type=type;
        this.cursor = db.get_data(type);
        this.inflater = LayoutInflater.from(c);
        this.c = c;
        count_saved=0;


        // Check if there is any thong to show
        if(cursor.getCount()==0)
            Toast.makeText(c,"No Saved Graphs to display", Toast.LENGTH_SHORT).show();

    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        //cursor.move(position+1);
        cursor.moveToPosition(position);

        holder.tv.setText(cursor.getString(cursor.getColumnIndex("TITLE")).toUpperCase());
        holder.tv1.setText(cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
        holder.tv2.setText(cursor.getString(cursor.getColumnIndex("DATE_MODIFIED")));


    }

    @Override
    public int getItemCount() {
        int i = cursor.getCount();
        //Toast.makeText(c,String.valueOf(cursor.getCount()),Toast.LENGTH_SHORT).show();
        return i;
    }


    class Myholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv;
        private TextView tv1;
        private TextView tv2;
        private CardView root;

        public Myholder(View itemView) {
            super(itemView);


            root = (CardView) itemView.findViewById(R.id.root_container);
            tv = (TextView) itemView.findViewById(R.id.text_title);
            tv1 = (TextView) itemView.findViewById(R.id.text_description);
            tv2 = (TextView) itemView.findViewById(R.id.text_date);
            root.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int position=getAdapterPosition();

            reformat(db.get_values(position,type));


            Intent sub = new Intent((Activity) c, graph_add_saved.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("name", data_names_saved);
            bundle.putString("title", tv.getText().toString());
            bundle.putString("details", tv1.getText().toString());
            bundle.putIntArray("value", data_values_saved);
            bundle.putInt("count", count_saved);
            sub.putExtras(bundle);
            ((Activity) c).finish();

            c.startActivity(sub);
        }


        public void reformat(Cursor r_data){
            data_names_saved=new String[r_data.getCount()];
            data_values_saved=new int [data_names_saved.length];

            count_saved=0;
            r_data.moveToFirst();

            while (!r_data.isAfterLast())
            {
                data_names_saved[count_saved]=r_data.getString(r_data.getColumnIndex("DATA_NAMES"));
                data_values_saved[count_saved]=r_data.getInt(r_data.getColumnIndex("DATA_VALUES"));

                count_saved++;
                r_data.moveToNext();
            }
        }
    }
}
