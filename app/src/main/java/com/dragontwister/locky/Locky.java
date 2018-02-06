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

import java.util.List;

import io.paperdb.Paper;

public class Locky extends AppCompatActivity {
    public String save_pattern_key = "pattern_code";
    public String final_pattern = "";

    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, LockyService.class));

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        Paper.init(this);
        final String save_pattern = Paper.book().read(save_pattern_key);

        if (isScreenOn == true && save_pattern != null && !save_pattern.equals("null")) {
            setContentView(R.layout.test_reset_password);
            Button button = findViewById(R.id.reset_id);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Locky.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.test_general_password_layout);

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
                setContentView(R.layout.pattern_screen);
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

                        if (final_pattern.equals(save_pattern)) {
                            Toast.makeText(Locky.this, "Password Correct", Toast.LENGTH_SHORT).show();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        } else
                            Toast.makeText(Locky.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCleared() {
                    }
                });
            } else {
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
        }
    }
}
