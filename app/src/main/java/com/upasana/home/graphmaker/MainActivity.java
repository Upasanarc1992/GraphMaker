package com.upasana.home.graphmaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout l1, l2, l3, l4, l5, l6;
    Bitmap bitmap;
    public static int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        l1 = (LinearLayout) findViewById(R.id.l1);
        l1.setOnClickListener(this);
        l2 = (LinearLayout) findViewById(R.id.l2);
        l2.setOnClickListener(this);
        l3 = (LinearLayout) findViewById(R.id.l3);
        l3.setOnClickListener(this);
        l4 = (LinearLayout) findViewById(R.id.l4);
        l4.setOnClickListener(this);
        l5 = (LinearLayout) findViewById(R.id.l5);
        l5.setOnClickListener(this);
        l6 = (LinearLayout) findViewById(R.id.l6);
        l6.setOnClickListener(this);


    }

    @Override
    public void onClick(View view)  {
        if (view.equals(l1)) {
            Intent sub = new Intent(MainActivity.this, pie_graph.class);
            type=1;
            Bundle scalebundle = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getLeft(), view.getRight(), view.getWidth(), view.getHeight()).toBundle();
            MainActivity.this.startActivity(sub, scalebundle);
        }
        if (view.equals(l2)) {
            Intent sub = new Intent(MainActivity.this, line_graph.class);
            type=2;
            Bundle scalebundle = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getLeft(), view.getRight(), view.getWidth(), view.getHeight()).toBundle();
            MainActivity.this.startActivity(sub, scalebundle);
        }
        if (view.equals(l3)) {
            Intent sub = new Intent(MainActivity.this, bubble.class);
            type=3;
            Bundle scalebundle = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getLeft(), view.getRight(), view.getWidth(), view.getHeight()).toBundle();
            MainActivity.this.startActivity(sub, scalebundle);
        }
        if (view.equals(l4)) {
            type=4;
            Intent sub = new Intent(MainActivity.this, bar_graph.class);
            Bundle scalebundle = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getLeft(), view.getRight(), view.getWidth(), view.getHeight()).toBundle();
            MainActivity.this.startActivity(sub, scalebundle);
        }
        if (view.equals(l5)) {
            type=5;
            Intent sub = new Intent(MainActivity.this, scatter_graph.class);
            Bundle scalebundle = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getLeft(), view.getRight(), view.getWidth(), view.getHeight()).toBundle();
            MainActivity.this.startActivity(sub, scalebundle);
        }
        if (view.equals(l6)) {
            Intent sub = new Intent(MainActivity.this, donut.class);
            type=6;
            Bundle scalebundle = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getLeft(), view.getRight(), view.getWidth(), view.getHeight()).toBundle();
            MainActivity.this.startActivity(sub, scalebundle);
        }


    }

}
