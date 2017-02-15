package com.upasana.home.graphmaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class line_graph extends AppCompatActivity {

    databaseHelper db;
    int type = MainActivity.type;
    private RecyclerView recView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_chart);

        RelativeLayout l=(RelativeLayout) findViewById(R.id.ll);
        l.setBackgroundColor(getResources().getColor(R.color.color2_sub));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color2));
        setSupportActionBar(toolbar);

        recView=(RecyclerView)findViewById(R.id.rec_list);
        recView.setLayoutManager(new StaggeredGridLayoutManager(2,1));

        db = new databaseHelper(this);
        adapter=new MyAdapter(db,this,type);

        recView.setAdapter(adapter);

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent sub = new Intent(line_graph.this, graph_add.class);
        line_graph.this.startActivity(sub);
        finish();
        return false;
    }


}
