package gplay.marlonaguirre.ml.gplay.activities;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.adapters.SongsAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerSongs;
    ArrayList<String> songs_list;
    ArrayList<String> directories_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerSongs = findViewById(R.id.recyclerSongs);
        songs_list = new ArrayList<>();
        directories_list = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getPath();
        File directorioActual = new File(path);
        for(File file : directorioActual.listFiles()){
            if(file.isDirectory()){
                directories_list.add(file.getAbsolutePath());
            }
        }
        for(String file : directories_list){
            File file2 = new File(file);
            for(File files : file2.listFiles()){
                songs_list.add(files.getName());
            }
        }

        SongsAdapter adapter = new SongsAdapter(songs_list);
        recyclerSongs.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerSongs.setAdapter(adapter);
    }
}
