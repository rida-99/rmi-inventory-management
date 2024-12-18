package server;

import java.io.Serializable;

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String category;
    private int quantity;
    private double price;
    

    // Constructor, Getters, and Setters
    public Product(int id, String name, String category, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }
    
    public Product(String name, String category, int quantity, double price) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId(){
        return this.id;
    }



    public void setId(int id) {
            this.id = id;
        }
   



    public String getName(){
        return this.name;
    }


    public String getCategory(){
        return this.category;
    }


    public int getQuantity(){
        return this.quantity;
    }


    public double getPrice(){
        return this.price;
    }


        
      
   

    @Override
    public String toString() {
        return String.format("Product[ID=%d, Name=%s, Category=%s, Quantity=%d, Price=%.2f, ]", id, name, category, quantity, price);
    }
}