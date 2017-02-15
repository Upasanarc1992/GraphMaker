package com.upasana.home.graphmaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;

public class graph_add extends AppCompatActivity implements View.OnClickListener {

    public static int screen_width, screen_height, screen_dpi;
    Button done, addButton, removeButton;
    LinearLayout container;
    ArrayList<EditText> items, values;
    ArrayList<image_color> color_item;

    public static String data_items[];
    public static int data_values[];
    public static int color_value[];
    int type;
    public static String title;
    public static String details;

    int color;
    databaseHelper db;
    EditText graph_id, graph_details;
    TextView dn,dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        addButton = (Button) findViewById(R.id.addButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        done = (Button) findViewById(R.id.done);
        container = (LinearLayout) findViewById(R.id.container);
        dn=(TextView)findViewById(R.id.dn);
        dv=(TextView)findViewById(R.id.dv);

        graph_id=(EditText)findViewById(R.id.graph_id);
        graph_details=(EditText)findViewById(R.id.graph_details);




        this.type=MainActivity.type;
        items = new ArrayList<EditText>();
        values = new ArrayList<EditText>();
        color_item = new ArrayList<image_color>();

        if(type==2 || type==4)
        {
            dn.setText("X_Values");
            dv.setText("Y_Values");
        }

        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        done.setOnClickListener(this);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        screen_width = displayMetrics.widthPixels;
        screen_height = displayMetrics.heightPixels;
        screen_dpi = displayMetrics.densityDpi;

        container.addView(ColoredView(this,"",""));


    }

    public LinearLayout ColoredView(final Context context, String name, String val) {

        LinearLayout myLayout = new LinearLayout(context);
        LinearLayout l1 = new LinearLayout(context);
        LinearLayout l2 = new LinearLayout(context);
        final LinearLayout l3 = new LinearLayout(context);


        LinearLayout.LayoutParams mCompressedParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, screen_height / 15);


        LinearLayout.LayoutParams new1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        new1.weight = 1;

        LinearLayout.LayoutParams new2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        new2.weight = 1;

        LinearLayout.LayoutParams new3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        new3.weight = 1;
        new3.setMargins(50,20,50,20);


        int red = (int) (Math.random() * 128 + 127);
        int green = (int) (Math.random() * 128 + 127);
        int blue = (int) (Math.random() * 128 + 127);
        color = 0xff << 24 | (red << 16) |
                (green << 8) | blue;


        myLayout.setOrientation(LinearLayout.HORIZONTAL);

        l1.setOrientation(LinearLayout.VERTICAL);
        l1.setLayoutParams(new1);
        l2.setOrientation(LinearLayout.VERTICAL);
        l2.setLayoutParams(new2);
        l3.setOrientation(LinearLayout.VERTICAL);
        l3.setLayoutParams(new3);


        EditText edit = new EditText(context);
        EditText edit2 = new EditText(context);
        final image_color icol = new image_color(context);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        if(type==5 || type==2 || type==3)
            edit2.setInputType(InputType.TYPE_CLASS_NUMBER);

        icol.set_background(color);
        l3.setBackgroundColor(color);

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Choose color")
                        .initialColor(-29440)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(9)

                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                l3.setBackgroundColor(selectedColor);
                                icol.set_background(selectedColor);

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        edit.setText(name);
        edit2.setText(val);
        l1.addView(edit);
        l2.addView(edit2);
        l3.addView(icol.l1);


        myLayout.addView(l2);
        myLayout.addView(l1);
        myLayout.addView(l3);


        myLayout.setLayoutParams(mCompressedParams);


        items.add(edit2);
        values.add(edit);
        color_item.add(icol);

        return myLayout;


    }

    @Override
    public void onClick(View view) {
        if (view.equals(addButton))
            container.addView(ColoredView(graph_add.this,"",""), 0);
        if (view.equals(removeButton)) {
            if (container.getChildCount() > 0) {
                // Removing a view will cause a LayoutTransition animation
                container.removeViewAt(Math.min(1, container.getChildCount() - 1));
                if (items.size() > 1) {
                    items.remove(items.size() - 2);
                    values.remove(values.size() - 2);
                    color_item.remove(color_item.size() - 2);
                } else {
                    items.remove(items.size() - 1);
                    values.remove(values.size() - 1);
                    color_item.remove(color_item.size() - 1);
                }
            } else
                Toast.makeText(this, "Nothing to Remove", Toast.LENGTH_SHORT).show();
        }
        if (view.equals(done)) {
            data_items  = new String[items.size()];
            data_values = new int[values.size()];
            color_value = new int[color_item.size()];

            if(items.size()==0)
                Toast.makeText(this,"No data to plot", Toast.LENGTH_SHORT).show();
            else {

                for (int i = 0; i < items.size(); i++) {
                    if (values.get(i).getText().toString().trim().length() == 0) {
                        data_items[i] = "";
                        data_values[i] = 0;
                    } else {
                        data_items[i] = items.get(i).getText().toString();
                        data_values[i] = Integer.parseInt(values.get(i).getText().toString());
                        color_value[i] = color_item.get(i).get_background();
                    }
                }

                title = graph_id.getText().toString();
                details = graph_details.getText().toString();

                Intent s = new Intent(graph_add.this, graph_draw.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Saved", 0);
                s.putExtras(bundle);
                graph_add.this.startActivity(s);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (type==1) {
            Intent sub = new Intent(graph_add.this, pie_graph.class);
            graph_add.this.startActivity(sub);
        }
        if (type==2) {
            Intent sub = new Intent(graph_add.this, line_graph.class);
            graph_add.this.startActivity(sub);
        }
        if (type==3) {
            Intent sub = new Intent(graph_add.this, bubble.class);
            graph_add.this.startActivity(sub);
        }
        if (type==4) {
            Intent sub = new Intent(graph_add.this, bar_graph.class);
            graph_add.this.startActivity(sub);
        }
        if (type==5) {
            Intent sub = new Intent(graph_add.this, scatter_graph.class);
            graph_add.this.startActivity(sub);
        }
        if (type==6) {
            Intent sub = new Intent(graph_add.this, scatter_graph.class);
            graph_add.this.startActivity(sub);
        }
        finish();
    }



}
