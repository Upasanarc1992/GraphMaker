package com.upasana.home.graphmaker;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class graph_draw extends Activity {

    String data_items[];
    int data_values[];
    int color_vaue[];
    int type;
    int choice;
    String title, details, title_o, detail_o;
    Bitmap save, delete, share;
    int sizex, sizey;
    databaseHelper db;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getIntent().getExtras();
        choice = b.getInt("Saved");

        if (choice == 0) {
            this.data_items = graph_add.data_items;
            this.data_values = graph_add.data_values;
            this.color_vaue = graph_add.color_value;
            this.title = graph_add.title;
            this.details = graph_add.details;
        }
        if (choice == 1) {
            this.data_items = graph_add_saved.data_items;
            this.data_values = graph_add_saved.data_values;
            this.color_vaue = graph_add_saved.color_value;
            this.title = graph_add_saved.title;
            this.title_o = graph_add_saved.title_o;
            this.detail_o = graph_add_saved.detail_o;
            this.details = graph_add_saved.details;

        }
        this.type = MainActivity.type;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MyView mv = new MyView(this);
        setContentView(mv);

        save = BitmapFactory.decodeResource(getResources(), R.drawable.save_button);
        delete = BitmapFactory.decodeResource(getResources(), R.drawable.delete_button);
        share = BitmapFactory.decodeResource(getResources(), R.drawable.share_button);


    }

    public void save_data() {
        db = new databaseHelper(this);
        db.insert_data(type, title.toLowerCase(), details.toLowerCase(), "20.01.2017", data_items, data_values);
        Toast.makeText(this, "Saved Sccessfully", Toast.LENGTH_SHORT).show();
    }

    public void update() {
        db = new databaseHelper(this);
        db.del(type, title_o, detail_o, "20.01.2017");
        db.insert_data(type, title.toLowerCase(), details.toLowerCase(), "20.01.2017", data_items, data_values);
        Toast.makeText(this, "Updated Sccessfully", Toast.LENGTH_SHORT).show();
    }

    public void del() {
        db = new databaseHelper(this);
        db.del(type, title_o, detail_o, "20.01.2017");
        Toast.makeText(this, "Deleted Sccessfully", Toast.LENGTH_SHORT).show();
        return_back(type);

    }

    public void share(Bitmap icon){


        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(graph_draw.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(graph_draw.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(graph_draw.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_graph.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
    } catch (IOException e) {
        e.printStackTrace();
    }
    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_graph.jpg"));
    startActivity(Intent.createChooser(share, "Share Image"));


}

class MyView extends View {

        public MyView(Context c) {
            super(c);
        }

        @Override
        public void onDraw(Canvas c) {
            sizex = c.getWidth();
            sizey = c.getHeight();

            save = Bitmap.createScaledBitmap(save, (int) (3 * sizex / 11), (int) (sizey / 15), true);
            delete = Bitmap.createScaledBitmap(delete, (int) (3 * sizex / 11), (int) (sizey / 15), true);
            share = Bitmap.createScaledBitmap(share, (int) (3 * sizex / 11), (int) (sizey / 15), true);


            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            bmp = Bitmap.createBitmap(sizex, (int)(14*sizey/15), conf); // this creates a MUTABLE bitmap
            Canvas canvas = new Canvas(bmp);


            
            if (choice == 1) {
                c.drawBitmap(save, 0, 14 * c.getHeight() / 15, null);
                c.drawBitmap(share, (int) (4 * c.getWidth() / 11), 14 * c.getHeight() / 15, null);
                c.drawBitmap(delete, (int) (8 * c.getWidth() / 11), 14 * c.getHeight() / 15, null);
            }

            if (choice == 0) {
                c.drawBitmap(save, (int) (2 * c.getWidth() / 11), 14 * c.getHeight() / 15, null);
                c.drawBitmap(share, (int) (6 * c.getWidth() / 11), 14 * c.getHeight() / 15, null);
            }


            if (type == 1) {
                MyGraph mg = new MyGraph(getResources(), canvas, data_items, data_values, color_vaue, "PIE", "VERTICAL", title);
            }
            if (type == 2) {
                MyGraph mg = new MyGraph(getResources(), canvas, data_items, data_values, color_vaue, "LINE", "VERTICAL", title);
            }
            if (type == 4) {
                MyGraph mg = new MyGraph(getResources(), canvas, data_items, data_values, color_vaue, "BAR", "VERTICAL", title);
            }
            if (type == 5) {
                MyGraph mg = new MyGraph(getResources(), canvas, data_items, data_values, color_vaue, "SCATTER", "VERTICAL", title);
            }
            if (type == 6) {
                MyGraph mg = new MyGraph(getResources(), canvas, data_items, data_values, color_vaue, "DONUT", "VERTICAL", title);
            }

            if (type == 3) {
                MyGraph mg = new MyGraph(getResources(), canvas, data_items, data_values, color_vaue, "MISC", "VERTICAL", title);
            }

            c.drawBitmap(bmp,0,0,null);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            int pressX = (int) event.getX();
            int pressY = (int) event.getY();

            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {

                if (choice == 1) {
                    if (pressX > 0 &&
                            pressX < 3 * sizex / 11  &&
                            pressY > 14 * sizey / 15 &&
                            pressY < 14 * sizey / 15 + save.getHeight()
                            ) {
                        update();
                    }

                    if (pressX > 5 * sizex / 11 &&
                            pressX < 8 * sizex / 11  &&
                            pressY > 14 * sizey / 15 &&
                            pressY < 14 * sizey / 15 + delete.getHeight()
                            ) {
                        share(bmp);
                    }

                    if (pressX > 9 * sizex / 11 &&
                            pressX < sizex  &&
                            pressY > 14 * sizey / 15 &&
                            pressY < 14 * sizey / 15 + delete.getHeight()
                            ) {
                        del();
                    }
                }
                if (choice == 0) {
                    if (pressX > 2 * sizex / 11 &&
                            pressX < 5 * sizex / 11  &&
                            pressY > 14 * sizey / 15 &&
                            pressY < 14 * sizey / 15 + save.getHeight()
                            ) {
                        save_data();
                    }

                    if (pressX > 6 * sizex / 11 &&
                            pressX < 9 * sizex / 11  &&
                            pressY > 14 * sizey / 15 &&
                            pressY < 14 * sizey / 15 + delete.getHeight()
                            ) {
                        share(bmp);
                    }
                }
            }
            return false;
        }
    }
    public void return_back(int type)
    {
        if (type==1) {
            Intent sub = new Intent(graph_draw.this, pie_graph.class);
            graph_draw.this.startActivity(sub);
        }
        if (type==2) {
            Intent sub = new Intent(graph_draw.this, line_graph.class);
            graph_draw.this.startActivity(sub);
        }
        if (type==3) {
            Intent sub = new Intent(graph_draw.this, bubble.class);
            graph_draw.this.startActivity(sub);
        }
        if (type==4) {
            Intent sub = new Intent(graph_draw.this, bar_graph.class);
            graph_draw.this.startActivity(sub);
        }
        if (type==5) {
            Intent sub = new Intent(graph_draw.this, scatter_graph.class);
            graph_draw.this.startActivity(sub);
        }
        if (type==6) {
            Intent sub = new Intent(graph_draw.this, scatter_graph.class);
            graph_draw.this.startActivity(sub);
        }
        finish();
    }
}