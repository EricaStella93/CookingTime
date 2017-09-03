package com.example.erica.cookingtime.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrSet){
        super(context, attrSet);
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Exo2-SemiBold.otf"));
    }
}
