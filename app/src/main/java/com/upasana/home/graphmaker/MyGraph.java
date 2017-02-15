package com.upasana.home.graphmaker;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.Toast;


class MyGraph {
    Canvas c;
    String[] dataNames;
    int[] dataValues;
    int[] colorValues;
    String graphType;
    String orientation;
    int graphWidth, graphHeight, xShift, yShift;
    int dpi;
    Boolean border;
    String title;
    Float density;
    int size;

    public MyGraph(Resources r, Canvas c, String[] dataNames, int[] dataValues, int[] ColorValue, String graphType, String orientation, String title) {
        this.c = c;
        this.dataNames = dataNames;
        this.dataValues = dataValues;
        this.graphType = graphType;
        this.orientation = orientation;
        colorValues = ColorValue;
        dpi = graph_add.screen_dpi;
        border = true;
        this.title = title;
        density = r.getDisplayMetrics().density;

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        c.drawRect(0,0,c.getWidth(),c.getHeight(),p);

        p.setColor(Color.BLACK);
        p.setTextAlign(Paint.Align.CENTER);
        size = (int) (20 * density);
        p.setTextSize(size);
        c.drawText(title, c.getWidth() / 2, c.getHeight() / 15, p);



        if (graphType.toLowerCase().equals("bar"))
            createBarGraph();

        if (graphType.toLowerCase().equals("pie"))
            createPieGraph();

        if (graphType.toLowerCase().equals("scatter"))
            createScatterGraph();

        if (graphType.toLowerCase().equals("line"))
            createLineGraph();

        if (graphType.toLowerCase().equals("donut"))
            createDonutGraph();

        if (graphType.toLowerCase().equals("misc"))
            createBubbleGraph();

    }

    private void createBarGraph() {

        //Area that the graph would cover
        graphWidth = (int) (c.getWidth() * 0.92);
        graphHeight = (int) (c.getHeight() * 0.75);

        //Shift from (0,0) poition
        xShift = (int) (c.getWidth() * 0.05);
        yShift = (int) (c.getHeight() * 0.12);

        //Get Bar Width
        int barWidth = graphWidth / dataNames.length;

        //Draw Axis
        Paint black = new Paint();
        black.setColor(Color.BLACK);
        int stroke=(int)(density*2);
        black.setStrokeWidth(stroke);

        c.drawLine(xShift - 1, (int) (c.getHeight() * 0.087), xShift - 1, c.getHeight() - yShift, black);     //y axis
        c.drawLine(xShift, c.getHeight() - yShift, c.getWidth() - xShift, c.getHeight() - yShift, black);    //x axis

        //Get Highest Value, Determine Y Scale
        int highest = dataValues[0];
        for (int temp : dataValues)
            if (highest < temp)
                highest = temp;

        float yScale = ((float) graphHeight) / ((float) highest);

        Paint p = new Paint();
        p.setAntiAlias(true);
        //p.setTextSize(40);
        size=(int)(2*size/3);
        p.setTextSize(size);


        for (int i = 0; i < dataValues.length; i++) {

            if (border) {
                p.setColor(Color.BLACK);
                c.drawRect(xShift + i * barWidth, c.getHeight() - yShift - 8 - (int) (yScale * dataValues[i]),
                        xShift + 4 + (int) ((i + 0.7) * (barWidth)), c.getHeight() - yShift - 4, p);
            }
            p.setColor(colorValues[i]);
            c.drawRect(xShift + 4 + i * barWidth, c.getHeight() - yShift - 4 - (int) (yScale * dataValues[i]),
                    xShift + (int) ((i + 0.7) * (barWidth)), c.getHeight() - yShift - 4, p);

            p.setColor(Color.BLACK);
            //DataNames
            c.drawText(dataNames[i], xShift + 8 + i * barWidth, graphHeight + yShift + (int)(density*20), p);

            //Data Values
            c.drawText("" + dataValues[i], xShift +8+ i * barWidth, c.getHeight() - yShift - 15 - (int) (yScale * dataValues[i]), p);
        }
    }


    private void createPieGraph() {


        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setAntiAlias(true);

        graphWidth = (int) (c.getWidth() * 0.8);
        graphHeight = (int) (c.getHeight() * 0.8);

        //Make Circular

        if (graphWidth < graphHeight)
            graphHeight = graphWidth;
        else
            graphWidth = graphHeight;


        xShift = (c.getWidth() - graphWidth) / 2;
        yShift = (c.getHeight() - graphHeight) / 2;

        //Find scale
        float scale = 0f;
        int sum = 0;
        for (int n : dataValues)
            sum += n;
        scale = (360.0f) / (float) sum;

        RectF r = new RectF(xShift, yShift, xShift + graphWidth, yShift + graphHeight);
        RectF r2 = new RectF(xShift - 5, yShift - 5, xShift + graphWidth + 5, yShift + graphHeight + 5);

        float sumOfAngles = 0;
        for (int i = 0; i < dataValues.length; i++) {
            if (border) {
                p.setColor(Color.BLACK);
                c.drawArc(r2, sumOfAngles, (float) (dataValues[i] * scale), true, p);
            }
            p.setColor(colorValues[i]);

            c.drawArc(r, sumOfAngles, (float) (dataValues[i] * scale), true, p);
            sumOfAngles += (float) (dataValues[i] * scale);

        }

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        size=(int)(2*size/3);
        paint.setTextSize(size);

        sumOfAngles = 0.0f;
        for (int i = 0; i < dataValues.length; i++) {

            int radius = graphWidth / 2;
            int xPos = xShift + radius + 5 +
                    (int) (Math.cos(Math.toRadians((sumOfAngles) + (dataValues[i] * scale) / 2)) * radius);

            int yPos = yShift + 5 + radius +
                    (int) (Math.sin(Math.toRadians((sumOfAngles) + (dataValues[i] * scale) / 2)) * radius);

            c.drawText(dataNames[i] + " " + dataValues[i], xPos, yPos, paint);
            sumOfAngles += (float) (dataValues[i] * scale);
        }


    }

    private void createScatterGraph() {
        int max_x;
        int max_y;
        int scale_x;
        int scale_y;
        Paint p = new Paint();
        Paint black = new Paint();
        int x_values[] = new int[dataNames.length];

        for (int i = 0; i < x_values.length; i++) {
            x_values[i] = Integer.parseInt(dataNames[i]);
        }

        max_y = dataValues[0];
        max_x = x_values[0];
        for (int i = 0; i < dataValues.length; i++) {
            if (dataValues[i] > max_y)
                max_y = dataValues[i];
            if (x_values[i] > max_x)
                max_x = x_values[i];
        }
        scale_x = (int) (c.getWidth() * 0.87 / max_x);
        scale_y = (int) (c.getHeight() * 0.75 / max_y);
        int x_pos;
        int y_pos;

        //Shift from (0,0) poition
        xShift = (int) (c.getWidth() * 0.075);
        yShift = (int) (c.getHeight() * 0.12);

        black.setColor(Color.BLACK);
        black.setStrokeWidth((int)(2*density));
        size = (int) (15 * density);

        c.drawLine(xShift - 1, (int) (c.getHeight() * 0.087), xShift - 1, c.getHeight() - yShift, black);     //y axis
        c.drawLine(xShift, c.getHeight() - yShift, (int)(c.getWidth() - xShift/2), c.getHeight() - yShift, black);    //x axis

        for (int i = 0; i < dataValues.length; i++) {
            p.setColor(colorValues[i]);
            x_pos = (int) (x_values[i] * scale_x);
            y_pos = (int) (c.getHeight() - (dataValues[i] * scale_y));

            if (border)

                c.drawCircle(x_pos + xShift, y_pos - yShift, (int)(8*density), black);

            c.drawCircle(x_pos + xShift, y_pos - yShift, (int)(6*density), p);

            p.setTextSize(size);
            p.setColor(Color.BLACK);
            //DataNames
            c.drawText(String.valueOf(x_values[i]), x_pos + xShift, (int) (c.getHeight() * 0.75+ yShift) + (int)(density*20), p);

            //Data Values
            c.drawText(String.valueOf(dataValues[i]), (int)(3*density), y_pos - yShift, p);

        }

    }

    private void createLineGraph() {


        int max_x;
        int max_y;
        int scale_x;
        int scale_y;

        Paint p = new Paint();
        Paint black = new Paint();
        int x_values[] = new int[dataNames.length + 1];
        int y_values[] = new int[dataNames.length + 1];
        int c_values[] = new int[dataNames.length + 1];

        x_values[0] = 0;
        y_values[0] = 0;
        c_values[0] = Color.BLACK;

        for (int i = 0; i < dataNames.length; i++) {
            x_values[i + 1] = Integer.parseInt(dataNames[i]);
            y_values[i + 1] = (dataValues[i]);
            c_values[i + 1] = (colorValues[i]);
        }

        max_y = dataValues[0];
        max_x = x_values[0];
        for (int i = 0; i < y_values.length; i++) {
            if (y_values[i] > max_y)
                max_y = y_values[i];
            if (x_values[i] > max_x)
                max_x = x_values[i];
        }
        scale_x = (int) (c.getWidth() * 0.87 / max_x);
        scale_y = (int) (c.getHeight() * 0.75 / max_y);
        int x_pos;
        int y_pos;

        //Shift from (0,0) poition
        xShift = (int) (c.getWidth() * 0.075);
        yShift = (int) (c.getHeight() * 0.12);

        black.setColor(Color.BLACK);
        black.setStrokeWidth((int)(2*density));
        size = (int) (15 * density);

        c.drawLine(xShift - 1, (int) (c.getHeight() * 0.087), xShift - 1, c.getHeight() - yShift, black);     //y axis
        c.drawLine(xShift, c.getHeight() - yShift, (int)(c.getWidth() - xShift/2), c.getHeight() - yShift, black);    //x axis

        for (int i = 1; i < y_values.length; i++) {
            p.setColor(c_values[i]);
            x_pos = (int) (x_values[i] * scale_x);
            y_pos = (int) (c.getHeight() - (y_values[i] * scale_y));
            c.drawCircle(x_pos + xShift, y_pos - yShift, (int)(6*density), p);
            p.setStrokeWidth((int)(2*density));
            c.drawLine(x_pos + xShift, y_pos - yShift, (int) (x_values[i - 1] * scale_x + xShift), (int) (c.getHeight() - (y_values[i - 1] * scale_y) - yShift), p);

            p.setColor(Color.BLACK);

            p.setTextSize(size);
            //DataNames
            c.drawText(String.valueOf(x_values[i]), x_pos + xShift, (int) (c.getHeight() * 0.75+ yShift) + (int)(density*20), p);

            //Data Values
            c.drawText(String.valueOf(y_values[i]), (int)(3*density), y_pos - yShift, p);
        }

    }

    private void createDonutGraph() {


        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setAntiAlias(true);

        graphWidth = (int) (c.getWidth() * 0.8);
        graphHeight = (int) (c.getHeight() * 0.8);

        //Make Circular

        if (graphWidth < graphHeight)
            graphHeight = graphWidth;
        else
            graphWidth = graphHeight;


        xShift = (c.getWidth() - graphWidth) / 2;
        yShift = (c.getHeight() - graphHeight) / 2;

        //Find scale
        float scale = 0f;
        int sum = 0;
        for (int n : dataValues)
            sum += n;
        scale = (360.0f) / (float) sum;
        int black_border=(int)(1.5*density);

        RectF r = new RectF(xShift, yShift, xShift + graphWidth, yShift + graphHeight);
        RectF r2 = new RectF(xShift - black_border, yShift - black_border, xShift + graphWidth + black_border, yShift + graphHeight + black_border);

        float sumOfAngles = 0;
        for (int i = 0; i < dataValues.length; i++) {
            if (border) {
                p.setColor(Color.BLACK);
                c.drawArc(r2, sumOfAngles, (float) (dataValues[i] * scale), true, p);
            }
            p.setColor(colorValues[i]);

            c.drawArc(r, sumOfAngles, (float) (dataValues[i] * scale), true, p);

            sumOfAngles += (float) (dataValues[i] * scale);

        }

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        c.drawCircle((int)(xShift+graphWidth/2),(int)(yShift+graphHeight/2),(int)(graphWidth/4),paint);

        paint.setColor(Color.WHITE);
        c.drawCircle((int)(xShift+graphWidth/2),(int)(yShift+graphHeight/2),(int)((graphWidth/4)-density*1.5),paint);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        size=(int)(2*size/3);
        paint.setTextSize(size);

        sumOfAngles = 0.0f;
        for (int i = 0; i < dataValues.length; i++) {

            int radius = graphWidth / 2;
            int xPos = xShift + radius + 5 +
                    (int) (Math.cos(Math.toRadians((sumOfAngles) + (dataValues[i] * scale) / 2)) * radius);

            int yPos = yShift + 5 + radius +
                    (int) (Math.sin(Math.toRadians((sumOfAngles) + (dataValues[i] * scale) / 2)) * radius);

            c.drawText(dataNames[i] + " " + dataValues[i], xPos, yPos, paint);
            sumOfAngles += (float) (dataValues[i] * scale);
        }


    }

    private void createBubbleGraph() {

        int max_x;
        int max_y;
        int scale_x;
        int scale_y;
        int bubble_size;
        Paint p = new Paint();
        Paint black = new Paint();
        int x_values[] = new int[dataNames.length];

        for (int i = 0; i < x_values.length; i++) {
            x_values[i] = Integer.parseInt(dataNames[i]);
        }

        max_y = dataValues[0];
        max_x = x_values[0];
        for (int i = 0; i < dataValues.length; i++) {
            if (dataValues[i] > max_y)
                max_y = dataValues[i];
            if (x_values[i] > max_x)
                max_x = x_values[i];
        }
        scale_x = (int) (c.getWidth() * 0.87 / max_x);
        scale_y = (int) (c.getHeight() * 0.75 / max_y);
        bubble_size=(int)((c.getWidth() * 0.12) / max_x);
        int x_pos;
        int y_pos;

        //Shift from (0,0) poition
        xShift = (int) (c.getWidth() * 0.075);
        yShift = (int) (c.getHeight() * 0.12);

        black.setColor(Color.BLACK);
        black.setStrokeWidth((int)(2*density));
        size = (int) (15 * density);

        c.drawLine(xShift - 1, (int) (c.getHeight() * 0.087), xShift - 1, c.getHeight() - yShift, black);     //y axis
        c.drawLine(xShift, c.getHeight() - yShift, (int)(c.getWidth() - xShift/2), c.getHeight() - yShift, black);    //x axis

        for (int i = 0; i < dataValues.length; i++) {
            p.setColor(colorValues[i]);
            x_pos = (int) (x_values[i] * scale_x);
            y_pos = (int) (c.getHeight() - (dataValues[i] * scale_y));

            //if (border)

                //c.drawCircle(x_pos + xShift, y_pos - yShift, (int)(8*density), black);

            c.drawCircle(x_pos + xShift, y_pos - yShift, (int)(dataValues[i]*bubble_size), p);

            p.setTextSize(size);
            p.setColor(Color.BLACK);
            //DataNames
            c.drawText(String.valueOf(x_values[i]), x_pos + xShift, (int) (c.getHeight() * 0.75+ yShift) + (int)(density*20), p);

            //Data Values
            c.drawText(String.valueOf(dataValues[i]), (int)(3*density), y_pos - yShift, p);

        }




    }
}