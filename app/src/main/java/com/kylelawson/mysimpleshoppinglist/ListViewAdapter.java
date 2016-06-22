package com.kylelawson.mysimpleshoppinglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;


/**
 * Created by Kyle on 6/20/2016.
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private String current = "";
        public TextView nameTextView;
        public EditText priceEditView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
            priceEditView = (EditText) itemView.findViewById(R.id.list_item_price);
            priceEditView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!editable.toString().equals(current)){
                        priceEditView.removeTextChangedListener(this);

                        String replacingString = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
                        String cleanString = editable.toString().replaceAll(replacingString, "");

                        double parsed;
                        try{
                            parsed = Double.parseDouble(cleanString);
                        }catch (NumberFormatException e){
                            parsed = 0.00;
                        }

                        current = NumberFormat.getCurrencyInstance().format(parsed/100);

                        priceEditView.setText(current);
                        priceEditView.setSelection(current.length());
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
    public void onBindViewHolder(ListViewAdapter.ViewHolder viewHolder, int posiiton){
        ShoppingListItem shoppingListItem = listItem.get(posiiton);

        TextView itemName = viewHolder.nameTextView;
        itemName.setText(shoppingListItem.getName());

        EditText priceEdit = viewHolder.priceEditView;


    }

    @Override
    public int getItemCount(){
        return listItem.size();
    }


}