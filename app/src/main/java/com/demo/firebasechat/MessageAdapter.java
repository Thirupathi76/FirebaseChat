package com.demo.firebasechat;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messages = new ArrayList<>();
    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());

    public void add(Message message) {
        messages.add(message);
        notifyItemInserted(messages.lastIndexOf(message));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_message, null);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MessageViewHolder) holder).bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static String getAudioTime(long time) {
        time *= 1000;
        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return timeFormatter.format(new Date(time));
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView play_icon, pause_icon;

        public MessageViewHolder(View view) {
            super(view);
            text = itemView.findViewById(R.id.textView);
            play_icon = itemView.findViewById(R.id.play_icon);
            pause_icon = itemView.findViewById(R.id.pause_icon);

        }

        public void bind(final Message message) {
            if (message.type == Message.TYPE_AUDIO) {
                play_icon.setVisibility(View.VISIBLE);
                play_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MediaPlayer mPlayer = new MediaPlayer();
                        Log.d("instartPlaying",message.audioPath);
                        try {
                            mPlayer.setDataSource(message.audioPath);
                            mPlayer.prepare();
                            mPlayer.start();
                        } catch (IOException e) {
                            Log.e("LOG_TAG", "prepare() failed");
                        }
                    }
                });
//                text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mic_small, 0, 0, 0);
                text.setText(String.valueOf(getAudioTime(message.time)));

            } else if (message.type == Message.TYPE_TEXT) {
                play_icon.setVisibility(View.GONE);
//                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text.setText(message.text);

            } else {
//                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text.setText("");
            }
        }
    }
}