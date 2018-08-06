package com.efortunetech.lau.demo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.efortunetech.lau.demo.R;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    //    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            showVideo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private ImageView playVideo, shrink;
    private SeekBar seekBar;
    private TextView videoTime;
    private MediaPlayer player;
    private boolean flag = true;
    private SurfaceView showVideo;
    private int currentPst;
    private ProgressBar progressBar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
//        mContentView = findViewById(R.id.fullscreen_content);


        showVideo = findViewById(R.id.showVideo);
        playVideo = findViewById(R.id.playVideo);
        seekBar = findViewById(R.id.seekBar);
        videoTime = findViewById(R.id.videoTime);
        shrink = findViewById(R.id.shrink);
        progressBar3 = findViewById(R.id.progressBar3);

        init();
        // Set up the user interaction to manually show or hide the system UI.
        showVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                updateTime();
            }
        }
    };

    private void updateTime() {
        if (player != null && player.getCurrentPosition() <= seekBar.getMax()) {
            int currentPosition = player.getCurrentPosition();
            seekBar.setProgress(currentPosition);
            this.currentPst = currentPosition;
            videoTime.setText(simpleTime(currentPosition) + "/" + simpleTime(seekBar.getMax()));
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    private String simpleTime(int time) {
        int hour = time / 1000 / 3600;
        int minute = time / 1000 % 3600 / 60;
        int second = time / 1000 % 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    private void init() {
        final SurfaceHolder holder = showVideo.getHolder();
        holder.setKeepScreenOn(true);
        Bundle sa = getIntent().getExtras();
        if (sa != null) {
            String uriString = sa.getString("uri");
            final Uri uri = Uri.parse(uriString);
            Log.i("ORM", "init: urlString" + uriString);
            currentPst = sa.getInt("progress");
            playVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.isPlaying() && player != null) {
                        player.pause();
                        playVideo.setImageResource(R.drawable.play_light);
                    } else {
                        player.start();
                        playVideo.setImageResource(R.drawable.pause_light);
                    }
                    if (!flag) {
                        flag = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }

                }
            });
            shrink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPst = player.getCurrentPosition();
                    Intent intent = getIntent();
                    intent.putExtra("currentPst", currentPst);
                    setResult(1, intent);
                    smallScreen();
                    finish();
                }
            });
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(final SurfaceHolder holder) {
                    progressBar3.setVisibility(View.VISIBLE);
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(getBaseContext(), uri);
                        player.prepareAsync();
                        player.setDisplay(holder);
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                seekBar.setMax(player.getDuration());
                                handler.sendEmptyMessageDelayed(1, 1000);
                                player.seekTo(currentPst);
                                player.start();
                                progressBar3.setVisibility(View.GONE);
                                playVideo.setImageResource(R.drawable.pause_light);
                            }
                        });
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                flag = false;
                                playVideo.setImageResource(R.drawable.play_light);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    if (player.isPlaying()) {
                        player.stop();
                    }
                    player.reset();
                    player.release();
                    player = null;
                    Log.i("ORM", "surfaceDestroyed: 1");
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    player.pause();
                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    player.seekTo(seekBar.getProgress());
                    playVideo.setImageResource(R.drawable.pause_light);
                    player.start();
                    if (seekBar.getProgress() < seekBar.getMax()) {
                        flag = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                }
            });
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        showVideo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int currentPst = player.getCurrentPosition();
        Intent intent = getIntent();
        intent.putExtra("currentPst", currentPst);
        setResult(1, intent);
        smallScreen();
        finish();
    }

    private void smallScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }
}
