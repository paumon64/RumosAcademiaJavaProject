/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.inject.Inject;

/**
 *
 * @author paumon64
 */
@Named(value = "invoiceLines")
@SessionScoped
public class InvoiceLinesBean implements Serializable {
    
    @Inject
    ArticlesBean articlesBean;

    /**
     * Creates a new instance of InvoiceLinesBean
     */
    public InvoiceLinesBean() {
    }
      
    private int quantity;
    private int idArticle;

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(String description) {
        this.idArticle = articlesBean.getIdArticle();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
