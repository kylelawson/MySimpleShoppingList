package com.kylelawson.mysimpleshoppinglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.ArrayList;


/**
 * Created by Kyle on 6/20/2016.
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private ArrayList<ShoppingListItem> listItem;
    private Context mContext;

    private String currentPrice;
    private String currentQuantity;

    NumberFormat format = NumberFormat.getCurrencyInstance();

    //Uses viewholder method in order to customize adapter
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public EditText nameTextView;
        public EditText priceEditView;
        public EditText quantityPicker;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (EditText) itemView.findViewById(R.id.list_item_name);

            quantityPicker = (EditText) itemView.findViewById(R.id.quantity_picker);

            priceEditView = (EditText) itemView.findViewById(R.id.list_item_price);

        }

    }

    public ListViewAdapter(Context context, ArrayList<ShoppingListItem> item){

        listItem = item;
        this.mContext = context;

    }

    private Context getContext(){
        return mContext;
    }

    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        ViewHolder viewholder = new ViewHolder(itemView);

        //Makes sure the view holder doesn't get recycled ensuring the list remains intact
        viewholder.setIsRecyclable(false);
        return viewholder;

    }

    @Override
    public void onBindViewHolder(final ListViewAdapter.ViewHolder viewHolder, int position){
        final ShoppingListItem shoppingListItem = listItem.get(position);

        viewHolder.nameTextView.setText(shoppingListItem.getName());
        viewHolder.nameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                shoppingListItem.name = editable.toString();
            }
        });

        if(shoppingListItem.price == 0.00){
            viewHolder.priceEditView.setText("");
        }else {
            viewHolder.priceEditView.setText(format.format(shoppingListItem.price));
        }

        currentPrice = shoppingListItem.getPrice();
        viewHolder.priceEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(currentPrice)){
                    viewHolder.priceEditView.removeTextChangedListener(this);

                    String digits = editable.toString().replaceAll("\\D", "");
                    NumberFormat nf = NumberFormat.getCurrencyInstance();

                    try{

                        String formatted = nf.format(Double.parseDouble(digits)/100);
                        editable.replace(0, editable.length(), formatted);
                        shoppingListItem.price = Double.valueOf(String.valueOf(editable));

                    } catch (NumberFormatException nfe) {

                        editable.clear();

                    }

                    viewHolder.priceEditView.addTextChangedListener(this);

                }
            }
        });

        if(shoppingListItem.quantity == 0){

            viewHolder.quantityPicker.setText("");

        }else {

            viewHolder.quantityPicker.setText(shoppingListItem.getQuantity());

        }

        currentQuantity = shoppingListItem.getQuantity();
        viewHolder.quantityPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                viewHolder.quantityPicker.removeTextChangedListener(this);

                if(!editable.toString().equals(currentQuantity) && !editable.toString().equals("")){

                    shoppingListItem.quantity = Integer.valueOf(String.valueOf(editable));

                }else{

                    shoppingListItem.quantity = 0;
                }

                viewHolder.quantityPicker.addTextChangedListener(this);

            }
        });

    }

    @Override
    public int getItemCount(){
        return listItem.size();
    }

}