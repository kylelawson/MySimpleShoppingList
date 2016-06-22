package com.kylelawson.mysimpleshoppinglist;

import java.util.ArrayList;

/**
 * Created by Kyle on 6/20/2016.
 */
public class ShoppingListItem {
    public String name;
    public Double price;

    public ShoppingListItem(String name, Double price){
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public Double getPrice(){
        return price;
    }



    public static ArrayList<ShoppingListItem> createShoppingItem(int numOfItems){
        ArrayList<ShoppingListItem> shoppingItem = new ArrayList<>();

        for(int i = 1; i <= numOfItems; i++){
            shoppingItem.add(new ShoppingListItem("", 0.00));
        }

        return shoppingItem;
    }
}
