package gplay.marlonaguirre.ml.gplay.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.adapters.SongsAdapter;
import gplay.marlonaguirre.ml.gplay.pojos.Song;

public class FolderSongs extends AppCompatActivity {

    MediaPlayer mp;
    RecyclerView recyclerSongs,recyclerFolders;
    ArrayList<Song> songs_list;
    File folder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_songs);
        initComponents();
        //searchSongs(folder);
        buscarMusica();


        final SongsAdapter adapter = new SongsAdapter(songs_list);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FolderSongs.this, PlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song",  songs_list);
                bundle.putInt("position", recyclerSongs.getChildAdapterPosition(view));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        recyclerSongs.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerSongs.setAdapter(adapter);

    }

    public void initComponents(){
        recyclerSongs = findViewById(R.id.recyclerSongs);
        recyclerFolders = findViewById(R.id.recyclerFolders);
        songs_list = new ArrayList<>();
        final Bundle bundle = getIntent().getExtras();
        folder       = (File) bundle.getSerializable("folder");
        mp = new MediaPlayer();
    }

    private ArrayList<Song> searchSongs(File path) {
        Song song;
        for(File file : path.listFiles()){
            if(file.isFile()  && !file.isHidden() && (
                    file.getName().endsWith(".m4a")
                            || file.getName().endsWith(".ogg")
                            || file.getName().endsWith(".wma")
                            || file.getName().endsWith(".mp3")
                            || file.getName().endsWith(".wav")
                            || file.getName().endsWith(".acc")
            ) ){
                song = new Song();
                song.setTitle(file.getName());

                song.setUrl(file.getPath());

                mp = new MediaPlayer();
                try {
                    mp.setDataSource(song.getUrl());
                    mp.prepare();
                    song.setDuration(String.valueOf(mp.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                songs_list.add(song);
            }else{
                if(file.isDirectory() && !file.isHidden()){
                    searchSongs(file);
                }
            }
        }
        return  songs_list;
    }

    public void buscarMusica(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] proj = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST};
        Cursor musicCursor = musicResolver.query(musicUri,
                null,
                MediaStore.Audio.Media.DATA + " like ?",
                new String[]{"%"+folder.getName()+"%"}, // Put your device folder / file location here.
                null);


        String selection = MediaStore.Audio.Media.TITLE;


       // Uri musicUri = Uri.fromFile(folder.getAbsoluteFile());

        //Cursor musicCursor = musicResolver.query(Uri.fromFile(folder.getAbsoluteFile()), proj, selection, null, null);



        if(musicCursor!=null && musicCursor.moveToFirst()){

            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int intDuration = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.DURATION);
            int intUrl = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.DATA);
            int intAlbum = musicCursor.getColumnIndex(
                    MediaStore.Audio.Media.ALBUM);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);

            //add songs to list
            do {
                String strDuration = musicCursor.getString(intDuration);
                String strAlbum = musicCursor.getString(intAlbum);
                String data = musicCursor.getString(intUrl);
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                songs_list.add(new Song(thisId, thisTitle, thisArtist,strAlbum,strDuration,data));
            }
            while (musicCursor.moveToNext());
        }

    }

}
