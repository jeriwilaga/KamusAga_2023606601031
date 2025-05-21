package com.example.kamusaga_2023606601031;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KamusAdapter extends RecyclerView.Adapter<KamusAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(KamusEntry entry);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(KamusEntry entry, int position);
    }

    private final List<KamusEntry> entryList;
    private final OnItemClickListener clickListener;
    private final OnItemLongClickListener longClickListener;

    public KamusAdapter(List<KamusEntry> entryList, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        this.entryList = entryList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }


    @NonNull
    @Override
    public KamusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kamus_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull KamusAdapter.ViewHolder holder, int position) {
        KamusEntry entry = entryList.get(position);
        holder.txtIndo.setText("Indonesia: " + entry.getBahasaIndo());
        holder.txtInggris.setText("Inggris: " + entry.getBahasaInggris());
        holder.txtKorea.setText("Korea: " + entry.getBahasaKorea());

        // Klik biasa
        holder.itemView.setOnClickListener(view -> clickListener.onItemClick(entry));

        holder.itemView.setOnLongClickListener(view -> {
            longClickListener.onItemLongClick(entry, holder.getAdapterPosition());
            return true;
        });


        // Klik lama untuk menghapus dengan konfirmasi
        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Hapus Data")
                    .setMessage("Yakin ingin menghapus entri ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        entryList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, entryList.size());
                    })
                    .setNegativeButton("Batal", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIndo, txtInggris, txtKorea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIndo = itemView.findViewById(R.id.txtIndo);
            txtInggris = itemView.findViewById(R.id.txtInggris);
            txtKorea = itemView.findViewById(R.id.txtKorea);
        }
    }
}