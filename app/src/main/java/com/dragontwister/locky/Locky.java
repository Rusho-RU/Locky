package com.dragontwister.locky;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

public class Locky extends AppCompatActivity {
    private String save_pattern_key = "pattern_code";
    private String final_pattern = "";
    private PatternLockView mPatternLockView;
    private static final int layoutNum = 5;
    private boolean accessible = false;

    private ArrayList<Integer>layout;
    private ArrayList<Integer>ID;
    Random random = new Random();

    private void addLayout(){
        layout.add(R.layout.background1);
        layout.add(R.layout.background2);
        layout.add(R.layout.background3);
        layout.add(R.layout.background4);
        layout.add(R.layout.background5);
        layout.add(R.layout.background6);

        ID.add(R.id.pattern_lock_view1);
        ID.add(R.id.pattern_lock_view2);
        ID.add(R.id.pattern_lock_view3);
        ID.add(R.id.pattern_lock_view4);
        ID.add(R.id.pattern_lock_view5);
        ID.add(R.id.pattern_lock_view6);
    }

    public Locky(){
        layout = new ArrayList<>();
        ID = new ArrayList<>();
        addLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, LockyService.class));

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        boolean isScreenOn = pm.isScreenOn();

        Paper.init(this);
        final String save_pattern = Paper.book().read(save_pattern_key);

        if (isScreenOn && save_pattern != null && !save_pattern.equals("null")) {
            setContentView(R.layout.activity_reset__password);
            Button button = findViewById(R.id.resetPasswordButton);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Locky.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.general_password_layout);

                    mPatternLockView = findViewById(R.id.pattern_lock_view2);
                    mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                        @Override
                        public void onStarted() {}

                        @Override
                        public void onProgress(List<PatternLockView.Dot> progressPattern) {}

                        @Override
                        public void onComplete(List<PatternLockView.Dot> pattern) {
                            final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);

                            if(final_pattern.equals(save_pattern)){
                                Toast.makeText(Locky.this, "Password Correct", Toast.LENGTH_SHORT).show();
                                setContentView(R.layout.activity_main);
                                mPatternLockView = findViewById(R.id.pattern_lock_view);

                                mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                                    @Override
                                    public void onStarted() {
                                    }

                                    @Override
                                    public void onProgress(List<PatternLockView.Dot> progressPattern) {
                                    }

                                    @Override
                                    public void onComplete(List<PatternLockView.Dot> pattern) {
                                        final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                                    }

                                    @Override
                                    public void onCleared() {
                                    }
                                });

                                Button btnSetup = findViewById(R.id.btnSetPattern);
                                btnSetup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Paper.book().write(save_pattern_key, final_pattern);
                                        Toast.makeText(Locky.this, "Pattern Saved!!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }

                            else
                                Toast.makeText(Locky.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCleared() {}
                    });
                }
            });
        }

        else {
            if (save_pattern != null && !save_pattern.equals("null")) {
                int id = random.nextInt(layoutNum);
                setContentView(layout.get(id));

                mPatternLockView = findViewById(ID.get(id));

                mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                    @Override
                    public void onStarted() {}

                    @Override
                    public void onProgress(List<PatternLockView.Dot> progressPattern) {
//                        HomeWatcher mHomeWatcher = new HomeWatcher(Locky.this);
//                        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
//                            @Override
//                            public void onHomePressed() {
//
//                            }
//                            @Override
//                            public void onHomeLongPressed() {
//                            }
//                        });
//                        mHomeWatcher.startWatch();
                    }

                    @Override
                    public void onComplete(List<PatternLockView.Dot> pattern) {
                        final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);

                        if (final_pattern.equals(save_pattern)) {
                            Toast.makeText(Locky.this, "Password Correct", Toast.LENGTH_SHORT).show();
                            accessible = true;
                            android.os.Process.killProcess(android.os.Process.myPid());
                        } else
                            Toast.makeText(Locky.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCleared() {
                    }
                });
            }

            else {
                accessible = false;
                setContentView(R.layout.activity_set__password);
                Button button = findViewById(R.id.setPassButton);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_main);
                        mPatternLockView = findViewById(R.id.pattern_lock_view);

                        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                            @Override
                            public void onStarted() {
                            }

                            @Override
                            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                            }

                            @Override
                            public void onComplete(List<PatternLockView.Dot> pattern) {
                                final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                            }

                            @Override
                            public void onCleared() {
                            }
                        });

                        Button btnSetup = findViewById(R.id.btnSetPattern);
                        btnSetup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Paper.book().write(save_pattern_key, final_pattern);
                                Toast.makeText(Locky.this, "Pattern Saved!!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });

            }
        }
    }

    @Override
    protected void onUserLeaveHint() {
        Toast.makeText(this, "Please insert the correct pattern", Toast.LENGTH_SHORT).show();

        if (accessible) {
            super.onUserLeaveHint();
        }
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Please insert the correct pattern", Toast.LENGTH_SHORT).show();

        if(accessible){
            super.onBackPressed();
        }
    }
}
