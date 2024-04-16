package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/*
This abstract class, allows us to re-use our selection fragment with multiple selection types
 */
public abstract class ScrollableSelection extends AppCompatActivity  {
    public abstract String getScrollType();
    public abstract List<Object> getData();
    public abstract void onClick(Object itemClicked);
    public abstract void rotate(boolean horizontal);
    public abstract void changeItemSpanCount(int newSpanCount);
}
