/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author paumon64
 */
@Named(value = "invoiceLines")
@SessionScoped
public class InvoiceLinesBean implements Serializable {

    /**
     * Creates a new instance of InvoiceLinesBean
     */
    public InvoiceLinesBean() {
    }
      
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
