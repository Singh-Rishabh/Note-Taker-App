package com.rishabh.notestaker;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Rishabh on 25-06-2017.
 */

public class Note implements Serializable {

    private long mDateTime;
    private String mTitle;
    private  String mContent;

    public Note(long mDate, String mTitle, String mContent) {
        this.mDateTime = mDate;
        this.mTitle = mTitle;
        this.mContent = mContent;
    }

    public void setmDateTime(long mDate) {
        this.mDateTime = mDate;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public long getmDateTime() {
        return mDateTime;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public String dateTimeFormatted (Context context){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"
                , context.getResources().getConfiguration().locale);

        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(mDateTime));
    }
}
