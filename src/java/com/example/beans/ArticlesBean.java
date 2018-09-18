/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import com.example.ejb.ArticlesFacade;
import com.example.entities.Articles;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 *
 * @author paumon64
 */

@Named(value = "articlesBean")
@SessionScoped

public class ArticlesBean implements Serializable {
    
        @EJB
    ArticlesFacade articlesFacade;
    
    private int idArticle;
    private String description;
    private int category;
    private int subcategory;
    private String owner;
    private int inventory;
    private double price;
    private ArrayList articleList = null;

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public String getDescription() {
        return description;
    }
    
    public String getDescription(int idArticle) {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(int subcategory) {
        this.subcategory = subcategory;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
       public ArrayList getArticleList() {
        if (articleList == null) {
            articleList = new ArrayList<String>();
            List freshArticle = articlesFacade.findAll();
           Iterator g = freshArticle.iterator();
           while (g.hasNext()) {
                Articles item = (Articles) g.next();
              //  SelectItem n = new SelectItem(item, item);
               articleList.add(item.getDescription());
            }
       }
        return articleList;
    
   } 
}
