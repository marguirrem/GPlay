package gplay.marlonaguirre.ml.gplay.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import gplay.marlonaguirre.ml.gplay.CreateNotification;
import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.Services.OnClearFromRecentService;
import gplay.marlonaguirre.ml.gplay.interfaces.Playable;
import gplay.marlonaguirre.ml.gplay.pojos.Song;

import static androidx.core.app.NotificationCompat.*;

public class PlayerActivity extends AppCompatActivity  implements Playable {

    ArrayList<Song> songs;
    TextView tvSong,tvDuration,tvArtist,tvAlbum,tvCurrentTime;
    ImageButton btnPlay,btnNext,btnPrev;
    ImageView songImage;
    SeekBar seekBar;
    Runnable mUpdateSeekbar;
    Handler mSeekbarUpdateHandler;
    int position;
    long currentSecond=0;
    int currentMinute=0,sec=0;
    static MediaPlayer mp = new MediaPlayer();
    NotificationManager notificationManager;
    boolean isPlaying = false;

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
        btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initComponents();

        playSong();
        //onTrackPlay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }

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
                  //  btnPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);

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
                if (isPlaying){
                    onTrackPause();
                } else {
                    onTrackPlay();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTrackNext();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onTrackPrevious();
            }
        });
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case CreateNotification.ACTION_PREVIUOS:
                    onTrackPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (isPlaying){
                        onTrackPause();
                    } else {
                        onTrackPlay();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    onTrackNext();
                    break;
            }
        }
    };

    public void playSong(){
        if(position == 0){
            btnPrev.setEnabled(false);
            btnPrev.setVisibility(View.GONE);
        }else if (position == songs.size()){
            btnNext.setEnabled(false);
            btnNext.setVisibility(View.GONE);
        }
        else{
            btnPrev.setEnabled(true);
            btnPrev.setVisibility(View.VISIBLE);
            btnNext.setEnabled(true);
            btnNext.setVisibility(View.VISIBLE);
        }

        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
        }
        try {
            mp = new MediaPlayer();
            mp.setDataSource(songs.get(position).getUrl());
            mp.prepare();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(songs.get(position).getCoveruri()));
                songImage.setImageBitmap(bitmap);

            } catch (Exception exception) {
                songImage.setImageResource(R.drawable.ic_headset_black_24dp);
            }

            updateTexts(position);
            seekBar.setMax((mp.getDuration() / 1000));
            seekBar.setProgress(0);
            mp.start();
            btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);

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
        songImage   = findViewById(R.id.imageSong);
        songs       = new ArrayList<>();
        mSeekbarUpdateHandler     = new Handler();
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

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "KOD Dev", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    @Override
    public void onTrackPrevious() {
        position--;
        CreateNotification.createNotification(PlayerActivity.this, songs.get(position),
                R.drawable.ic_pause_black_24dp, position, songs.size()-1);
        //title.setText(tracks.get(position).getTitle());
        playSong();
    }

    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(PlayerActivity.this, songs.get(position),
                R.drawable.ic_pause_black_24dp, position, songs.size()-1);
        btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        //title.setText(tracks.get(position).getTitle());
        isPlaying = true;
        mp.start();
    }

    @Override
    public void onTrackPause() {

        CreateNotification.createNotification(PlayerActivity.this, songs.get(position),
                R.drawable.ic_play_arrow_black_24dp, position, songs.size()-1);
        //btnPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        //title.setText(tracks.get(position).getTitle());
        isPlaying = false;
        if(mp.isPlaying()) {
            mp.pause();
            btnPlay.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
        else {
            mp.start();
            btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        }
    }

    @Override
    public void onTrackNext() {
        position++;
        CreateNotification.createNotification(PlayerActivity.this, songs.get(position),
                R.drawable.ic_pause_black_24dp, position, songs.size()-1);
        //title.setText(tracks.get(position).getTitle());
        if(position == songs.size()){
            btnNext.setEnabled(false);
        }else{
            btnNext.setEnabled(true);
        }
        playSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }

        unregisterReceiver(broadcastReceiver);
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
    }

    public ImageButton getBtnPrev(){
        return this.btnPrev;
    }

    public  ImageButton getBtnNext(){
        return this.btnNext;
    }
}
