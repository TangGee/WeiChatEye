package com.rizhi.vchateye;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity implements SurfaceHolder.Callback{

    private ListView lv;

    private boolean canShowEye=true;

    private LinearLayout ll_main;

    private float mainY;

    private boolean eyeisShow=false;

    private int screenHeight;

    private SurfaceView surfaceView;

    private  int time=500;

    private ValueAnimator animator;

    private EyeView eye;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenHeight=getResources().getDisplayMetrics().heightPixels;

        lv= (ListView) findViewById(R.id.lv);


        ll_main= (LinearLayout) findViewById(R.id.ll_main);

        surfaceView= (SurfaceView) findViewById(R.id.sv);



        surfaceView.getHolder().addCallback(this);


        mainY=ll_main.getY();


        animator=ValueAnimator.ofFloat(0,time);
        animator.setDuration(time);


        eye= (EyeView) findViewById(R.id.eye);

        Log.i("AAAA",mainY+"  asdasdasd asd asdasd"+ll_main.getTop());


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem==0)
                    canShowEye=true;
                else
                    canShowEye=false;

            }
        });

        lv.setAdapter(new MyAdapter());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"nihao zhouxingchi ",Toast.LENGTH_SHORT).show();
            }
        });

        lv.setOnTouchListener(new View.OnTouchListener() {

            float downY=0;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**
                 * 滑动
                 */

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            downY=event.getRawY();

                            return true;
                        case MotionEvent.ACTION_MOVE:

                            float dy = event.getRawY() - downY;
                            if(canShowEye&&!eyeisShow){
                                if(dy>0)
                                    eyeisShow=true;
                            }

                            if (canShowEye&&eyeisShow) {

                                ll_main.setY(mainY + dy);

                                eye.drawRaius((int) ((int) (dy-100)*0.6));

                                Log.i("AAAA","move: "+ll_main.getY());
                                return true;
                            }


                            return false;


                        case MotionEvent.ACTION_UP:


                            if(eyeisShow){
                                downY=0;


                                Log.i("AAAA",ll_main.getY()+"------");

                                if(ll_main.getY()>300)
                                {
                                    showEye(ll_main.getY());

                                }else{
                                    showMain();
                                }
                            }

                            eyeisShow=false;

                            break;


                }

                return false;
            }


        });


    }

    private void showEye(float y) {

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = Float.parseFloat(animation.getAnimatedValue().toString());

                ll_main.setY(ll_main.getY() + (screenHeight-ll_main.getY() ) / time * value);


                if(ll_main.getY()>=screenHeight)
                {
                    surfaceView.setVisibility(View.VISIBLE);
                    surfaceView.getHolder().addCallback(MainActivity.this);
                }

            }
        });

        animator.start();



    }

    @Override
    public void onBackPressed() {
        if(surfaceView.getVisibility()==View.VISIBLE)
        {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = Float.parseFloat(animation.getAnimatedValue().toString());

                    ll_main.setY(ll_main.getY() - (ll_main.getY() - mainY) / time * value);


                    if(ll_main.getY()<=0)
                    {
                       surfaceView.setVisibility(View.GONE);

                    }

                }
            });

            animator.start();

        }else{
            super.onBackPressed();

        }
    }

    private void showMain() {

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = Float.parseFloat(animation.getAnimatedValue().toString());

                ll_main.setY(ll_main.getY() - (ll_main.getY() - mainY) / time * value);

                Log.i("AAAA", ll_main.getY() + "");
            }
        });

        animator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Camera camera;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.i("AAAA","surfacecreate");

        camera =Camera.open();
        try {
            camera.setPreviewDisplay(surfaceView.getHolder());
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Log.i("AAAA","chanager");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.release();

    }


    private class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return 40;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textView=new TextView(MainActivity.this);

            textView.setText(position+"xxxx");

            textView.setTextColor(Color.BLACK);

            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER);

            return textView;
        }
    }
}
