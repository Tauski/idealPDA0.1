package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Custom recycleview adapter to fit our notes, they will have title and content.
//By clicking note we will open it and enable modifications
//Using Adam Sinicki's tutorial
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    private List<String> mData;
    private LayoutInflater mInflater;
    private OnNoteListener mOnNoteListener;


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // data is passed into the constructor
    public NotesAdapter(Context context, List<String> data, OnNoteListener onNoteListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnNoteListener = onNoteListener;
        this.mData = data;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_list_row, parent, false);
        return new ViewHolder(view,mOnNoteListener );
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        OnNoteListener onNoteListener;

        ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvTitle);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public interface OnNoteListener{
        void onNoteClick(int position);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
