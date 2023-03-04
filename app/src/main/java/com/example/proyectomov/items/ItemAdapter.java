package com.example.proyectomov.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectomov.R;
import com.example.proyectomov.Realm.MovieRealm;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private final ArrayList<MovieRealm> movieRealm;

    public ItemAdapter(OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener, ArrayList<MovieRealm> movieRealm) {
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        this.movieRealm = movieRealm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_list,parent,false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieRealm mov = movieRealm.get(position);
        holder.bind(mov);
        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onItemClick(mov);
        });

        holder.itemView.setOnLongClickListener(view -> {
            onItemLongClickListener.onItemLongClick(mov);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return movieRealm.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(MovieRealm item);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(MovieRealm item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView maker;
        private TextView description;
        private ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_tv);
            maker = (TextView) itemView.findViewById(R.id.maker_tv);
            description = (TextView) itemView.findViewById(R.id.description_tv);
            photo = (ImageView) itemView.findViewById(R.id.image_view);
        }

        public void bind(MovieRealm item) {
            title.setText(item.getTitulo());
            maker.setText(item.getDirector());
            description.setText(item.getGenero());
            photo.setImageBitmap(item.getImg());
        }
    }
}
