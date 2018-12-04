package gplay.marlonaguirre.ml.gplay.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.adapters.SongsAdapter;
import gplay.marlonaguirre.ml.gplay.pojos.Song;

public class SongsFolder extends AppCompatActivity {

    RecyclerView recyclerSongs;
    ArrayList<Song> songs_list;
    String folder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_folder);
        initComponents();
        buscarMusicaFolder();


        SongsAdapter adapter = new SongsAdapter(songs_list,this);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongsFolder.this, PlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song",  songs_list);
                bundle.putInt("position",recyclerSongs.getChildAdapterPosition(view));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerSongs.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerSongs.setAdapter(adapter);

    }

    public void initComponents() {

        recyclerSongs       = findViewById(R.id.recyclerSongsFolder);
        songs_list          = new ArrayList<>();
        Bundle file         = getIntent().getExtras();
        folder              = file.getString("folder");
    }


    public void buscarMusicaFolder() {
        // Query URI
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Columns
        String[] select = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION
        };

        // Where
        String where = MediaStore.Audio.Media.IS_MUSIC + "=1";

        Cursor cursor = getContentResolver().query(
                uri, select,
                android.provider.MediaStore.Audio.Media.DATA + " like ? ",
                new String[] {"%"+this.folder+"%"}, null);

        // Perform the query
        // cursor = this.getContentResolver().query(uri, select, where, null, null);

        if (cursor.moveToFirst()) {
            Bitmap bitmap = null;

            while (!cursor.isAfterLast()) {
                long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String track = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albumId);
/*
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), albumArtUri);
                } catch (Exception exception) {
                    //Log.e("bitmap",exception.getMessage());
                }*/
                //songs_list.add(new Song(albumId, track, artist,album,String.valueOf(duration),data,bitmap));
               // songs_list.add(new Song(albumId, track, artist,album,String.valueOf(duration),data));
                songs_list.add(new Song(albumId, track, artist,album,String.valueOf(duration),data,"content://media/external/audio/albumart/"+albumId));
                cursor.moveToNext();
            }
            cursor.close();

        }
    }
}
