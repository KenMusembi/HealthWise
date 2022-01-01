package com.example.healthwise;

//categories class, only has id and name attributes
public class Categories {

    String categoryID, categoryName;

     public Categories() {
         //this constructor is required
     }

     //overloaded constructor
    public Categories(String categoryID, String categoryName) {
         this.categoryID= categoryID;
         this.categoryName = categoryName;
    }

    //getters for the Category class
    public String getCategoryID(){
         return categoryID;
    }

    public String getCategoryName(){
         return categoryName;
    }

}
