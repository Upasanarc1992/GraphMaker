package com.upasana.home.graphmaker;


import android.content.Context;
import android.widget.LinearLayout;

public class image_color {
    LinearLayout l1;
    int color;

    public image_color(Context c) {
        l1=new LinearLayout(c);
        color=0;
    }

    public void set_background(int color)
    {
        l1.setBackgroundColor(color);
        this.color=color;
    }
    public int get_background()
    {
        return color;
    }


}
