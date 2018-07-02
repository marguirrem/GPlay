package gplay.marlonaguirre.ml.gplay.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.pojos.Song;
import gplay.marlonaguirre.ml.gplay.adapters.FoldersAdapter;
import gplay.marlonaguirre.ml.gplay.adapters.SongsAdapter;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerSongs,recyclerFolders;
    ArrayList<File> folders_list;
    ArrayList<Song>songs_list;
    TabHost tabHost;
    String path,sd;
    File rootDirectory,sdDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        //searchSongs(rootDirectory);
        buscarMusica();

        final SongsAdapter adapter = new SongsAdapter(songs_list);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song",  songs_list);
                bundle.putInt("position",recyclerFolders.getChildAdapterPosition(view));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        extractDuplicateFolders();
        FoldersAdapter adapterFolders = new FoldersAdapter(folders_list);

        adapterFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"folder: "+
                                folders_list.get(recyclerFolders
                                        .getChildAdapterPosition(view)).getName()
                        , Toast.LENGTH_SHORT).show();
            }
        });
        recyclerSongs.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerFolders.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerSongs.setAdapter(adapter);
        recyclerFolders.setAdapter(adapterFolders);
    }

    private void extractDuplicateFolders() {
        HashSet<File> hashSet = new HashSet<File>(folders_list);
        folders_list.clear();
        folders_list.addAll(hashSet);
    }

    public void initComponents(){
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec thSongs     = tabHost.newTabSpec("Tab1");
        thSongs.setIndicator("Songs");
        thSongs.setContent(R.id.layoutSongs);

        TabHost.TabSpec thFolders   = tabHost.newTabSpec("Tab2");
        thFolders.setIndicator("Folders");
        thFolders.setContent(R.id.layoutFolders);
        tabHost.addTab(thSongs);
        tabHost.addTab(thFolders);
        recyclerSongs   = findViewById(R.id.recyclerSongs);
        recyclerFolders = findViewById(R.id.recyclerFolders);
        folders_list    = new ArrayList<>();
        songs_list      = new ArrayList<>();
        path            = Environment.getExternalStorageDirectory().getPath();
        sd              = Environment.getDataDirectory().toString();
        rootDirectory   = new File(path);
        sdDirectory   = new File(sd);
    }

    public void toggleDetails(View view) {
        Toast.makeText(view.getContext(), "Click", Toast.LENGTH_SHORT).show();
    }

    public void buscarMusica(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

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
   /* private ArrayList<File> searchSongs(File path) {
        for(File file : path.listFiles()){
            if(file.isFile()  && !file.isHidden() && (
                                                    file.getName().endsWith(".m4a")
                                                            || file.getName().endsWith(".ogg")
                                                            || file.getName().endsWith(".wma")
                                                            || file.getName().endsWith(".mp3")
                                                            || file.getName().endsWith(".wav")
                                                            || file.getName().endsWith(".acc")
            ) ){
                folders_list.add(file.getParentFile());
                songs_list.add(file);
            }else{
                if(file.isDirectory() && !file.isHidden()){
                    searchSongs(file);
                }
            }
        }
        return  songs_list;
    }*/
}
