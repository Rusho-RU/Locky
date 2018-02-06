package com.dragontwister.locky;

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

public class TestActivity extends AppCompatActivity {
    public String save_pattern_key = "pattern_code";
    public String final_pattern = "";

    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);
        final String save_pattern = Paper.book().read(save_pattern_key);


        if (save_pattern != null && !save_pattern.equals("null")) {
            setContentView(R.layout.background2);
            mPatternLockView = findViewById(R.id.pattern_lock_view2);
            mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {}

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {}

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);

                    if (final_pattern.equals(save_pattern)) {
                        Toast.makeText(TestActivity.this, "Password Correct", Toast.LENGTH_SHORT).show();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                    else
                        Toast.makeText(TestActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();

                    @Override
                    public void onCleared() {}
                });
            }
        }

            else {
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
