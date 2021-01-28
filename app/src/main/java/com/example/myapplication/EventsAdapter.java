package com.example.myapplication;

import android.content.Context;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Custom recycleview adapter to fit our events, they will have title and content.
//By clicking events we will open it and enable modifications
//Using Adam Sinicki's tutorial
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

    private List<String> mData;
    private LayoutInflater mInflater;
    private OnEventPressListener mOnEventPressListener;

    // data is passed into the constructor
    public EventsAdapter(Context context, List<String> data, OnEventPressListener onEventPressListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnEventPressListener = onEventPressListener;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_list_row, parent, false);
        return new ViewHolder(view,mOnEventPressListener );
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String event = mData.get(position);
        holder.myTextView.setText(event);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        OnEventPressListener onEventPressListener;

        ViewHolder(View itemView, OnEventPressListener onEventPressListener) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvTitle);
            this.onEventPressListener = onEventPressListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventPressListener.onEventPressClick(getAdapterPosition());
        }
    }

    public interface OnEventPressListener{
        void onEventPressClick(int position);
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}