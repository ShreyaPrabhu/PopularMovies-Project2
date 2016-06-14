package com.example.shreyaprabhu.popmovies;

import java.io.Serializable;

/**
 * Created by Shreya Prabhu on 6/7/2016.
 */
public class ReviewAttributes implements Serializable {

    private String id;
    private String author;
    private String content;

    public ReviewAttributes(){


    }
    public ReviewAttributes(String id, String author, String content){
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public String getId(){
        return (id);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAuthor(){
        return (author);
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getContent(){
        return (content);
    }

    public void setContent(String content)
    {
        this.content = content;
    }


}

