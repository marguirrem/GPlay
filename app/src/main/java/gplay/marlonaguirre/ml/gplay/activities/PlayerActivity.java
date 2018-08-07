package gplay.marlonaguirre.ml.gplay.activities;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

import static android.support.v4.app.NotificationCompat.*;

public class PlayerActivity extends AppCompatActivity {

    ArrayList<Song> songs;
    TextView tvSong,tvDuration,tvArtist,tvAlbum,tvCurrentTime;
    ImageButton btnPlay,btnNext,btnPrev;
    SeekBar seekBar;
    Runnable mUpdateSeekbar;
    Handler mSeekbarUpdateHandler;
    int position;
    long currentSecond=0;
    int currentMinute=0,sec=0;
    static MediaPlayer mp = new MediaPlayer();
    NotificationManagerCompat notificationManager;
    Builder mBuilder;
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        seekBar.setProgress(mp.getCurrentPosition());
        mp.start();
        btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initComponents();

       playSong();

        mSeekbarUpdateHandler = new Handler();
        Runnable mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                currentMinute =((mp.getCurrentPosition()/1000)/60);
                //if(currentMinute!=0){
                    tvCurrentTime.setText(currentMinute+":"+((currentSecond-60*currentMinute) ));
                currentSecond=(((mp.getCurrentPosition()/1000)) );
                seekBar.setProgress((mp.getCurrentPosition() / 1000));
                Log.e("HILO","currentSecond: "+currentSecond+" totalSeconds: "+((mp.getDuration()/1000)%60));
                if(seekBar.getProgress() == (seekBar.getMax())){
                    position++;
                    btnPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    playSong();
                }
                sec++;

                mSeekbarUpdateHandler.postDelayed(this, 50);
            }
        };

        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo((seekBar.getProgress()*1000));
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
                    playSong();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position > 0){
                    position--;
                    Toast.makeText(PlayerActivity.this, songs.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    playSong();
                }
            }
        });
    }

    public void playSong(){
        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
            mBuilder=null;
        }
        try {

            mp = new MediaPlayer();
            mp.setDataSource(songs.get(position).getUrl());
            mp.prepare();
            updateTexts(position);
            seekBar.setMax((mp.getDuration() / 1000) );
            seekBar.setProgress(0);

            mp.start();
            btnPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);

            mBuilder = new Builder(this,"NOT")
                    .setSmallIcon(R.drawable.ic_play_circle_outline_black_24dp)
                    .setContentTitle(songs.get(position).getArtist())
                    .setContentText(songs.get(position).getTitle())
                    .setStyle(new BigTextStyle()
                            .bigText(songs.get(position).getTitle()))
                    .setPriority(PRIORITY_DEFAULT);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, mBuilder.build());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initComponents(){
        tvSong      = findViewById(R.id.playerTvSong);
        tvDuration  = findViewById(R.id.playerTvSongDuration);
        tvArtist    = findViewById(R.id.playerTvArtist);
        tvAlbum     = findViewById(R.id.playerTvAlbum);
        tvCurrentTime = findViewById(R.id.playerTvCurrentTime);
        btnPlay     = findViewById(R.id.btnPlay);
        btnNext     = findViewById(R.id.btnNext);
        btnPrev     = findViewById(R.id.btnPrev);
        songs       = new ArrayList<>();
        mSeekbarUpdateHandler     = new Handler();
        final Bundle file = getIntent().getExtras();
        songs       = (ArrayList<Song>) file.getSerializable("song");
        position    = file.getInt("position");
        seekBar     = findViewById(R.id.songProgress);
        mBuilder = new Builder(this,"NOT")
                .setSmallIcon(R.drawable.ic_play_circle_outline_black_24dp)
                .setContentTitle(songs.get(position).getArtist())
                .setContentText(songs.get(position).getTitle())
                .setStyle(new BigTextStyle()
                        .bigText(songs.get(position).getTitle()))
                .setPriority(PRIORITY_DEFAULT);

        notificationManager = NotificationManagerCompat.from(this);

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
