package com.kylelawson.mysimpleshoppinglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.text.NumberFormat;
import java.util.List;



/**
 * Created by Kyle on 6/20/2016.
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private String current = "";
        public EditText nameTextView;
        public EditText priceEditView;
        public NumberPicker quantityPicker;
        public int quantity;
        public double price;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (EditText) itemView.findViewById(R.id.list_item_name);
            quantityPicker = (NumberPicker) itemView.findViewById(R.id.quantity_picker);

            quantityPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
                @Override
                public void onScrollStateChange(NumberPicker numberPicker, int i) {
                    quantity = i;
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
                    if(!editable.toString().equals(current)){
                        priceEditView.removeTextChangedListener(this);

                        String digits = editable.toString().replaceAll("\\D", "");
                        NumberFormat nf = NumberFormat.getCurrencyInstance();
                        try{
                            String formatted = nf.format(Double.parseDouble(digits)/100);
                            editable.replace(0, editable.length(), formatted);
                            price = Double.valueOf(editable.toString());
                        } catch (NumberFormatException nfe) {
                            editable.clear();
                        }


                        priceEditView.addTextChangedListener(this);

                    }
                }
            });

        }
    }

    private List<ShoppingListItem> listItem;
    private Context mContext;

    public ListViewAdapter(Context context, List<ShoppingListItem> item){
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

        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewAdapter.ViewHolder viewHolder, int position){
        ShoppingListItem shoppingListItem = listItem.get(position);

        viewHolder.nameTextView.setText("");

        viewHolder.priceEditView.setText("");

        viewHolder.quantityPicker.setMaxValue(20);
        viewHolder.quantityPicker.setMinValue(1);


    }

    @Override
    public int getItemCount(){
        return listItem.size();
    }


}