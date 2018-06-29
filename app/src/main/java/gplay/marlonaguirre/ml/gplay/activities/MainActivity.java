package gplay.marlonaguirre.ml.gplay.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.adapters.FoldersAdapter;
import gplay.marlonaguirre.ml.gplay.adapters.SongsAdapter;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerSongs,recyclerFolders;
    ArrayList<File> songs_list,folders_list;
    TabHost tabHost;
    String path;
    File rootDirectory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        searchSongs(rootDirectory);

        //Collections.sort(songs_list.get().getName(), String.CASE_INSENSITIVE_ORDER);
        final SongsAdapter adapter = new SongsAdapter(songs_list);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("song",songs_list.get(recyclerSongs.getChildAdapterPosition(view)));
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
        rootDirectory   = new File(path);
    }

    public void toggleDetails(View view) {
        Toast.makeText(view.getContext(), "Click", Toast.LENGTH_SHORT).show();
    }
    private ArrayList<File> searchSongs(File path) {
        for(File file : path.listFiles()){
            if(file.isFile()  && file.getName().endsWith(".mp3") && !file.isHidden()){
                folders_list.add(file.getParentFile());
                songs_list.add(file);
            }else{
                if(file.isDirectory() && !file.isHidden()){
                    searchSongs(file);
                }
            }
        }
        return  songs_list;
    }
}
