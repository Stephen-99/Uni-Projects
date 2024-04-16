package com.example.assignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
This fragment works in conjunction with scrollable selection Fragments to change their layout.
 */
public class LayoutSelectorFragment extends Fragment {

    private ScrollableSelection activity;

    //Represents whether the layout is horizontally scrolling or not
    private boolean horizontal;
    private ImageView oneElement, twoElements, threeElements, arrow;

    public LayoutSelectorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_selector, container, false);

        activity = (ScrollableSelection) getActivity();

        oneElement = view.findViewById(R.id.one_element);
        twoElements = view.findViewById(R.id.two_elements);
        threeElements = view.findViewById(R.id.three_elements);
        arrow = view.findViewById(R.id.scroll_arrow);

        oneElement.setImageResource(R.drawable.one_rect);
        twoElements.setImageResource(R.drawable.two_rect_hoz);
        threeElements.setImageResource(R.drawable.three_rect_hoz);
        arrow.setImageResource(R.drawable.arrow_hoz);

        horizontal = false;

        //On click listeners
        {
            //Arrow governs any rotation
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (horizontal) {
                        twoElements.setImageResource(R.drawable.two_rect_hoz);
                        threeElements.setImageResource(R.drawable.three_rect_hoz);
                        arrow.setImageResource(R.drawable.arrow_hoz);
                        horizontal = false;
                    } else {
                        twoElements.setImageResource(R.drawable.two_rect_vert);
                        threeElements.setImageResource(R.drawable.three_rect_vert);
                        arrow.setImageResource(R.drawable.arrow_vert);
                        horizontal = true;

                    }
                    activity.rotate(horizontal);
                }
            });
        }

        {
            oneElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.changeItemSpanCount(1);
                }
            });
        }

        {
            twoElements.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.changeItemSpanCount(2);
                }
            });
        }

        {
        threeElements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.changeItemSpanCount(3);
            }
        });
        }



        return view;
    }
}