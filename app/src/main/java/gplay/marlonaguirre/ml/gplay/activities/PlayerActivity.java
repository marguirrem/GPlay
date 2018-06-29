package gplay.marlonaguirre.ml.gplay.activities;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import gplay.marlonaguirre.ml.gplay.R;

public class PlayerActivity extends AppCompatActivity {

    TextView tvSong,tvDuration,tvArtist;
    Button btnPlay;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        tvSong      = findViewById(R.id.playerTvSong);
        tvDuration  = findViewById(R.id.playerTvSongDuration);
        tvArtist    = findViewById(R.id.playerTvArtist);

        btnPlay = findViewById(R.id.btnPlay);
        mp = new MediaPlayer();
        final Bundle file = getIntent().getExtras();
        final File song = (File) file.getSerializable("song");
        try {
            mp.setDataSource(song.getAbsolutePath());
            mp.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvSong.setText(song.getName());
        int intduration =mp.getDuration();
        float minutes = (intduration / 1000);
        float seconds = (minutes / 60);
        String duration =String.valueOf(minutes + seconds);
        tvDuration.setText(duration);
        tvArtist.setText(mp.getTrackInfo().toString());

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){
                    mp.stop();
                }
                else {
                    mp.start();
                }
            }
        });
    }
}
