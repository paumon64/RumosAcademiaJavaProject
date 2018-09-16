/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import com.example.ejb.SubcategoryFacade;
import com.example.entities.Category;
import com.example.entities.Subcategory;
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


@Named(value = "subCategoryController")
@SessionScoped


public class SubCategoryController implements Serializable{
    
    @EJB
    SubcategoryFacade SubcategoryFacade;

    @Inject
    SubCategoryBean subCategoryBean;

    
        public List<Subcategory> getAll() {

        return SubcategoryFacade.findAll();
    }

    public int count() {

        return SubcategoryFacade.count();
    }

    public String delete(Subcategory x) {

       SubcategoryFacade.remove(x);

        return null;

    }

    public String add() {

        Subcategory y = new Subcategory();
        y.setIdSubCategory(Integer.SIZE);
        y.setSubCategoryDescript(subCategoryBean.getSubCategoryName());
       // y.setSubCategoryName(subCategoryBean.getCategoryName());

//        y.setCategory(articleBean.getCategory());
//        y.setSubCategory(articleBean.getSubcategory());

        SubcategoryFacade.create(y);

        return "index";
    }

    public String edit(Subcategory i) {
        
        subCategoryBean.setSubCategoryId(i.getIdSubCategory());
        subCategoryBean.setSubCategoryName(i.getSubCategoryDescript());
        return "update";
    }

    public String save(){
        
      Subcategory i = new Subcategory(subCategoryBean.getSubCategoryId());
      i.setSubCategoryDescript(subCategoryBean.getSubCategoryName());
     
       SubcategoryFacade.edit(i);
       
       return "index";
       

   }
}
