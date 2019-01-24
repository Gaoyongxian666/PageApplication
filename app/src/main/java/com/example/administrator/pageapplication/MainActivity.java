package com.example.administrator.pageapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;


import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private static final android.view.Gravity Gravity = ;
    private int CODE_FOR_WRITE_PERMISSION=1;
    private static ViewPager ultraViewPager;
    private volatile static int Flag=0;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int currentPage = ultraViewPager.getCurrentItem();//设置当前页面
                    int nextPage = currentPage + 1;
                    //给viewpager设置当前的page  为多少
                    if (nextPage == 3) {
                        ultraViewPager.setCurrentItem(0);
                    } else {
                        ultraViewPager.setCurrentItem(nextPage);
                    }
                    break;
                case 2:
                    new Thread() {
                        @Override
                        public void run() {
                            Flag=1;
                            while (Flag==1) {
                                //会一直开启另外的线程
//                    handler.sendEmptyMessageDelayed(1,2000);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.sendEmptyMessage(1);
                            }
                        }
                    }.start();
                    break;
                case 3:
                    ultraViewPager.setCurrentItem(3,true);
                    break;
            }
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ultraViewPager = (ViewPager)findViewById(R.id.ultra_viewpager);
//        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

//        LayoutInflater layoutInflater=getLayoutInflater();
//        @SuppressLint("ResourceType") View view=layoutInflater.inflate(R.layout.d1,null);
//        @SuppressLint("ResourceType") View view2=layoutInflater.inflate(R.layout.d1,null);
//        ArrayList<View> viewlist=new ArrayList<View>();
//        viewlist.add(view);
//        viewlist.add(view2);
        //UltraPagerAdapter 绑定子view到UltraViewPager

//        int [] viewlist=new int [3];
//        viewlist[0]=R.mipmap.d1;
//        viewlist[1]=R.mipmap.d1;
//        viewlist[2]=R.mipmap.d1;
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }



        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            //拥有权限，执行操作
            Log.e("dd","            //拥有权限，执行操作\n");

            Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/dengdai/1.png");
            Resources resources = getResources();
            BitmapDrawable drawable = new BitmapDrawable(resources,
                    bitmap);
            ArrayList<BitmapDrawable> arrayList = new ArrayList<BitmapDrawable>();
            arrayList.add(drawable);
            arrayList.add(drawable);
            arrayList.add(drawable);

            PagerAdapter adapter = new UltraPagerAdapter(arrayList);
            ultraViewPager.setAdapter(adapter);
            Button button = findViewById(R.id.ccc);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Flag = 0;

                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(3);
                        }
                    }.start();
                }
            });
            Button button2 = findViewById(R.id.bbb);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Flag = 1;
                    handler.sendEmptyMessage(2);

                }
            });
            try {
                Field field = ViewPager.class.getDeclaredField("mScroller");
                field.setAccessible(true);
                FixedSpeedScroller scroller = new FixedSpeedScroller(ultraViewPager.getContext(),
                        new AccelerateInterpolator());
                field.set(ultraViewPager, scroller);
                scroller.setmDuration(500);
            } catch (Exception e) {

            }

            handler.sendEmptyMessage(2);
        }else {//请求权限方法
            Log.e("ee","未授予权限不为空");
            String[] mpermissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, mpermissions, CODE_FOR_WRITE_PERMISSION);
        }




//        //内置indicator初始化
//        ultraViewPager.initIndicator();
//        //设置indicator样式
//        ultraViewPager.getIndicator()
//                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
//                .setFocusColor(Color.GREEN)
//                .setNormalColor(Color.WHITE)
//                .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
//        //设置indicator对齐方式
//        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//        //构造indicator,绑定到UltraViewPager
//        ultraViewPager.getIndicator().build();
//
//        //设定页面循环播放
//        ultraViewPager.setInfiniteLoop(true);
//        //设定页面自动切换  间隔2秒
//        ultraViewPager.setAutoScroll(2000);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        if (requestCode == CODE_FOR_WRITE_PERMISSION){
            Log.e("ee","请求结果回调");
            Log.e("ee", String.valueOf(grantResults.length));
            Log.e("ee", String.valueOf(grantResults[0]));

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.e("ee","权限请求成功");


            }else{

//                //用户不同意，向用户展示该权限作用
//
//                }

            }
        }
    }

}
