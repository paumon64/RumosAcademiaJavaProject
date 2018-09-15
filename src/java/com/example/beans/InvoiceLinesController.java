/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import com.example.ejb.ArticlesFacade;
import com.example.ejb.InvoiceLinesFacade;
import com.example.entities.Articles;
import com.example.entities.Employees;
import com.example.entities.InvoiceLines;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

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
    
    @Inject
    ArticlesBean ArticlesBean;
    
    /**
     * Creates a new instance of EmployeesController
     */
    
    public InvoiceLinesController() {
    }
    
    public List<InvoiceLines> getAll() {

        return invoiceLinesFacade.findAll();
    }

    public int count() {

        return invoiceLinesFacade.count();
    }

    public String delete(InvoiceLines x) {

        invoiceLinesFacade.remove(x);

        return null;

    }

    public String add() {

        InvoiceLines y = new InvoiceLines();
        
       // y.setIdLine(Integer.SIZE);
        y.setIdArticle(invoiceLinesBean.getIdArticle());
        y.setQuantity(invoiceLinesBean.getQuantity());
        
//        y.setFirstName(employeesBean.getFirstName());
//        y.setLastName(employeesBean.getLastName());
//        y.setPosition(employeesBean.getPosition());

        invoiceLinesFacade.create(y);

        return "list_invoiceLines";
    }

    public String edit(Employees i) {
//        employeesBean.setIdEmployee(i.getIdEmployee());
//        employeesBean.setFirstName(i.getFirstName());
//        employeesBean.setLastName(i.getLastName());
//        employeesBean.setPosition(i.getPosition());

         return "list_invoiceLines";
    }

    public String save(){
        
       //Employees i = new Employees(invoiceLinesBean.getIdEmployee());
       
//       i.setFirstName(employeesBean.getFirstName());
//       i.setLastName(employeesBean.getLastName());
//       i.setPosition(employeesBean.getPosition());

       //invoiceLinesFacade.edit(i);
       
     
       
       
        return "list_invoiceLines";
       

   }
}
