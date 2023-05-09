package com.example.onair;

import android.R.color;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PMAdapter extends RecyclerView.Adapter<PMAdapter.ViewHolder> {
    ArrayList<PMItem> items = new ArrayList<PMItem>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RecyclerView recyclerView;
        TextView textView, textView2, textView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }

        public void setItem(PMItem item) {
            textView.setText(item.sidoName + " " + item.stationName);
            int val;
            if (item.khaiValue == null || item.khaiValue.equals("-")) {
                val = -1;
                imageView.setImageResource(R.drawable.normal);
                textView2.setText("데이터 없음");
            } else {
                val = Integer.parseInt(item.khaiValue);
                if (val >= 0 && val < 51) {
                    imageView.setImageResource(R.drawable.good);
                    textView2.setText("좋음");
                } else if (val >= 51 && val < 101) {
                    imageView.setImageResource(R.drawable.normal);
                    textView2.setText("보통");
                } else if (val >= 101 && val < 251) {
                    imageView.setImageResource(R.drawable.bad);
                    textView2.setText("나쁨");
                } else if (val >= 251) {
                    imageView.setImageResource(R.drawable.worst);
                    textView2.setText("매우 나쁨");
                }
            }
            textView3.setText("통합대기환경지수: " + val);
        }
    }

    public void updateList(ArrayList<PMItem> list) {
        items.clear();
        items.addAll(list);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PMItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(PMItem item){
        items.add(item);
    }

    public void setItems(ArrayList<PMItem> items){
        this.items = items;
    }

    public PMItem getItem(int position) {
        return items.get(position);
    }
}
