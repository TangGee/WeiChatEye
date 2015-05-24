package com.rizhi.vchateye;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by tangtang on 15/5/24.
 */
public class EyeView extends FrameLayout{


    private Paint paint;

    private Bitmap bitmap;

    public EyeView(Context context) {
        super(context);
        init();

    }

    public EyeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public EyeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        setDrawingCacheEnabled(true);
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if(bitmap!=null){

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            paint.setXfermode(null);
        }
    }

    /**
     * 这里画一个bitmap 上边画一个圆
     * @param radius
     */
    protected void drawRaius(int radius)
    {
        if(bitmap!=null)
            bitmap.recycle();
        bitmap=Bitmap.createBitmap(getWidth(),getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
        invalidate();
    }
}
