/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ejb;

import com.example.entities.InvoiceLines;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author paumon64
 */
@Stateless
public class InvoiceLinesFacade extends AbstractFacade<InvoiceLines> {

    @PersistenceContext(unitName = "RumosAcademiaJavaProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvoiceLinesFacade() {
        super(InvoiceLines.class);
    }

}
