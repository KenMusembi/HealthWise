package com.example.healthwise;

public class Stories {

    String storyId, storyCategory, storyContent;

     public Stories() {
         //this constructor is required
     }

    public Stories(String storyId, String storyCategory, String storyContent) {
        this.storyId = storyId;
         this.storyCategory = storyCategory;
        this.storyContent = storyContent;
    }

    //getters for the Story class
    public String getStoryId(){
         return storyId;
    }

    public String getStoryCategory(){
         return storyCategory;
    }

    public String getStoryContent(){
         return storyContent;
    }

}
