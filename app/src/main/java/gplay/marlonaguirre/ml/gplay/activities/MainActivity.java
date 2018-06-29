package gplay.marlonaguirre.ml.gplay.activities;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.adapters.SongsAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerSongs,recyclerFolders;
    ArrayList<String> songs_list,foilders_list;
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = (TabHost) findViewById(R.id.tabHost);


        tabHost.setup();
        TabHost.TabSpec thSongs = tabHost.newTabSpec("Tab1");
        thSongs.setIndicator("Songs");
        thSongs.setContent(R.id.layoutSongs);


        TabHost.TabSpec thFolders = tabHost.newTabSpec("Tab2");
        thFolders.setIndicator("Folders");
        thFolders.setContent(R.id.layoutFolders);

        tabHost.addTab(thSongs);
        tabHost.addTab(thFolders);

        recyclerSongs = findViewById(R.id.recyclerSongs);

        foilders_list = new ArrayList<>();
        songs_list = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().getPath();
        File directorioActual = new File(path);

        searchSongs(directorioActual);
        // Ordenamos la lista de archivos para que se muestren en orden alfabetico
        Collections.sort(songs_list, String.CASE_INSENSITIVE_ORDER);

        final SongsAdapter adapter = new SongsAdapter(songs_list);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        songs_list.get(recyclerSongs
                                .getChildAdapterPosition(view))
                        , Toast.LENGTH_SHORT).show();

            }
        });
        recyclerSongs.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerSongs.setAdapter(adapter);
    }

    public void toggleDetails(View view) {
        Toast.makeText(view.getContext(), "Click", Toast.LENGTH_SHORT).show();
    }
    private ArrayList<String> searchSongs(File path) {
        for(File file : path.listFiles()){
            if(file.isFile()  && file.getName().endsWith(".mp3") && !file.isHidden()){
                foilders_list.add(file.getAbsoluteFile().getName());
//                Log.e("archivo: ",file.getName()+" FOLDER: " +file.getParentFile().getName());
                songs_list.add(file.getName());
            }else{
                if(file.isDirectory() && !file.isHidden()){
//                    Log.e("DIRECTORIO",file.getName());
                    searchSongs(file);
                }
            }

        }
        return  songs_list;
    }
}
