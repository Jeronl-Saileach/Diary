package com.example.dairyApplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smdiary.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Database.DatabaseHelper;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Context context;
    private Cursor cursor;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(long entryId);
    }

    public DiaryAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_diary, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            long entryId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ENTRY_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
            long dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
            String tags = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TAGS));

            holder.titleTextView.setText(title);
            holder.dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(dateMillis)));
            holder.contentTextView.setText(content.length() > 50 ? content.substring(0, 50) + "..." : content);
            holder.tagTextView.setText(tags);

            holder.itemView.setOnClickListener(v -> listener.onItemClick(entryId));

            switch (tags) {
                case "开心":
                    holder.tagTextView.setTextColor(Color.BLUE);
                    break;
                case "伤心":
                    holder.tagTextView.setTextColor(Color.GREEN);
                    break;
                case "无聊":
                    holder.tagTextView.setTextColor(Color.YELLOW);
                    break;
                case "烦躁":
                    holder.tagTextView.setTextColor(Color.RED);
                    break;
                case "劳累":
                    holder.tagTextView.setTextColor(Color.rgb(0, 255, 255));
                    break;
                case "有意义的一天":
                    holder.tagTextView.setTextColor(Color.rgb(255, 128, 0));
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView contentTextView;
        TextView tagTextView;

        public DiaryViewHolder (@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.diaryTitle);
            dateTextView = itemView.findViewById(R.id.diaryDate);
            contentTextView = itemView.findViewById(R.id.diaryContent);
            tagTextView = itemView.findViewById(R.id.entryTag);
        }
    }
}

