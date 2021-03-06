package com.cookandroid.everytimecrawler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.everytimecrawler.Room.ServiceControlDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlEntity;

public class loading extends AppCompatActivity {
    private ImageView loadingImage;
    private Animation anim;
    ImageButton exitButton, preButton;
    boolean isPaused = false;
    Intent intent;
    Intent intent2;

    ServiceControlDatabase sdb = ServiceControlDatabase.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        loadingImage = (ImageView) findViewById(R.id.loadingImage);
        exitButton = (ImageButton) findViewById(R.id.exitButton);
        preButton = (ImageButton) findViewById(R.id.preButton);

        initView();

        loadingImage.setOnClickListener(listener);
        preButton.setOnClickListener(listener);
        exitButton.setOnClickListener(listener);
    }

    private void initView(){
        anim = AnimationUtils.loadAnimation(this, R.anim.loding);
        loadingImage.setAnimation(anim);

        new Thread(new Runnable() {
            @Override
            public void run() {
                onoffData("ON");
            }
        }).start();
    }

    private void onoffData(String state) {
        int temp_id = sdb.ServiceControlDao().showId();
        System.out.println("temp_id??? " + temp_id);
        String c = "check";
        ServiceControlEntity SC = new ServiceControlEntity(c, state);
        SC.setId(temp_id);

        String temp_loginId = sdb.ServiceControlDao().showLoginId();
        SC.setLoginId(temp_loginId);

        String temp_loginPw = sdb.ServiceControlDao().showLoginPw();
        SC.setLoginPw(temp_loginPw);

        String temp_cookie_key = sdb.ServiceControlDao().showCookie_key();
        SC.setCookie_key(temp_cookie_key);

        String temp_cookie_value = sdb.ServiceControlDao().showCookie_value();
        SC.setCookie_value(temp_cookie_value);

        String temp_userAgent = sdb.ServiceControlDao().showUserAgent();
        SC.setUserAgent(temp_userAgent);

        sdb.ServiceControlDao().update(SC);
        System.out.println("c??? " + c + ", newc1??? " + state + ", loginId??? " + temp_loginId + ", loginPw??? " + temp_loginPw + ", cookie_key??? " + temp_cookie_key + ", cookie_value??? " + temp_cookie_value + ", userAgent??? " + temp_userAgent);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.loadingImage:
                    if(!isPaused) {
                        loadingImage.clearAnimation();
                        // ON -> OFF

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                android.util.Log.i("run ??????", "Information message");
                                onoffData("OFF");
                            }
                        }).start();
                        Toast.makeText(loading.this, "????????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
//                        stopService(CrawlingService);
                    }
                    else {
                        loadingImage.startAnimation(anim);
                        // OFF -> ON

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                onoffData("ON");
                            }
                        }).start();
                        Toast.makeText(loading.this, "????????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), CrawlingService.class);
                        startService(intent);
                    }
                    isPaused = !isPaused;
                    break;

                case R.id.preButton:
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            String d = sdb.ServiceControlDao().showDes();
//                            Log.e("showDes", d);
////                             ON?????? loading ??????????????? ????????????(
//                            if (d.equals("ON")) {
//                                intent2 = new Intent(getApplicationContext(), SubActivity.class);
//                                startActivity(intent2);
//                            }
//                           finish();
//                        }
//                    }).start();
                    intent2 = new Intent(getApplicationContext(), SubActivity.class);
                    startActivity(intent2);
                    finish();
                    break;

                case R.id.exitButton:
                    exit();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        intent2 = new Intent(getApplicationContext(), SubActivity.class);
        startActivity(intent2);
        finish();
//        super.onBackPressed();
    }

    public void exit() {
        // AlertDialog ????????? ????????? ????????? ???????????? ?????? ?????????
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("????????? ??????????????? ????????? ?????? ????????? ???????????????. \n ????????? ?????? ???????????? ?????????????????? ?????????????????? ??? ??? ???????????? ????????? ?????? ??????????????? ?????????. \n?????????????????????????");

        // "???" ????????? ????????? ???????????? ?????????
        alBuilder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(loading.this, "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                finishAffinity(); // ????????????????????? ????????????.
            }
        });
        // "?????????" ????????? ????????? ???????????? ?????????
        alBuilder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; // ????????? ????????? ?????? ?????? ????????????
            }
        });
        alBuilder.setTitle("????????? ?????? ?????? ??????");
        alBuilder.show(); // AlertDialog.Bulider??? ?????? AlertDialog??? ????????????.
    }

}

