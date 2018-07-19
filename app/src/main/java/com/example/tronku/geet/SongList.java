package com.example.tronku.geet;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;

public class SongList extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private List<Songs> songs = new ArrayList<>();
    private SongAdapter songAdapter;
    private final static int REQUEST_PERMISSION = 1;

    public SongList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_list, container, false);
        songAdapter = new SongAdapter(getContext(), songs);
        checkPermission();
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(songAdapter);
        return view;
    }

    public void checkPermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }
        }
        else
            fillData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION:
                if(grantResults.length>0){
                    fillData();
                }
                else{
                    Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void fillData(){

        final String track_id = android.provider.MediaStore.Audio.Media._ID;
        final String track_no = android.provider.MediaStore.Audio.Media.TRACK;
        final String track_name = android.provider.MediaStore.Audio.Media.TITLE;
        final String artist = android.provider.MediaStore.Audio.Media.ARTIST;
        final String duration = android.provider.MediaStore.Audio.Media.DURATION;
        final String album = android.provider.MediaStore.Audio.Media.ALBUM;
        final String composer = android.provider.MediaStore.Audio.Media.COMPOSER;
        final String year = android.provider.MediaStore.Audio.Media.YEAR;
        final String path = android.provider.MediaStore.Audio.Media.DATA;
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[]columns={track_id, track_no, artist, track_name,album, duration, path, year, composer};

        Cursor cursor = getActivity().getContentResolver().query(uri, columns, null, null, null);
        if(cursor!=null){
            cursor.moveToFirst();
            do{
                String songTitle = cursor.getString(cursor.getColumnIndexOrThrow(track_name));
                String artistName = cursor.getString(cursor.getColumnIndexOrThrow(artist));
                String albumName = cursor.getString(cursor.getColumnIndexOrThrow(album));
                String composerName = cursor.getString(cursor.getColumnIndexOrThrow(composer));
                String songPath = "file:///" + cursor.getString(cursor.getColumnIndexOrThrow(path));
                Long songDuration = cursor.getLong(cursor.getColumnIndexOrThrow(duration));
                String songYear = cursor.getString(cursor.getColumnIndexOrThrow(year));
                Songs song_item = new Songs(songTitle, artistName, albumName, composerName, songYear, songDuration, songPath);
                songs.add(song_item);
            }
            while (cursor.moveToNext());
            songAdapter.update(songs);
            cursor.close(); }
    }

    private Bitmap getBitmap(Uri uri, Context context){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options bfo=new BitmapFactory.Options();

        mmr.setDataSource(context, uri);
        rawArt = mmr.getEmbeddedPicture();
        if (null != rawArt)
        {
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        }
        return art;
    }

    @Override
    public void onPause() {
        super.onPause();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songlist", (ArrayList<? extends Parcelable>) songs);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("songlist", (ArrayList<? extends Parcelable>) songs);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null)
            songs = savedInstanceState.getParcelableArrayList("songlist");
    }
}
