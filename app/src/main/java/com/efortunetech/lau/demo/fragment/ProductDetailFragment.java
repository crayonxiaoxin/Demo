package com.efortunetech.lau.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.efortunetech.lau.demo.R;
import com.efortunetech.lau.demo.activity.FullscreenActivity;
import com.efortunetech.lau.demo.activity.MainActivity;
import com.efortunetech.lau.demo.bean.ProductBean;

import java.io.Console;
import java.io.IOException;

/**
 * Created by yq06 on 2018/7/13.
 */

public class ProductDetailFragment extends BaseFragment {
    private TextView productName, productDesc, videoTime;
    private Activity mActivity;
    private SurfaceView surfaceView;
    private ImageView playVideo, fullscreen;
    private SeekBar seekBar;
    private MediaPlayer player;
    private ProgressBar progressBar, progressBar2;
    private EditText count;
    private TextView plus, reduce;
    private Button addtocart;
    private SurfaceHolder holder;
    private RelativeLayout playerView;
    private LinearLayout videoControl;
    private int currentPstBack = 0;
    private boolean flag = true;
    private boolean isFullScreen = false;
    private boolean videoControlHidden = false;
    private boolean isAdjust = false;
    private float screenW, screenH;
    private float lastX = 0, lastY = 0;
    private int threshold = 50;

    public static final ProductDetailFragment newInstance(int productId) {
        ProductDetailFragment pdf = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("productId", productId);
        pdf.setArguments(bundle);
        return pdf;
    }

    public static final ProductDetailFragment newInstance_static(String title, String desc) {
        ProductDetailFragment pdf = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productName", title);
        bundle.putString("productDesc", desc);
        pdf.setArguments(bundle);
        return pdf;
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
            videoTime.setText(simpleTime(currentPosition) + "/" + simpleTime(seekBar.getMax()));
            handler.sendEmptyMessageDelayed(1, 500);
        }
    }

    private String simpleTime(int time) {
        int hour = time / 1000 / 3600;
        int minute = time / 1000 % 3600 / 60;
        int second = time / 1000 % 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productdetail, null);
        playerView = view.findViewById(R.id.player);
        videoControl = view.findViewById(R.id.videoControl);
        productName = view.findViewById(R.id.productName);
        productDesc = view.findViewById(R.id.productDesc);
        surfaceView = view.findViewById(R.id.surfaceView);
        playVideo = view.findViewById(R.id.playVideo);
        seekBar = view.findViewById(R.id.seekBar);
        videoTime = view.findViewById(R.id.videoTime);
        fullscreen = view.findViewById(R.id.fullscreen);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar2 = view.findViewById(R.id.progressBar2);
        count = view.findViewById(R.id.count);
        reduce = view.findViewById(R.id.reduce);
        plus = view.findViewById(R.id.plus);
        addtocart = view.findViewById(R.id.btn_addToCar);
        Log.i("ORM", "onCreateView: ");
        init();
        return view;
    }

    private void init() {
        progressBar.setVisibility(View.VISIBLE);
        screenW = mActivity.getResources().getDisplayMetrics().widthPixels;
        screenH = mActivity.getResources().getDisplayMetrics().heightPixels;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            progressBar.setVisibility(View.GONE);
            productName.setText(bundle.getString("productName"));
            productDesc.setText(bundle.getString("productDesc"));
            surfaceView.setZOrderMediaOverlay(true);
//            surfaceView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isFullScreen) {
//                        if (videoControlHidden) {
//                            videoControl.setVisibility(View.VISIBLE);
//                            videoControlHidden = false;
//                        } else {
//                            videoControl.setVisibility(View.GONE);
//                            videoControlHidden = true;
//                        }
//                    }
//                }
//            });
            surfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (isFullScreen) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                lastX = event.getX();
                                lastY = event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                float endX = event.getX();
                                float endY = event.getY();
                                float deltaX = endX - lastX;
                                float deltaY = endY - lastY;
                                float absDeltaX = Math.abs(deltaX);
                                float absDeltaY = Math.abs(deltaY);
                                if (absDeltaX < threshold && absDeltaY > threshold) {
                                    isAdjust = true;
                                } else if (absDeltaX < threshold && absDeltaY < threshold) {
                                    isAdjust = false;
                                } else if (absDeltaX > threshold && absDeltaY > threshold) {
                                    if (absDeltaX < absDeltaY) {
                                        isAdjust = true;
                                    } else {
                                        isAdjust = false;
                                    }
                                } else {
                                    isAdjust = false;
                                }
                                if (isAdjust) {
                                    if (endX < screenW / 2) {
                                        // 左邊調節亮度
                                        changeBrightness(-deltaY);
                                    } else {
                                        // 右邊調節音量
                                        changeVolume(-deltaY);
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                if (videoControlHidden) {
                                    videoControl.setVisibility(View.VISIBLE);
                                    videoControlHidden = false;
                                } else {
                                    videoControl.setVisibility(View.GONE);
                                    videoControlHidden = true;
                                }
                                break;
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            playVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player != null && player.isPlaying()) {
                        player.pause();
                        if (isFullScreen) {
                            playVideo.setImageResource(R.drawable.play_light);
                        } else {
                            playVideo.setImageResource(R.drawable.play);
                        }
                    } else {
                        player.start();
                        if (isFullScreen) {
                            playVideo.setImageResource(R.drawable.pause_light);

                        } else {
                            playVideo.setImageResource(R.drawable.pause);
                        }
                    }
                    if (!flag) {
                        flag = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }

                }
            });

            final String urlString = "http://teststreaming7v.s3.amazonaws.com/public/7515/1374782317346-beagle_puppy_howl_640x360_448_main.mp4";
            final Uri uri = Uri.parse(urlString);

            holder = surfaceView.getHolder();
            holder.setKeepScreenOn(true);
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(final SurfaceHolder holder) {
                    Log.i("ORM", "surfaceCreated: 创建");
                    progressBar2.setVisibility(View.VISIBLE);
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(mActivity, uri);
                        player.prepareAsync();
                        player.setDisplay(holder);
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                seekBar.setMax(player.getDuration());
                                handler.sendEmptyMessageDelayed(1, 1000);
                                seekBar.setProgress(currentPstBack);
                                player.seekTo(seekBar.getProgress());
                                player.start();
                                progressBar2.setVisibility(View.GONE);
                                if (isFullScreen) {
                                    playVideo.setImageResource(R.drawable.pause_light);
                                } else {
                                    playVideo.setImageResource(R.drawable.pause);
                                }
                            }
                        });
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                flag = false;
                                if (isFullScreen) {
                                    playVideo.setImageResource(R.drawable.play_light);
                                    videoControlHidden = false;
                                    videoControl.setVisibility(View.VISIBLE);
                                } else {
                                    playVideo.setImageResource(R.drawable.play);
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                           int height) {
                    Log.i("ORM", "surfaceChanged: 改变");
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    if (player.isPlaying()) {
                        player.stop();
                    }
                    player.reset();
                    player.release();
                    player = null;
                    Log.i("ORM", "surfaceDestroyed: 销毁");
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

            {
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
                    if (isFullScreen) {
                        playVideo.setImageResource(R.drawable.pause_light);
                    } else {
                        playVideo.setImageResource(R.drawable.pause);
                    }
                    player.start();
                    if (seekBar.getProgress() < seekBar.getMax()) {
                        flag = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                }
            });
            fullscreen.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    if (!isFullScreen) {
                        toFullScreen();
                        screenH = mActivity.getResources().getDisplayMetrics().widthPixels;
                        screenW = mActivity.getResources().getDisplayMetrics().heightPixels;
                    } else {
                        toNormalScreen();
                        screenW = mActivity.getResources().getDisplayMetrics().widthPixels;
                        screenH = mActivity.getResources().getDisplayMetrics().heightPixels;
                    }
                }
            });


            reduce.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    int num = Integer.parseInt(count.getText().toString());
                    if (num > 1) {
                        count.setText("" + (num - 1));
                    } else {
                        count.setText("" + 1);
                    }
                }
            });
            plus.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    int num = Integer.parseInt(count.getText().toString()) + 1;
                    count.setText("" + num);
                }
            });
            addtocart.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "開發中...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void changeVolume(float y) {
        AudioManager autioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        int max = autioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = autioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int index = (int) (y / screenH * max);
        int volume = Math.max(current + index, 0);
        autioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    private void changeBrightness(float y) {
        WindowManager.LayoutParams attributes = mActivity.getWindow().getAttributes();
        float brightness = attributes.screenBrightness;
        float index = y / screenH / 3;
        brightness += index;
        if (brightness > 1.0f) {
            brightness = 1.0f;
        }
        if (brightness < 0.01f) {
            brightness = 0.01f;
        }
        attributes.screenBrightness = brightness;
        mActivity.getWindow().setAttributes(attributes);
    }

    private void toNormalScreen() {
        isFullScreen = false;
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = dp2px(mActivity, 240);
        params.setMargins(dp2px(mActivity, 8), dp2px(mActivity, 8), dp2px(mActivity, 8), dp2px(mActivity, 8));
        playerView.setLayoutParams(params);
        ViewGroup.LayoutParams params_pv = surfaceView.getLayoutParams();
        params_pv.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params_pv.height = dp2px(mActivity, 200);
        surfaceView.setLayoutParams(params_pv);
        videoControl.setBackgroundColor(Color.parseColor("#eeeeee"));
        if (player.isPlaying()) {
            playVideo.setImageResource(R.drawable.pause);
        } else {
            playVideo.setImageResource(R.drawable.play);
        }
        fullscreen.setImageResource(R.drawable.fullscreen);
        videoTime.setTextColor(Color.parseColor("#80000000"));
        MainActivity.topbar.setVisibility(View.VISIBLE);
        MainActivity.newsTab.setVisibility(View.VISIBLE);
        videoControl.setVisibility(View.VISIBLE);
        videoControlHidden = false;
    }

    private void toFullScreen() {
        isFullScreen = true;
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0, 0, 0, 0);
        playerView.setLayoutParams(params);
        ViewGroup.LayoutParams params_pv = surfaceView.getLayoutParams();
        params_pv.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params_pv.height = ViewGroup.LayoutParams.MATCH_PARENT;
        surfaceView.setLayoutParams(params_pv);
        videoControl.setBackgroundColor(Color.parseColor("#80000000"));
        if (player.isPlaying()) {
            playVideo.setImageResource(R.drawable.pause_light);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFullScreen && !videoControlHidden) {
                        videoControl.setVisibility(View.GONE);
                        videoControlHidden = true;
                    }
                }
            }, 2000);
        } else {
            playVideo.setImageResource(R.drawable.play_light);
        }
        fullscreen.setImageResource(R.drawable.shrink);
        videoTime.setTextColor(Color.parseColor("#ffffff"));
        MainActivity.topbar.setVisibility(View.GONE);
        MainActivity.newsTab.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ORM", "onPause: 暂停");
        currentPstBack = seekBar.getProgress();
        if (isFullScreen) {
            toNormalScreen();
        }
    }

    @Override
    public boolean onBackPressed() {
        Log.i("ORM", "onBackPressed: 返回事件");
        if (isFullScreen) {
            toNormalScreen();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
