package gplay.marlonaguirre.ml.gplay.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    MediaPlayer mp;
    File rootDirectory,sdDirectory;
    public static final int RUNTIME_PERMISSION_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        //searchSongs(rootDirectory);
        buscarMusica();

       // getAllAudioFromDevice(this);
        final SongsAdapter adapter = new SongsAdapter(songs_list);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song",  songs_list);
                bundle.putInt("position",recyclerSongs.getChildAdapterPosition(view));
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("folder",  folders_list.get(recyclerFolders
                        .getChildAdapterPosition(view)).getAbsoluteFile());
                Intent intent = new Intent(MainActivity.this,FolderSongs.class);
                intent.putExtras(bundle);
                startActivity(intent);

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
        path            = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        sd              = Environment.getDataDirectory().toString();
        rootDirectory   = new File(path);
        sdDirectory   = new File(sd);
    }

    public void toggleDetails(View view) {
        Toast.makeText(view.getContext(), "Click", Toast.LENGTH_SHORT).show();
    }

   public void buscarMusica(){
       ContentResolver musicResolver = getContentResolver();
       Uri musicUri = MediaStore.Audio.Media.getContentUriForPath(rootDirectory.getAbsolutePath()) ;
       File currentFolder;
       //String [] proj={MediaStore.Audio.Media.getContentUriForPath()};

       //Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

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

                currentFolder = new File(data);
                folders_list.add(currentFolder.getParentFile());
                songs_list.add(new Song(thisId, thisTitle, thisArtist,strAlbum,strDuration,data));

            }
            while (musicCursor.moveToNext());
        }

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
                folders_list.add(file.getParentFile());
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

    // Creating Runtime permission function.
    public void AndroidRuntimePermission(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE

                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel",null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                }
                else {

                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch(requestCode){

            case RUNTIME_PERMISSION_CODE:{

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else {

                }
            }
        }
    }

    public List<Song> getAllAudioFromDevice(final Context context) {


        Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor c = context.getContentResolver().query(uri, projection, selection, null, null);

        if (c != null) {
            while (c.moveToNext()) {

                Song audioModel = new Song();
                String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);

                String name = path.substring(path.lastIndexOf("/") + 1);

                audioModel.setTitle(name);
                audioModel.setAlbum(album);
                audioModel.setArtist(artist);
                audioModel.setUrl(path);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                songs_list.add(audioModel);
            }
            c.close();
        }

        return songs_list;
    }
}
