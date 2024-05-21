package com.example.androidaudioplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class SecondActivity extends AppCompatActivity {

    MediaPlayer mPlayer;
    String Path;
    String selectMp3;
    Thread MThread;
    boolean checkPlay;
    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        Toolbar toolbar = findViewById(R.id.s_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        TextView name = findViewById(R.id.scname);
        TextView mTime = findViewById(R.id.sctime);
        ImageView image = findViewById(R.id.scimage);
        SeekBar seekBar = findViewById(R.id.seekbar);
        selectMp3 = intent.getStringExtra("selectMp3");
        Path = intent.getStringExtra("Path");
        checkPlay = true;
        name.setText(selectMp3);
        image.setImageResource(R.drawable.v);

        try {
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(Path + selectMp3);
                    mPlayer.prepareAsync();
                    mPlayer.setOnPreparedListener(mp -> {
                        mp.start();
                        mp.setLooping(false);
                        seekBar.setMax(mp.getDuration());
                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if(fromUser){
                                    mPlayer.seekTo(progress);
                                }

                                if(seekBar.getMax() == progress){
                                    mPlayer.stop();
                                }
                            }
                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                mPlayer.pause();
                            }
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                mPlayer.seekTo(seekBar.getProgress());
                                mPlayer.start();
                            }
                        });
                        MThread = new Thread(() -> {
                            while (mPlayer !=null && mPlayer.isPlaying()){
                                try{
                                    Thread.sleep(1000);
                                    runOnUiThread(()->{
                                        int currentP = mPlayer.getCurrentPosition();
                                        seekBar.setProgress(currentP);
                                        mTime.setText(timeFormat.format(mPlayer.getCurrentPosition()));
                                    });
                                }catch(Exception e){}
                            }
                        });
                        MThread.start();
                    });
                } catch (IOException e) {
                }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                mPlayer.stop();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
