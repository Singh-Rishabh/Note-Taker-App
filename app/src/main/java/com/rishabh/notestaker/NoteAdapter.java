package com.rishabh.notestaker;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NoteAdapter extends ArrayAdapter<Note> {

    public NoteAdapter(Context context, int resource,  ArrayList<Note> notes) {
        super(context, resource, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_note , null);
        }

        Note note = getItem(position);

        if (note != null){

            TextView title = (TextView)convertView.findViewById(R.id.list_note_title);
            TextView date = (TextView)convertView.findViewById(R.id.list_note_date);
            TextView content = (TextView)convertView.findViewById(R.id.list_note_content);

            title.setText(note.getmTitle());
            date.setText(note.dateTimeFormatted(getContext()));

            if (note.getmContent().length() > 15 ){
                content.setText(note.getmContent().substring(0,15));
            }
            else{

                content.setText(note.getmContent());
            }
        }
        return convertView ;
    }
}
