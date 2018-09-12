/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import com.example.ejb.CategoryFacade;
import com.example.entities.Category;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author paumon64
 */


@Named(value = "categoriesController")
@SessionScoped


public class CategoriesController implements Serializable{
    
    @EJB
    CategoryFacade categoryFacade;

    @Inject
    CategoriesBean categoryBean;

    
        public List<Category> getAll() {

        return categoryFacade.findAll();
    }

    public int count() {

        return categoryFacade.count();
    }

    public String delete(Category x) {

       categoryFacade.remove(x);

        return null;

    }

    public String add() {

        Category y = new Category();
        y.setIdCategory(Integer.SIZE);
        y.setCategoryName(categoryBean.getCategoryName());

//        y.setCategory(articleBean.getCategory());
//        y.setSubCategory(articleBean.getSubcategory());

        categoryFacade.create(y);

        return "index";
    }

    public String edit(Category i) {
        
        categoryBean.setCategoryId(i.getIdCategory());
        categoryBean.setCategoryName(i.getCategoryName());
        return "update";
    }

    public String save(){
        
      Category i = new Category(categoryBean.getCategoryId());
      i.setCategoryName(categoryBean.getCategoryName());
     
       categoryFacade.edit(i);
       
       return "index";
       

   }
}
