package com.example.islandbuilder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class FragmentSelector extends Fragment {
    private Structure curSelection = null;

    private class ListSelection extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView description;
        private Structure structure;

        private ListSelection(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.list_selection, parent, false));
            //ViewGroup.LayoutParams lp = itemView.getLayoutParams();


            img = (ImageView) itemView.findViewById(R.id.structImg);
            description = (TextView) itemView.findViewById(R.id.structName);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListSelection.this.onClick();
                }
            });
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListSelection.this.onClick();
                }
            });
        }

        public void bind(Structure structure)
        {
            this.structure = structure;
            img.setImageResource(structure.getDrawableId());
            description.setText(structure.getLabel());
        }

        public void onClick()
        {
            setCurSelection(structure);
        }


    }

    private class SelectorAdapter extends RecyclerView.Adapter<ListSelection>
    {
        private StructureData sd;
        public SelectorAdapter(StructureData sd) {
            this.sd = sd;
        }

        @Override
        public int getItemCount() {
            return sd.size();
        }

        @NonNull
        @Override
        public ListSelection onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater li = LayoutInflater.from(getActivity());
            return new ListSelection(li, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ListSelection holder, int position) {
            holder.bind(sd.get(position));
        }
    }

    public FragmentSelector() {
        // Required empty public constructor
    }

    public Structure getCurSelection() {
        return curSelection;
    }

    public void setCurSelection(Structure structure)
    {
        curSelection = structure;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selector, container, false);
        
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.selectorRecyclerView);

        //setting layout for recycler view as linear
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        //rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, true));

        StructureData sd = StructureData.get();

        SelectorAdapter adapter = new SelectorAdapter(sd);

        rv.setAdapter(adapter);

        return view;
    }

}