package com.example.tronku.geet;

import android.content.Intent;
import android.content.UriMatcher;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener{

    private TextView title, artistName, current, duration, albumName, year, composers;
    private ImageView banner, back, close, info, blackLayer, transparentLayer;
    private ImageButton play, prev, next, shuffle, repeat;
    private int songPosition, currentPosition;
    private String path;
    private LinearLayout songInfo;
    private ArrayList<Songs> list;
    private Intent songIntent;
    private Utilities utils;
    private SeekBar seekBar;
    private MediaPlayer mp;
    private Handler handler = new Handler();
    private boolean isShuffle = false;
    private boolean isRepeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mp = new MediaPlayer();
        utils = new Utilities();
        title = findViewById(R.id.title);
        artistName = findViewById(R.id.artistName);
        current = findViewById(R.id.currentPos);
        duration = findViewById(R.id.duration);
        banner = findViewById(R.id.banner);
        back = findViewById(R.id.back);
        close = findViewById(R.id.close);
        info = findViewById(R.id.info);
        songInfo = findViewById(R.id.songInfo);
        albumName = findViewById(R.id.albumName);
        year = findViewById(R.id.albumYear);
        composers = findViewById(R.id.composers);
        blackLayer = findViewById(R.id.layer);
        transparentLayer = findViewById(R.id.gradient);
        seekBar = findViewById(R.id.seekbar);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        shuffle = findViewById(R.id.shuffle);
        repeat = findViewById(R.id.repeat);
        seekBar.setOnSeekBarChangeListener(this);
        mp.setOnCompletionListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setImageResource(R.drawable.ic_info_click);
                transparentLayer.setVisibility(View.INVISIBLE);
                blackLayer.setVisibility(View.VISIBLE);
                back.setVisibility(View.INVISIBLE);
                close.setVisibility(View.VISIBLE);
                songInfo.setVisibility(View.VISIBLE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setImageResource(R.drawable.ic_info);
                transparentLayer.setVisibility(View.VISIBLE);
                blackLayer.setVisibility(View.INVISIBLE);
                back.setVisibility(View.VISIBLE);
                close.setVisibility(View.INVISIBLE);
                songInfo.setVisibility(View.INVISIBLE);
            }
        });

        songIntent = getIntent();
        songPosition = songIntent.getIntExtra("position", 0);
        list = songIntent.getParcelableArrayListExtra("songList");

        playSong(songPosition);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        currentPosition = mp.getCurrentPosition();
                        play.setImageResource(R.drawable.play_button);
                    }
                }
                else{
                    if(mp!=null){
                        play.setImageResource(R.drawable.pause_button);
                        mp.seekTo(currentPosition);
                        mp.start();
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songPosition < list.size()-1){
                    songPosition++;

                }
                else {
                    songPosition=0;
                }
                playSong(songPosition);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songPosition > 0){
                    songPosition--;

                }
                else {
                    songPosition = list.size()-1;
                }
                playSong(songPosition);
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF!", Toast.LENGTH_SHORT).show();
                    repeat.setBackgroundColor(Color.TRANSPARENT);
                }
                else{
                    isRepeat = true;
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Repeat is ON!", Toast.LENGTH_SHORT).show();
                    shuffle.setBackgroundColor(Color.TRANSPARENT);
                    repeat.setBackgroundColor(getResources().getColor(R.color.select));

                }
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF!", Toast.LENGTH_SHORT).show();
                    shuffle.setBackgroundColor(Color.TRANSPARENT);
                }
                else{
                    isShuffle = true;
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON!", Toast.LENGTH_SHORT).show();
                    repeat.setBackgroundColor(Color.TRANSPARENT);
                    shuffle.setBackgroundColor(getResources().getColor(R.color.select));
                }
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(isRepeat)
            playSong(songPosition);
        else if(isShuffle){
            Random random = new Random();
            songPosition = random.nextInt(list.size());
            playSong(songPosition);
        }
        else{
            if(songPosition < list.size()-1){
                songPosition++;

            }
            else {
                songPosition=0;
            }
            playSong(songPosition);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateBar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateBar);
        int duration = mp.getDuration();
        int current = utils.progressToTimer(seekBar.getProgress(), duration);
        mp.seekTo(current);
        updateSeekBar();
    }

    public void playSong(int songPosition){
        try{
            mp.reset();
            mp.setDataSource(list.get(songPosition).getPath());
            updateUI(songPosition);
            mp.prepare();
            mp.start();
            play.setImageResource(R.drawable.pause_button);
            seekBar.setMax(100);
            seekBar.setProgress(0);
            updateSeekBar();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSeekBar() {
        handler.postDelayed(mUpdateBar, 100);
    }

    private Runnable mUpdateBar = new Runnable() {
        @Override
        public void run() {
            long currentDuration = mp.getCurrentPosition();
            long totalDuration = mp.getDuration();
            current.setText("" + utils.milliSecondsToTimer(currentDuration));
            duration.setText("" + utils.milliSecondsToTimer(totalDuration));
            int progress = utils.getProgressPercentage(currentDuration, totalDuration);
            seekBar.setProgress(progress);

            handler.postDelayed(this, 100);
        }
    };

    public void updateUI(int songPosition){
        title.setText(list.get(songPosition).getSongTitle());
        artistName.setText(list.get(songPosition).getArtistName());
        composers.setText(list.get(songPosition).getComposer());
        if(composers.length()==0)
            composers.setText("Unknown");
        year.setText(list.get(songPosition).getYear());
        albumName.setText(list.get(songPosition).getAlbum());
        path = list.get(songPosition).getPath();
        info.setImageResource(R.drawable.ic_info);
        transparentLayer.setVisibility(View.VISIBLE);
        blackLayer.setVisibility(View.INVISIBLE);
        back.setVisibility(View.VISIBLE);
        close.setVisibility(View.INVISIBLE);
        songInfo.setVisibility(View.INVISIBLE);
        setThumbnail(path);
    }

    private void setThumbnail(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] raw;
        mmr.setDataSource(getApplicationContext(), Uri.parse(path));
        raw = mmr.getEmbeddedPicture();
        if(raw!=null){
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeByteArray(raw, 0, raw.length, bfo);
            banner.setImageBitmap(bitmap);
        }
        else{
            banner.setImageResource(R.drawable.player_bg);
        }
    }
}
