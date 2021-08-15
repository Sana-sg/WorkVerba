package com.sana.timetable;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Model> notesList;
    DatabaseHelper databaseClass;
    CoordinatorLayout coordinatorLayout;
    Context thiscontext;
    TextView no_rcv,add_note,swipe;
    LottieAnimationView anim1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_second,container,false);
        setHasOptionsMenu(true);
        thiscontext=container.getContext();
        recyclerView = v.findViewById(R.id.recycler_view);
        fab = v.findViewById(R.id.fab);
        coordinatorLayout = v.findViewById(R.id.layout_main);
        no_rcv=v.findViewById(R.id.no_rcv);
        anim1=v.findViewById(R.id.anim1);
        add_note=v.findViewById(R.id.add_note);
        swipe=v.findViewById(R.id.swipe);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thiscontext, AddNotesActivity.class);
                startActivity(intent);
            }
        });

        notesList = new ArrayList<>();
        databaseClass = new DatabaseHelper(thiscontext);
        fetchAllNotesFromDatabase();


        recyclerView.setLayoutManager(new GridLayoutManager(thiscontext,2, GridLayoutManager.VERTICAL,false));
        adapter = new Adapter(thiscontext,notesList);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        final SwipeRefreshLayout pullToRequest= v.findViewById(R.id.refrsh);
        pullToRequest.setOnRefreshListener((new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notesList.clear();
                refreshData();
                adapter.notifyDataSetChanged();
                pullToRequest.setRefreshing(false);

            }
        }));
        return  v;
    }
    public void refreshData(){fetchAllNotesFromDatabase(); }
    void fetchAllNotesFromDatabase() {
        Cursor cursor = databaseClass.readAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(thiscontext, "No Data to show", Toast.LENGTH_LONG).show();
            no_rcv.setVisibility(View.VISIBLE);
            anim1.setVisibility(View.VISIBLE);
            add_note.setVisibility(View.VISIBLE);
            swipe.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    no_rcv.setVisibility(View.INVISIBLE);
                    anim1.setVisibility(View.INVISIBLE);
                    add_note.setVisibility(View.INVISIBLE);
                    swipe.setVisibility(View.INVISIBLE);
                }
            },10000);
        } else {
            while (cursor.moveToNext()) {
                notesList.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }

        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchbar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Notes Here");

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        };

        searchView.setOnQueryTextListener(listener);


        super.onCreateOptionsMenu(menu,inflater);
    }


    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Model item = adapter.getList().get(position);

            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                        }
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if (!(event == DISMISS_EVENT_ACTION)) {
                                DatabaseHelper db = new DatabaseHelper(thiscontext);
                                boolean result=db.deleteSingleItem(item.getId());
                                if (result == false ) {
                                    Toast.makeText(thiscontext, "Item Not Deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(thiscontext, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }
                    });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    };

}