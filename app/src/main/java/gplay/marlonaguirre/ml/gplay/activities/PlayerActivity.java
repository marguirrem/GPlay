package gplay.marlonaguirre.ml.gplay.activities;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.Song;

public class PlayerActivity extends AppCompatActivity {

    ArrayList<Song> songs;
    TextView tvSong,tvDuration,tvArtist,tvAlbum;
    ImageButton btnPlay,btnNext,btnPrev;
    MediaPlayer mp;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        tvSong      = findViewById(R.id.playerTvSong);
        tvDuration  = findViewById(R.id.playerTvSongDuration);
        tvArtist    = findViewById(R.id.playerTvArtist);
        tvAlbum     = findViewById(R.id.playerTvAlbum);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        songs = new ArrayList<>();
        mp = new MediaPlayer();
        final Bundle file = getIntent().getExtras();
        songs = (ArrayList<Song>) file.getSerializable("song");
        position = file.getInt("position");
        /*MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(song.getAbsolutePath());
        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String autor = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR );
        */
        try {
            mp.setDataSource(songs.get(position).getUrl());
            mp.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvSong.setText(songs.get(position).getTitle());

        tvDuration.setText(songs.get(position).getDuration());
        tvArtist.setText(songs.get(position).getArtist());
        tvAlbum.setText(songs.get(position).getAlbum());
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){
                    mp.stop();
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
                    Toast.makeText(PlayerActivity.this, "position:"+position, Toast.LENGTH_SHORT).show();
                    try {
                        if (mp.isPlaying()){
                            mp.stop();
                        }
                        mp = new MediaPlayer();
                        updateTexts(position);
                        mp.setDataSource(songs.get(position).getUrl());
                        mp.prepare();
                        mp.start();
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
                    Toast.makeText(PlayerActivity.this, "position: "+position, Toast.LENGTH_SHORT).show();
                    if (mp.isPlaying()){
                        mp.stop();
                    }
                    mp = new MediaPlayer();
                    try {
                        updateTexts(position);
                        mp.setDataSource(songs.get(position).getUrl());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public void updateTexts(int position){
        tvSong.setText(songs.get(position).getTitle());
        tvAlbum.setText(songs.get(position).getAlbum());
        tvArtist.setText(songs.get(position).getArtist());
        tvDuration.setText(songs.get(position).getDuration());
    }
}
