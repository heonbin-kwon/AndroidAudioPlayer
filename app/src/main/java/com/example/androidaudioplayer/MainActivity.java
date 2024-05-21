package com.example.androidaudioplayer;

import com.example.androidaudioplayer.R;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public void checkPermission(){
        int permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionWrite != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "이 앱을 실행하기 위해 권한이 필요합니다.",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);

        }
    }

    private ListView listView;
    int currentPlay = 0;
    ArrayList<String> mp3List;
    String mp3Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    String selectMp3;
    MediaPlayer mPlayer;
    boolean[] Playing;
    //SeekBar currentSB = null;
    Thread MThread = null;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setTitle("DCU Music Player");
        // 저장소 접근 허가 요청
        checkPermission();

        mp3List = new ArrayList<String>();

        File[] listFiles = new File(mp3Path).listFiles();

        String fileName, extName;

        for (File file : listFiles) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            if (extName.equals((String) "mp3"))
                mp3List.add(fileName);
        }

        listView = (ListView) findViewById(R.id.listView3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.audioview, R.id.musicName, mp3List) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ImageView iv = view.findViewById(R.id.music1);
                TextView tx = view.findViewById(R.id.musicTime);
                iv.setImageResource(R.drawable.a);
                return view;
            }
        };
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);

        boolean[] Playing = new boolean[mp3List.size()];

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mPlayer != null){
//                    mPlayer.stop();
//                    mPlayer.release();
//                    mPlayer = null;
//                    if(currentPlay != position){
//                        Playing[currentPlay] = false;
//                        if(MThread != null){
//                            MThread.interrupt();
//                            MThread = null;
//                        }
//                    }
//                }
//                selectMp3 = mp3List.get(position);
//                try {
//                    mPlayer = new MediaPlayer();
//                    mPlayer.setDataSource(mp3Path + selectMp3);
//                    mPlayer.prepareAsync();
//                    mPlayer.setOnPreparedListener(mp -> {
//                        mp.start();
//                        TextView mTime = view.findViewById(R.id.musicTime);
//                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                            @Override
//                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                                if(fromUser){
//                                    mPlayer.seekTo(progress);
//                                }
//                            }
//                            @Override
//                            public void onStartTrackingTouch(SeekBar seekBar) {
//
//                            }
//                            @Override
//                            public void onStopTrackingTouch(SeekBar seekBar) {
//
//                            }
//                        });
//                        MThread = new Thread(() -> {
//                            while (mPlayer !=null && mPlayer.isPlaying()){
//                                try{
//                                    Thread.sleep(1000);
//                                    runOnUiThread(()->{
//                                        int currentP = mPlayer.getCurrentPosition();
//                                        currentSB.setProgress(currentP);
//                                        mTime.setText(timeFormat.format(mPlayer.getCurrentPosition()));
//                                    });
//                                }catch(Exception e){}
//                            }
//                        });
//                        MThread.start();
//                    });
//                    currentPlay = position;
//                    adapter.notifyDataSetChanged();
//                } catch (IOException e) {
//                }
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                selectMp3 = mp3List.get(position);
                intent.putExtra("selectMp3", selectMp3);
                intent.putExtra("Path",mp3Path);
                startActivity(intent);
                // 넘겨주기
            }
        });
        selectMp3 = mp3List.get(0);

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        if (MThread != null) {
            MThread.interrupt();
            MThread = null;
        }
    }
}
