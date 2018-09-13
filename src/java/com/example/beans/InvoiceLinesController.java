/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import com.example.ejb.InvoiceLinesFacade;
import com.example.ejb.exceptions.NonexistentEntityException;
import com.example.ejb.exceptions.RollbackFailureException;
import com.example.entities.InvoiceLines;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author paumon64
 */
public class InvoiceLinesController implements Serializable {

  
    @EJB
    InvoiceLinesFacade invoiceLinesFacade;

    @Inject
    InvoiceLinesBean invoiceLinesBean;
    
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
}

