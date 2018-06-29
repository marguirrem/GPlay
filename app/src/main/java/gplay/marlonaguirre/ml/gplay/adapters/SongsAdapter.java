package gplay.marlonaguirre.ml.gplay.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import gplay.marlonaguirre.ml.gplay.R;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolderSongs>
        implements View.OnClickListener {
    private View.OnClickListener listener;

    ArrayList<File> songs_list;

    public SongsAdapter(ArrayList<File> songs_list) {
        this.songs_list = songs_list;
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
        holder.tvSong.setText( songs_list.get(position).getName());

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

        public ViewHolderSongs(View itemView) {
            super(itemView);
            tvSong = itemView.findViewById(R.id.tvSong);
        }
    }
}
