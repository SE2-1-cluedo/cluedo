package at.moritzmusel.cluedo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class DrawView extends AppCompatImageView {

    public DrawView(Context context){
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
