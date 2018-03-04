package com.rishabh.notestaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.addNote:
                Intent newNoteactivity = new Intent(this, NoteActivity.class);
                startActivity(newNoteactivity);
                return true;
            case R.id.help:
                Intent helpActicity = new Intent(this, HelpActivity.class);
                startActivity(helpActicity);
                return true;

        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        listView.setAdapter(null);

        ArrayList<Note> notes = Utilities.getAllSavedNotes(this);
        final NoteAdapter na;

        if (notes == null || notes.size() == 0) {
            Toast.makeText(this, "You have no saved notes ", Toast.LENGTH_SHORT).show();
        } else {

            na = new NoteAdapter(this, R.layout.item_note, notes);
            Collections.reverse(notes);
            listView.setAdapter(na);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String filename = ((Note) listView.getItemAtPosition(position)).getmDateTime()
                            + Utilities.FILE_EXTENSION;

                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", filename);

                    startActivity(viewNoteIntent);
                }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete This Note ?")
                            .setMessage("Are you sure to delete this?")
                            .setIcon(android.R.drawable.ic_delete)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Utilities.deleteNote(getApplicationContext(), ((Note) listView.getItemAtPosition(position))
                                            .getmDateTime() + Utilities.FILE_EXTENSION);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);

                                    Toast.makeText(MainActivity.this, "The note was Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .setCancelable(false);
                    dialog.show();
                    //na.notifyDataSetChanged();
                    return true;
                }

            });

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);


    }



}
