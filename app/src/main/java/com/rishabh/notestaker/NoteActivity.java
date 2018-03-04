package com.rishabh.notestaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    EditText titleEditText ;
    EditText conetnteditText ;
    TextToSpeech t1;
    private String mNotefilename ;
    private Note mlodadedNote;

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_note_new , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){

            case R.id.deleteNote:
                DeleteNotes();
                return true;
            case R.id.speakNote :
                SpeakNote();
                return true;
            case R.id.whatsapp:
                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mlodadedNote.getmContent());
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            case R.id.gmail:
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, mlodadedNote.getmTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, mlodadedNote.getmContent());
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            case R.id.message:
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"));
                    sendIntent.putExtra("sms_body", mlodadedNote.getmContent());
                    startActivity(sendIntent);
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to open message !!", Toast.LENGTH_SHORT).show();
                    return false;
                }

            case R.id.addreminder:
                try {
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("title", mlodadedNote.getmTitle());
                    intent.putExtra("description", mlodadedNote.getmContent());
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to open Calender !!", Toast.LENGTH_SHORT).show();
                    return false;
                }

            default:
                return false;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        titleEditText = (EditText)findViewById(R.id.titleEditText);
        conetnteditText = (EditText)findViewById(R.id.contentEditText);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


        mNotefilename = getIntent().getStringExtra("NOTE_FILE");

        if (mNotefilename != null && !mNotefilename.isEmpty()){
            mlodadedNote = Utilities.getNoteByName(this,mNotefilename);

            if (mlodadedNote != null){
                titleEditText.setText(mlodadedNote.getmTitle());
                conetnteditText.setText(mlodadedNote.getmContent());
            }
        }



    }

    private void DeleteNotes(){
        if (mlodadedNote == null) {
            finish();
        }else {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Delete This Note ?")
                    .setMessage("Are you sure to delete this?")
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.deleteNote(getApplicationContext() , mlodadedNote.getmDateTime() + Utilities.FILE_EXTENSION);
                            finish();
                            Toast.makeText(NoteActivity.this, "The note was Deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No" , null )
                    .setCancelable(false);
            dialog.show();

            
        }
    }

    public void SpeakNote () {
        if (mlodadedNote == null ){
            Toast.makeText(this, "Please Save the note first ", Toast.LENGTH_SHORT).show();
            return;
        }
        String toSpeak = conetnteditText.getText().toString();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

    }

    private void SaveNotes (){
        Note note;
        if (titleEditText.getText().toString().trim().isEmpty() || conetnteditText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please Enter Title and Content", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mlodadedNote == null) {
            note = new Note(System.currentTimeMillis(), titleEditText.getText().toString(), conetnteditText.getText().toString());
        }else {
            note = new Note(mlodadedNote.getmDateTime(), titleEditText.getText().toString(), conetnteditText.getText().toString());

        }
        if (Utilities.SaveNote(this,note)){
            Toast.makeText(this, "Your note is saved !!", Toast.LENGTH_SHORT).show();

        }else {

            Toast.makeText(this, "Cannot save note , please insure you have enough space in Your device", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        SaveNotes();
        super.onBackPressed();
    }
}
