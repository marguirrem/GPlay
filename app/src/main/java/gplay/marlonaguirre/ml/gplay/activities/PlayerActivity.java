package gplay.marlonaguirre.ml.gplay.activities;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.IOException;
import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.Song;

public class PlayerActivity extends AppCompatActivity {

    TextView tvSong,tvDuration,tvArtist,tvAlbum;
    ImageButton btnPlay;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        tvSong      = findViewById(R.id.playerTvSong);
        tvDuration  = findViewById(R.id.playerTvSongDuration);
        tvArtist    = findViewById(R.id.playerTvArtist);
        tvAlbum     = findViewById(R.id.playerTvAlbum);
        btnPlay = findViewById(R.id.btnPlay);
        mp = new MediaPlayer();
        final Bundle file = getIntent().getExtras();
        Song song = (Song) file.getSerializable("song");
        /*MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(song.getAbsolutePath());
        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String autor = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR );
        */
        try {
            mp.setDataSource(song.getUrl());
            mp.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvSong.setText(song.getTitle());

        tvDuration.setText(song.getDuration());
        tvArtist.setText(song.getArtist());
        tvAlbum.setText(song.getAlbum());
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
    }
}
