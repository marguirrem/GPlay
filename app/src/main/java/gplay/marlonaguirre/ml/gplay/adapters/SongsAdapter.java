package gplay.marlonaguirre.ml.gplay.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gplay.marlonaguirre.ml.gplay.R;
import gplay.marlonaguirre.ml.gplay.activities.MainActivity;
import gplay.marlonaguirre.ml.gplay.pojos.Song;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolderSongs>
        implements View.OnClickListener {
    private View.OnClickListener listener;
    private Context context;
    ArrayList<Song> songs_list;


    public SongsAdapter(ArrayList<Song> songs_list, Context context) {
        this.songs_list = songs_list;
        this.context = context;
    }

    @NonNull
    @Override
    public SongsAdapter.ViewHolderSongs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item_list,null,false);
        view.setOnClickListener(this);
        return new ViewHolderSongs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.ViewHolderSongs holder, int position) {
        holder.tvSong.setText( songs_list.get(position).getTitle());
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap( context.getContentResolver(), Uri.parse(songs_list.get(position).getCoveruri()));
            holder.songView.setImageBitmap(bitmap);

        } catch (Exception exception) {
            //Log.e("bitmap",exception.getMessage());
            holder.songView.setImageResource(R.drawable.ic_headset_black_24dp);
        }

/*
        if(songs_list.get(position).getBitmap() != null){
            holder.songView.setImageBitmap(songs_list.get(position).getBitmap());
        }else {
            holder.songView.setImageResource(R.drawable.ic_headset_black_24dp);
        }*/

    }

    @Override
    public int getItemCount() {
        return songs_list.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }


    public class ViewHolderSongs extends RecyclerView.ViewHolder {
        TextView tvSong;
        ImageView songView;

        public ViewHolderSongs(View itemView) {
            super(itemView);
            tvSong = itemView.findViewById(R.id.tvSong);
            songView = itemView.findViewById(R.id.ivSongItem);


        }
    }
}
