package gplay.marlonaguirre.ml.gplay.activities;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.pojos.Song;

public class PlayerActivity extends AppCompatActivity {

    ArrayList<Song> songs;
    TextView tvSong,tvDuration,tvArtist,tvAlbum;
    ImageButton btnPlay,btnNext,btnPrev;
    SeekBar seekBar;
    Runnable runnable;
    Handler handler;
    static MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        seekBar.setProgress(mp.getCurrentPosition());
        mp.start();
        btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
    }

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initComponents();

        /*MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(song.getAbsolutePath());
        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String autor = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR );
        */


        if(mp.isPlaying()){
            mp.stop();
            mp.release();
            //mp.release();
            Toast.makeText(this, "stop playing", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this, "no stop", Toast.LENGTH_SHORT).show();
        try {
            mp = new MediaPlayer();
            mp.setDataSource(songs.get(position).getUrl());
            mp.prepare();
            seekBar.setMax(mp.getDuration());
            mp.start();
            btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
        } catch (IOException e) {
            e.printStackTrace();
        }






        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                mp.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        updateTexts(position);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){
                    mp.pause();
                    btnPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                }
                else {
                    mp.start();

                    btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position <songs.size()){
                    position++;
                    Toast.makeText(PlayerActivity.this, songs.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    try {
                        if (mp.isPlaying()){
                            mp.stop();
                            mp.release();
                        }
                        mp = new MediaPlayer();
                        mp.setDataSource(songs.get(position).getUrl());
                        mp.prepare();
                        updateTexts(position);
                        seekBar.setMax(mp.getDuration());
                        seekBar.setProgress(0);

                        mp.start();
                        btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position > 0){
                    position--;
                    Toast.makeText(PlayerActivity.this, songs.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                    }
                    try {
                        mp = new MediaPlayer();
                        mp.setDataSource(songs.get(position).getUrl());
                        mp.prepare();
                        updateTexts(position);
                        seekBar.setMax(mp.getDuration());
                        seekBar.setProgress(0);

                        mp.start();
                        btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    public void initComponents(){
        tvSong      = findViewById(R.id.playerTvSong);
        tvDuration  = findViewById(R.id.playerTvSongDuration);
        tvArtist    = findViewById(R.id.playerTvArtist);
        tvAlbum     = findViewById(R.id.playerTvAlbum);
        btnPlay     = findViewById(R.id.btnPlay);
        btnNext     = findViewById(R.id.btnNext);
        btnPrev     = findViewById(R.id.btnPrev);
        songs       = new ArrayList<>();
      //  mp          = new MediaPlayer();
        handler     = new Handler();
        final Bundle file = getIntent().getExtras();
        songs       = (ArrayList<Song>) file.getSerializable("song");
        position    = file.getInt("position");
        seekBar     = findViewById(R.id.songProgress);
    }

    public void updateTexts(int position){
        long timeDuration = Integer.parseInt(songs.get(position).getDuration()) ;
        int totalSeconds = (int) timeDuration/1000;
        int minutes = totalSeconds/60;
        int seconds = totalSeconds % 60;
        tvSong.setText(songs.get(position).getTitle());
        tvAlbum.setText(songs.get(position).getAlbum());
        tvArtist.setText(songs.get(position).getArtist());
        tvDuration.setText(minutes+":"+seconds);
    }
}
