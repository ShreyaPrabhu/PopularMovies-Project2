package com.example.shreyaprabhu.popmovies;

import java.io.Serializable;

/**
 * Created by Shreya Prabhu on 6/6/2016.
 */
public class TrailerAttributes implements Serializable {


    private String id;
    private String key;
    private String name;

    public TrailerAttributes(){

        //Constructer when no arguments passed
    }
    public TrailerAttributes(String id, String key, String name){
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public String getId(){
        return (id);
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getKey(){
        return (key);
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getName(){
        return (name);
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
