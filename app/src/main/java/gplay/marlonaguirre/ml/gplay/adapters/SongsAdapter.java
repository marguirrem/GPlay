package gplay.marlonaguirre.ml.gplay.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gplay.marlonaguirre.ml.gplay.R;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolderSongs>{

    ArrayList<String> songs_list;

    public SongsAdapter(ArrayList<String> songs_list) {
        this.songs_list = songs_list;
    }

    @NonNull
    @Override
    public SongsAdapter.ViewHolderSongs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item_list,null,false);
        return new ViewHolderSongs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.ViewHolderSongs holder, int position) {
        holder.tvSong.setText( songs_list.get(position));
    }

    @Override
    public int getItemCount() {
        return songs_list.size();
    }

    public class ViewHolderSongs extends RecyclerView.ViewHolder {
        TextView tvSong;

        public ViewHolderSongs(View itemView) {
            super(itemView);
            tvSong = itemView.findViewById(R.id.tvSong);
        }
    }
}
