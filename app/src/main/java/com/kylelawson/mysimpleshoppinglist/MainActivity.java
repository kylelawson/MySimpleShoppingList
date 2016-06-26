package com.kylelawson.mysimpleshoppinglist;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView shoppingList;
    ListViewAdapter adapter;
    ArrayList<ShoppingListItem> shoppingListNameArray;

    FloatingActionButton addItemFAB;
    TextView totalPriceView;
    ImageButton recalculateButton;
    NumberFormat format;

    Double totalPrice = 0.00;

    SharedPreferences listSize;
    SharedPreferences listName;
    SharedPreferences listPrice;
    SharedPreferences listQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences instantiation
        listSize = getSharedPreferences("SIZE", MODE_PRIVATE);
        listName = getSharedPreferences("NAME", MODE_PRIVATE);
        listPrice = getSharedPreferences("PRICE", MODE_PRIVATE);
        listQuantity = getSharedPreferences("QUANTITY", MODE_PRIVATE);

        //Total price text view
        totalPriceView = (TextView) findViewById(R.id.total_price_view);
        format = NumberFormat.getCurrencyInstance();
        totalPriceView.setText(String.valueOf(format.format(totalPrice)));

        //For the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().show();


        //Listview
        shoppingListNameArray = ShoppingListItem.createShoppingItem(0);

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

                ShoppingListItem tempItem = new ShoppingListItem("", 0.00, 0);
                shoppingListNameArray.add(tempItem);

                SharedPreferences.Editor editor = listName.edit();
                editor.putString("" + (shoppingListNameArray.size() - 1), "");
                editor.apply();

                editor = listSize.edit();
                editor.putInt("0", shoppingListNameArray.size());
                editor.apply();

                editor = listPrice.edit();
                editor.putString("" + (shoppingListNameArray.size() - 1), "0.00");
                editor.apply();

                editor = listQuantity.edit();
                editor.putInt("" + (shoppingListNameArray.size() - 1), 0);
                editor.apply();

                adapter.notifyItemInserted(shoppingListNameArray.size());
            }
        });

        //Recalculate price button
        recalculateButton = (ImageButton) findViewById(R.id.recalculate_button);
        recalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalPriceCalculation();
            }
        });

    }

    //Adds the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //Adds actions to menu item selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings:

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.about)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setItems(R.array.about_credit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                return true;

            case R.id.clear_list:

                //Clear the object array and the recycler view
                shoppingListNameArray.clear();
                adapter.notifyDataSetChanged();
                totalPriceCalculation();

                //Clear the persistence data
                SharedPreferences.Editor editor = listQuantity.edit();
                editor.clear();
                editor.apply();

                editor = listPrice.edit();
                editor.clear();
                editor.apply();

                editor = listSize.edit();
                editor.clear();
                editor.apply();

                editor = listName.edit();
                editor.clear();
                editor.apply();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //This sets up the recycler view swipe
    private void setupItemTouchHelper(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                shoppingListNameArray.remove(swipedPosition);
                adapter.notifyItemRemoved(swipedPosition);
                totalPriceCalculation();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(shoppingList);
    }

    public void totalPriceCalculation(){
        double tempPrice;
        int tempQuantity;
        totalPrice = 0.00;

        for(int i = 0; i < shoppingListNameArray.size();i++){

            tempPrice = shoppingListNameArray.get(i).price;
            tempQuantity = shoppingListNameArray.get(i).quantity;

            totalPrice += (tempPrice * tempQuantity);
        }

        totalPriceView.setText(String.valueOf(format.format(totalPrice)));
    }
}
