/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import com.example.ejb.InvoiceLinesFacade;
import com.example.entities.InvoiceLines;
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
@Named(value = "invoiceLinesController")
@SessionScoped

public class InvoiceLinesController implements Serializable {

    @EJB
    InvoiceLinesFacade invoiceLinesFacade;

    @Inject
    InvoiceLinesBean invoiceLinesBean;

    /**
     * Creates a new instance of InvoiceLinesController
     */
    public InvoiceLinesController() {
    }

    public List<InvoiceLines> getAll() {

        return invoiceLinesFacade.findAll();
    }

    public int count() {

        return invoiceLinesFacade.count();
    }

    public String add() {

        InvoiceLines y = new InvoiceLines();
        y.setIdLine(Integer.SIZE);
        y.setIdArticle(invoiceLinesBean.getIdArticle());
        y.setQuantity(invoiceLinesBean.getQuantity());
        invoiceLinesFacade.create(y);
        return "list_invoiceLines";
    }

    public String add(String description) {

        InvoiceLines y = new InvoiceLines();
        //y.setId(Integer.SIZE);
        y.setQuantity(invoiceLinesBean.getQuantity());
        //   y.getIdArticle(invoiceLinesBean.getIdArticle());

        invoiceLinesFacade.create(y);
        return "index";
    }

    public String delete(InvoiceLines x) {

        invoiceLinesFacade.remove(x);

        return null;

    }
}
