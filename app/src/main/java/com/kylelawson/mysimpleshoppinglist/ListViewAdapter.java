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
    private static Calculate calculate;

    //Uses viewholder method in order to customize adapter
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private String currentPrice = "";
        private String currentQuantity = "";
        public EditText nameTextView;
        public EditText priceEditView;
        public EditText quantityPicker;
        public int quantity = 0;
        public double price = 0.00;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (EditText) itemView.findViewById(R.id.list_item_name);

            quantityPicker = (EditText) itemView.findViewById(R.id.quantity_picker);
            quantityPicker.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    quantityPicker.removeTextChangedListener(this);

                    if(!editable.toString().equals(currentQuantity)){
                        quantity = Integer.parseInt(String.valueOf(editable));
                    }

                    calculate.calculatePrice();
                    quantityPicker.addTextChangedListener(this);

                }
            });

            priceEditView = (EditText) itemView.findViewById(R.id.list_item_price);
            priceEditView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public synchronized void afterTextChanged(Editable editable) {
                    if(!editable.toString().equals(currentPrice)){
                        priceEditView.removeTextChangedListener(this);

                        String digits = editable.toString().replaceAll("\\D", "");
                        NumberFormat nf = NumberFormat.getCurrencyInstance();
                        try{
                            String formatted = nf.format(Double.parseDouble(digits)/100);
                            editable.replace(0, editable.length(), formatted);
                            price = Double.valueOf(String.valueOf(editable));
                        } catch (NumberFormatException nfe) {
                            editable.clear();
                        }

                        calculate.calculatePrice();
                        priceEditView.addTextChangedListener(this);
                    }
                }
            });

        }


    }

    public ListViewAdapter(Context context, ArrayList<ShoppingListItem> item, Calculate calculate){
        listItem = item;
        this.mContext = context;
        ListViewAdapter.calculate = calculate;

    }

    private Context getContext(){
        return mContext;
    }

    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewAdapter.ViewHolder viewHolder, int position){
        ShoppingListItem shoppingListItem = listItem.get(position);

        viewHolder.nameTextView.setText(shoppingListItem.name);

        viewHolder.priceEditView.setText(shoppingListItem.price.toString());

        viewHolder.quantityPicker.setText(shoppingListItem.quantity);
    }

    @Override
    public int getItemCount(){
        return listItem.size();
    }

    public interface Calculate{
        void calculatePrice();
    }



}