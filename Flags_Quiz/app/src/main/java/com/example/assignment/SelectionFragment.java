package com.example.assignment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/*/
This activity is used for any scrollable activity, in this case Flag selection or question selection.
 */
public class SelectionFragment extends Fragment {

    //To allow maintaining this when rotating fragment, doesn't keep it for rotating of screen
    int currentSpanCount = 2;
    private boolean horizontal = false;

    //Activity using this fragment must have a scrollable selection and implement methods relating to that selection
    private ScrollableSelection activity;
    private RecyclerView.Adapter adapter;
    private RecyclerView rv;

    private class FlagView extends RecyclerView.ViewHolder {
        private ImageView flagView;
        private Flag flag;

        public FlagView(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.flag_viewholder, parent, false));

            updateLayouts();

            flagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onClick(flag);
                }
            });
        }

        public void bind(Flag flag) {
            this.flag = flag;
            flagView.setImageResource(flag.getDrawable());
        }

        private void updateLayouts() {
            //Updating layout parameters based on if its horizontal or vertical scrolling.
            // Uses values in the integer.xml file, which is different for landscape mode and for tablets.
            ConstraintLayout layout = itemView.findViewById(R.id.ConstraintLayout);
            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();

            flagView = itemView.findViewById(R.id.flag_image);
            ViewGroup.LayoutParams flagParams = flagView.getLayoutParams();

            /*
                0   --> match constraint
                -1  --> match parent
                -2  --> wrap content
             */
            if (horizontal) {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

                //Value is stored as dp, needs to be converted to pixels.
                flagParams.width = dpToPx(getResources().getInteger(R.integer.flag_dimension));
                flagParams.height = 0;

            } else {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                //Value is stored as dp, needs to be converted to pixels.
                flagParams.height = dpToPx(getResources().getInteger(R.integer.flag_dimension));
                flagParams.width = 0;
            }
            layout.setLayoutParams(layoutParams);
            flagView.setLayoutParams(flagParams);

        }
    }

    private class FlagAdapter extends RecyclerView.Adapter<FlagView> {
        private List<Flag> flagList;

        public FlagAdapter(List<Flag> flags) {
            flagList = flags;
        }

        @Override
        public int getItemCount() {
            return flagList.size();
        }

        @NonNull
        @Override
        public FlagView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(activity);
            return new FlagView(li, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FlagView holder, int position) {
            holder.bind(flagList.get(position));
        }
    }

    private class QuestionView extends RecyclerView.ViewHolder {
        private Question question;
        private TextView header, points, penalty;

        //Use this for an onClick listener, to cover the whole view
        private ConstraintLayout layout;

        public QuestionView(LayoutInflater li, ViewGroup parent, int layoutFile) {
            super(li.inflate(layoutFile, parent, false));

            header = itemView.findViewById(R.id.header);
            points = itemView.findViewById(R.id.points);
            penalty = itemView.findViewById(R.id.penalty);
            layout = itemView.findViewById(R.id.question_layout);

            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();

            //Update layout based on if its horizontal or vertically scrolling.
            // Uses values in the integer.xml file, which is different for landscape mode and for tablets.
            if (horizontal) {
                //one is -1, i.e. match parent, two is -2, i.e. wrapContent
                layoutParams.width = getResources().getInteger(R.integer.question_dimension_two);
                layoutParams.height = dpToPx(getResources().getInteger(R.integer.question_dimension_one));
            } else {
                //one is -1, i.e. match parent, two is -2, i.e. wrapContent
                layoutParams.width = dpToPx(getResources().getInteger(R.integer.question_dimension_one));
                layoutParams.height = getResources().getInteger(R.integer.question_dimension_two);
            }
            layout.setLayoutParams(layoutParams);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onClick(question);
                }
            });
        }

        public void bind(Question question) {
            this.question = question;

            if (question.isAnswered()) {
                layout.setClickable(false);
                layout.setBackgroundResource(R.drawable.one_rectangle_filled);
            }

            points.setText("Points: " + question.getPoints());
            penalty.setText("Penalty: " + question.getPenalty());
            if (question.isSpecial()) {
                header.setText(question.getLabel() + " (Special)");
            } else {
                header.setText(question.getLabel());
            }

        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionView> {
        private List<Question> questionList;

        public QuestionAdapter(List<Question> questions) {
            ViewGroup.LayoutParams rvParams = rv.getLayoutParams();
            if (horizontal) {
                rvParams.width = 0;
                rvParams.height = -2;
            } else {
                rvParams.width = -2;
                rvParams.height = 0;
            }
            this.questionList = questions;
        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }

        //These 2 methods need to be defined to stop weird duplication glitchs in the recyclerView.
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @NonNull
        @Override
        public QuestionView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(activity);
            int layoutFile;
            if (horizontal) {
                layoutFile = R.layout.question_viewholder;
            } else {
                //This file is the same as the standard for portrait, the view for vertical scrolling
                // landscapeView will be changed to use same view as portrait so that it can fit on
                // the screen with 3 columns
                layoutFile = R.layout.question_viewholder_alternative;
            }
            return new QuestionView(li, parent, layoutFile);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionView holder, int position) {
            holder.bind(questionList.get(position));
        }
    }


    public SelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (ScrollableSelection) getActivity();

        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        rv = view.findViewById(R.id.recycler_view);

        rv.setLayoutManager(new GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false));

        setAdapter();

        return view;
    }

    private void setAdapter() {
        String scrollItem = activity.getScrollType();

        if (scrollItem.equals("flag")) {
            adapter = (RecyclerView.Adapter) new FlagAdapter((List<Flag>) (Object) activity.getData());

            //Lots of flags have white, this grey background makes them clearer to differentiate.
            rv.setBackgroundColor(Color.parseColor("#d3d3d3"));
        } else {
            adapter = (RecyclerView.Adapter) new QuestionAdapter((List<Question>) (Object) activity.getData());
        }
        rv.setAdapter(adapter);
    }

    private int dpToPx(int dp) {
        //dp less than 0 has special meaning, e.g. match_Parent, want to preserve this meaning
        if (dp >= 0) {
            dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        }
        return dp;
    }

    //This will update the recycler view to reflect any changes. Useful when we don't know which
    // position has been changed, or if all items have been changed
    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }

    //Re-create the recycler view to match the new orientation
    public void rotate(boolean horizontal) {
        this.horizontal = horizontal;
        if (horizontal) {
            rv.setLayoutManager(new GridLayoutManager(activity, currentSpanCount, GridLayoutManager.HORIZONTAL, false));
        } else {
            rv.setLayoutManager(new GridLayoutManager(activity, currentSpanCount, GridLayoutManager.VERTICAL, false));
        }
        setAdapter();
    }

    //re-create the recycler view to reflect the new span count.
    public void changeSpanCount(int newCount) {
        currentSpanCount = newCount;
        if (horizontal) {
            rv.setLayoutManager(new GridLayoutManager(activity, newCount, GridLayoutManager.HORIZONTAL, false));
        } else {
            rv.setLayoutManager(new GridLayoutManager(activity, newCount, GridLayoutManager.VERTICAL, false));
        }
        setAdapter();
    }

}