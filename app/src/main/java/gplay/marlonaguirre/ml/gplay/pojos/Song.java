package gplay.marlonaguirre.ml.gplay.pojos;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Song implements Serializable {
    private long id;
    private String title;
    private String artist;
    private String url;
    private String album;
    private String duration;
   // private Bitmap bitmap;
    private String coveruri;


    public Song(long id, String title, String artist, String album, String duration, String url,String coveruri) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.url = url;
        //this.bitmap = bitmap;
        this.coveruri = coveruri;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }

//    public Bitmap getBitmap() {
  //      return this.bitmap;
    //}


    public String getCoveruri() {
        return coveruri;
    }
}
