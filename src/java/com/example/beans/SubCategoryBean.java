/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author paumon64
 */
@Named(value = "SubCategory")
@SessionScoped

public class SubCategoryBean implements Serializable {

    /**
     * Creates a new instance of CategoriesBean
     */
    
    private int subCategoryId;
    private String subCategoryName;
    private int categoryId;

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
   
    public SubCategoryBean() {
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCategoryName() {
        return subCategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.subCategoryName = subCategoryName;
    }
    
}
