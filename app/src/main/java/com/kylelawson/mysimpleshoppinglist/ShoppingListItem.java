package com.kylelawson.mysimpleshoppinglist;

import java.util.ArrayList;

/**
 * Created by Kyle on 6/20/2016.
 */
public class ShoppingListItem {
    public String name;
    public Double price;
    public int quantity;

    public ShoppingListItem(String name, Double price, int quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName(){
        return name;
    }

    public Double getPrice(){
        return price;
    }

    public int getQuantity() { return quantity; }



    public static ArrayList<ShoppingListItem> createShoppingItem(int numOfItems){
        ArrayList<ShoppingListItem> shoppingItem = new ArrayList<>();

        if(numOfItems != 0) {

            for (int i = 1; i <= numOfItems; i++) {
                shoppingItem.add(new ShoppingListItem("", 0.00, 0));
            }
        }

        return shoppingItem;
    }
}
