package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Dish> items;

    public Cart(){
        items = new ArrayList<>();
    }

    public void addDish(Dish dish){
        items.add(dish);
    }

    public List<Dish> getCart(){
        return items;
    }

    public void clear(){
        items.clear();
    }

    public void deleteDish(String name){
        boolean deleted = false;
        for(Dish dish : items){
            if ((dish.getDishName().equals(name)) && (!deleted)){
                items.remove(dish);
                deleted = !deleted;
            }
        }
    }
}
