package com.example.myapplication;

import android.content.Context;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//Custom recycleview adapter to fit our events, they will be made according to eventmodel class.
//By clicking events we will open it and enable modifications
//Using Adam Sinicki's tutorial
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

    private ArrayList<EventModel> mData;
    private LayoutInflater mInflater;
    private OnEventClickListener mOnEventClickListener;


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public EventsAdapter(Context context, ArrayList<EventModel> data, OnEventClickListener onEventClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnEventClickListener = onEventClickListener;
        //this.mData = data;
        this.mData = data;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_list_row, parent, false);
        return new ViewHolder(view,mOnEventClickListener );
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //String event = mData.get(position);
        EventModel model = mData.get(position);
        holder.myTextView.setText(model.getEventName());
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // total number of rows
    @Override
    public int getItemCount() { return mData.size(); }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        OnEventClickListener onEventClickListener;

        ViewHolder(View itemView, OnEventClickListener onEventClickListener) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvEventTitle);
            this.onEventClickListener = onEventClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventClickListener.onEventClick(getAdapterPosition());
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public interface OnEventClickListener{
        void onEventClick(int position);
    }
}