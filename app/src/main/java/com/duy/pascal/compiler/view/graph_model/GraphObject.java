package com.duy.pascal.compiler.view.graph_model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Duy on 02-Mar-17.
 */

public abstract class GraphObject {
    public Paint paint = new Paint();

    public abstract void draw(Canvas canvas);

    public void setPaintColor(int color) {
        this.paint.setColor(color);
    }
}