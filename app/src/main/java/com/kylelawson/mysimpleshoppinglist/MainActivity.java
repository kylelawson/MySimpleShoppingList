package com.kylelawson.mysimpleshoppinglist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView shoppingList;
    ListViewAdapter adapter;
    ArrayList<ShoppingListItem> shoppingListNameArray;

    FloatingActionButton addItemFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Listview
        shoppingListNameArray = ShoppingListItem.createShoppingItem(1);

        adapter = new ListViewAdapter(this, shoppingListNameArray);
        shoppingList = (RecyclerView) findViewById(R.id.parent_list);
        shoppingList.setAdapter(adapter);
        shoppingList.setLayoutManager(new LinearLayoutManager(this));

        //Configure the recycler view
        shoppingList.setHasFixedSize(true);
        setupItemTouchHelper();

        //Floating action button for adding an item
        addItemFAB = (FloatingActionButton) findViewById(R.id.add_item_fab);
        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingListNameArray.add(new ShoppingListItem("",0.00));
                adapter.notifyItemInserted(shoppingListNameArray.size());
            }
        });
    }

    private void setupItemTouchHelper(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                adapter.notifyItemRemoved(swipedPosition);
                shoppingListNameArray.remove(swipedPosition);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(shoppingList);
    }

}
