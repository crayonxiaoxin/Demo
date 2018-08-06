package com.efortunetech.lau.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efortunetech.lau.demo.R;
import com.efortunetech.lau.demo.activity.FullscreenActivity;

import java.io.IOException;

/**
 * Created by yq06 on 2018/7/13.
 */

public class ProductDetailFragment_bak extends Fragment {
    private TextView productName, productDesc;
    private Activity mActivity;
    private SurfaceView productVideo;
    private ImageView playVideo;
    private SeekBar seekBar;
    private TextView videoTime;
    private MediaPlayer player;
    private boolean flag = true;
    private ProgressBar progressBar, progressBar2;
    private EditText count;
    private TextView plus;
    private TextView reduce;
    private Button addtocart;
    private ImageView fullscreen;
    private SurfaceHolder holder;
    private int currentPstBack = 0;
    private ConstraintLayout playerView;


    public static final ProductDetailFragment_bak newInstance(int productId) {
        ProductDetailFragment_bak pdf = new ProductDetailFragment_bak();
        Bundle bundle = new Bundle();
        bundle.putInt("productId", productId);
        pdf.setArguments(bundle);
        return pdf;
    }

    public static final ProductDetailFragment_bak newInstance_static(String title, String desc) {
        ProductDetailFragment_bak pdf = new ProductDetailFragment_bak();
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
            handler.sendEmptyMessageDelayed(1, 1000);
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
        productName = view.findViewById(R.id.productName);
        productDesc = view.findViewById(R.id.productDesc);
        productVideo = view.findViewById(R.id.surfaceView);
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
        init();
        return view;
    }

    private void init() {
        progressBar.setVisibility(View.VISIBLE);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            progressBar.setVisibility(View.GONE);
//            int productId = bundle.getInt("productId");
//            productName.setText("productName "+productId);
//            productDesc.setText("productDesc "+productId);

            productName.setText(bundle.getString("productName"));
            productDesc.setText(bundle.getString("productDesc"));

//            progressBar2.setVisibility(View.VISIBLE);
            productVideo.setZOrderMediaOverlay(true);
            playVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player != null && player.isPlaying()) {
                        player.pause();
                        playVideo.setImageResource(R.drawable.play);
                    } else {
                        player.start();
                        playVideo.setImageResource(R.drawable.pause);
                    }
                    if (!flag) {
                        flag = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }

                }
            });
            final String urlString = "http://teststreaming7v.s3.amazonaws.com/public/7515/1374782317346-beagle_puppy_howl_640x360_448_main.mp4";
            final Uri uri = Uri.parse(urlString);


//            player = new MediaPlayer();

            holder = productVideo.getHolder();
            holder.setKeepScreenOn(true);
//            holder.setFixedSize(w,h);
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
                                playVideo.setImageResource(R.drawable.pause);
                            }
                        });
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                flag = false;
                                playVideo.setImageResource(R.drawable.play);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
                    playVideo.setImageResource(R.drawable.pause);
                    player.start();
                    if (seekBar.getProgress() < seekBar.getMax()) {
                        flag = true;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                }
            });
            fullscreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mActivity,"開發中...",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mActivity, FullscreenActivity.class);
                    intent.putExtra("uri", urlString);
                    Log.i("ORM", "onClick: " + urlString);
                    int currentPst = (player != null) ? player.getCurrentPosition() : 0;
                    intent.putExtra("progress", currentPst);
                    startActivityForResult(intent, 1);
//                    mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
//                    playerView.setLayoutParams(params);
                }
            });

            reduce.setOnClickListener(new View.OnClickListener() {
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
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int num = Integer.parseInt(count.getText().toString()) + 1;
                    count.setText("" + num);
                }
            });
            addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "開發中...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            Log.i("ORM", "onActivityResult: " + data.getIntExtra("currentPst", 0));
            this.currentPstBack = data.getIntExtra("currentPst", 0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ORM", "onPause: 暂停");
        currentPstBack = seekBar.getProgress();
    }
}
