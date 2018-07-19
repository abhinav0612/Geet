package com.example.tronku.geet;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class Songs implements Parcelable{
    private String songTitle, artistName, album, composer, year, path;
    private Long duration;
    private Bitmap thumbnail;

    public Songs(String songTitle, String artistName, String album, String composer, String year, Long duration, String path) {
        this.songTitle = songTitle;
        this.artistName = artistName;
        this.album = album;
        this.composer = composer;
        this.year = year;
        this.duration = duration;
        this.path = path;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    protected Songs(Parcel in) {
        songTitle = in.readString();
        artistName = in.readString();
        album = in.readString();
        composer = in.readString();
        year = in.readString();
        duration = in.readLong();
        path = in.readString();
    }

    public static final Creator<Songs> CREATOR = new Creator<Songs>() {
        @Override
        public Songs createFromParcel(Parcel in) {
            return new Songs(in);
        }

        @Override
        public Songs[] newArray(int size) {
            return new Songs[size];
        }
    };

    public String getSongTitle() {
        return songTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbum() {
        return album;
    }

    public String getComposer() {
        return composer;
    }

    public String getYear() {
        return year;
    }

    public Long getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songTitle);
        dest.writeString(artistName);
        dest.writeString(album);
        dest.writeString(composer);
        dest.writeString(year);
        dest.writeLong(duration);
        dest.writeString(path);
    }
}
