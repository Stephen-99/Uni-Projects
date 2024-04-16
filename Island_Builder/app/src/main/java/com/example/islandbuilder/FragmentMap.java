package com.example.islandbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.content.ContentValues.TAG;


public class FragmentMap extends Fragment {

    public static final int DETAILS_ACTIVITY = 15;

    private MapActivity main;
    private FragmentSelector selector;
    private FragmentDisplay display;

    private GameData gameData;
    private MapData map;
    private MapAdapter adapter;
    private Settings settings;
    private int height, width;

    private class GridCell extends RecyclerView.ViewHolder {

        private ImageView tl, tr, bl, br, full;     //top left etc.
        private MapElement element;

        public GridCell(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.grid_cell, parent, false));

            int size = parent.getMeasuredHeight() / height + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;

            tl = (ImageView) itemView.findViewById(R.id.topLeft);
            tr = (ImageView) itemView.findViewById(R.id.topRight);
            bl = (ImageView) itemView.findViewById(R.id.bottomLeft);
            br = (ImageView) itemView.findViewById(R.id.bottomRight);
            full = (ImageView) itemView.findViewById(R.id.fullCell);

            tl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GridCell.this.onClick();
                }
            });
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GridCell.this.onClick();
                }
            });
            bl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GridCell.this.onClick();
                }
            });
            br.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GridCell.this.onClick();
                }
            });
            full.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GridCell.this.onClick();
                }
            });

        }

        public void bind(MapElement element) {
            this.element = element;
            tl.setImageResource(element.getNorthWest());
            tr.setImageResource(element.getNorthEast());
            bl.setImageResource(element.getSouthWest());
            br.setImageResource(element.getSouthEast());

            if (element.getStructure() != null) {
                //Check for user-defined image.
                if (element.getImage() != null) {
                    full.setImageBitmap(element.getImage());
                } else {
                    full.setImageResource(element.getStructure().getDrawableId());
                }
            }
            else
            {
                full.setImageAlpha(android.R.attr.activatedBackgroundIndicator);
                //full.setImageResource(android.R.attr.activatedBackgroundIndicator);
            }
        }

        public void onClick() {
            Structure s = selector.getCurSelection();
            int colLoc = this.getLayoutPosition() / height;
            int rowLoc = this.getLayoutPosition() - height *  (colLoc);

            if(s != null)
            {
                if (element.isBuildable()) {
                    String errorMsg = null;
                    if (s.isRoad()) {
                        if (!gameData.transaction(-settings.getRoadCost())) {
                            errorMsg = "You don't have enough money to build that!";
                        }
                    } else if (s.isCommercial()) {
                        if (!nextToRoad(rowLoc, colLoc)) {
                            errorMsg = "Commercial buildings must be next to a road";
                        } else if (!gameData.transaction(-settings.getCommercialCost())) {
                            errorMsg = "You don't have enough money to build that!";
                        } else {
                            gameData.increaseNumCommercial(1);
                        }
                    } else if (s.isHouse()) {
                        if (!nextToRoad(rowLoc, colLoc)) {
                            errorMsg = "Houses must be next to a road";
                        } else if (!gameData.transaction(-settings.getHouseCost())) {
                            errorMsg = "You don't have enough money to build that!";
                        } else {
                            gameData.increaseNumResidential(1);
                        }
                    }
                    if (errorMsg == null) {
                        //Set the structure
                        element.setStructure(s);

                        //Notify adapter and display structure
                        adapter.notifyItemChanged(GridCell.this.getAbsoluteAdapterPosition());

                        //Set system as owner of the mapElement.
                        map.get(rowLoc, colLoc).setOwnerName("System");


                        //update display
                        display.updateView();
                    } else {
                        gameData.displayPopUp(errorMsg, main);
                    }
                }
                //reset structure
                selector.setCurSelection(null);
            }
            //demolish
            else if (display.isDemolish() && element.getStructure() != null)
            {
                Structure structure = element.getStructure();
                if (structure.isHouse()) {
                    gameData.increaseNumResidential(-1);
                } else if (structure.isCommercial()) {
                    gameData.increaseNumCommercial(-1);
                }

                if (structure.isRoad() && dependantRoad(rowLoc, colLoc)) {
                    GameData.displayPopUp("Cannot demolish road, structure depends on it.", main);
                } else {
                    //No selected structure, remove existing structure
                    element.removeStructure();
                    adapter.notifyItemChanged(this.getAbsoluteAdapterPosition());

                    map.get(rowLoc, colLoc).setOwnerName("Player");

                    //update display
                    display.updateView();
                }
            }
            //show structure details
            else if (display.isDetail() && element.getStructure() != null) {
                Intent intent = new Intent(main, DetailsActivity.class);
                intent.putExtra("row", rowLoc);
                intent.putExtra("col", colLoc);

                startActivityForResult(intent, DETAILS_ACTIVITY);

            }
            //If they accidentally click demolish or details, they can click an empty space to reset.
            display.setDemolish(false);
            display.setDetail(false);
        }
    }

    private class MapAdapter extends RecyclerView.Adapter<GridCell> {
        private MapData map;

        public MapAdapter(MapData map) {
            this.map = map;
        }

        @Override
        public int getItemCount() {
            return height * width;
        }

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
        public GridCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from((main));

            return new GridCell(li, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull GridCell holder, int position) {
            int row = position % height;
            int col = position / height;
            holder.bind(map.get(row, col));
        }
    }

    public FragmentMap() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, true);
        main = (MapActivity) getActivity();

        selector = main.getSelector();
        display = main.getDisplayPanel();

        gameData = GameData.getInstance(main);
        gameData.updateMap();
        map = gameData.getMap();
        adapter = new MapAdapter(map);
        settings = gameData.getSettings();
        height = settings.getMapHeight();
        width = settings.getMapWidth();

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.mapRecyclerView);

        //setting layout for recycler view as grid layout
        rv.setLayoutManager(new GridLayoutManager(main, height, GridLayoutManager.HORIZONTAL, false));


        rv.setAdapter(adapter);

        return view;
    }

    public boolean nextToRoad(int row, int col)
    {
        return numAdjacentRoads(row, col) > 0;
    }

    public int numAdjacentRoads(int row, int col)
    {
        int numRoads = 0;

        //ii+=2 don't need to check if current structure is a road.
        for (int ii = row -  1; ii <= row + 1; ii+=2) {
            //Checking that it isn't outside of the grid
            if (ii >= 0 && ii < height) {
                Structure s = map.get(ii, col).getStructure();
                if (s != null) {
                     if (s.isRoad()) {
                         numRoads++;
                     }
                }
            }
        }

        for (int ii = col -  1; ii <= col + 1; ii+=2) {
            //Checking that it isn't outside of the grid
            if (ii >= 0 && ii < width) {
                Structure s = map.get(row, ii).getStructure();
                if (s != null) {
                    if (s.isRoad()) {
                        numRoads++;
                    }
                }
            }
        }
        return numRoads;
    }

    public boolean dependantRoad(int row, int col)
    {
        boolean dependant = false;

        int ii = row - 1;
        while (!dependant && ii <= row+1) {
            //Checking that it isn't outside of the grid
            if (ii >= 0 && ii < height) {
                Structure s = map.get(ii, col).getStructure();
                if (s != null) {
                    if (s.isCommercial() || s.isHouse()) {
                        if (numAdjacentRoads(ii, col) < 2) {
                            dependant = true;
                        }
                    }
                }
            }
            ii += 2;
        }

        ii = col - 1;
        while (!dependant && ii <= col+1) {
            //Checking that it isn't outside of the grid
            if (ii >= 0 && ii < height) {
                Structure s = map.get(row, ii).getStructure();
                if (s != null) {
                    if (s.isCommercial() || s.isHouse()) {
                        if (numAdjacentRoads(row, ii) < 2) {
                            dependant = true;
                        }
                    }
                }
            }
            ii += 2;
        }
        return dependant;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Result_ok means camera was used
        if (requestCode == DETAILS_ACTIVITY && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra("row") && data.hasExtra("col")) {
                int row = data.getIntExtra("row", 0);
                int col = data.getIntExtra("col", 0);
                adapter.notifyItemChanged(height * col + row);
            }
        }
    }
}
