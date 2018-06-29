package gplay.marlonaguirre.ml.gplay.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import gplay.marlonaguirre.ml.gplay.R;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.ViewHolderFolders>
implements View.OnClickListener{
    private View.OnClickListener listener;
    ArrayList<File> folders_list;

    public FoldersAdapter(ArrayList<File> folders_list) {
        this.folders_list = folders_list;
    }

    @NonNull
    @Override
    public FoldersAdapter.ViewHolderFolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_item_list,null,false);
        view.setOnClickListener(this);
        return new ViewHolderFolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoldersAdapter.ViewHolderFolders holder, int position) {
        holder.tvFolderName.setText(folders_list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return folders_list.size();
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

    public class ViewHolderFolders extends RecyclerView.ViewHolder {

        TextView tvFolderName;

        public ViewHolderFolders(View itemView) {
            super(itemView);
            tvFolderName = itemView.findViewById(R.id.tvFolder);
        }
    }
}
