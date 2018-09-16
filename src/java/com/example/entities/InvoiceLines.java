/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paumon64
 */
@Entity
@Table(name = "invoicelines")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoicelines.findAll", query = "SELECT i FROM Invoicelines i")
    , @NamedQuery(name = "Invoicelines.findByIdLine", query = "SELECT i FROM Invoicelines i WHERE i.idLine = :idLine")
    , @NamedQuery(name = "Invoicelines.findByIdInvoice", query = "SELECT i FROM Invoicelines i WHERE i.idInvoice = :idInvoice")
    , @NamedQuery(name = "Invoicelines.findByIdArticle", query = "SELECT i FROM Invoicelines i WHERE i.idArticle = :idArticle")
    , @NamedQuery(name = "Invoicelines.findByPrice", query = "SELECT i FROM Invoicelines i WHERE i.price = :price")})

public class InvoiceLines implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idLine")
    private Integer idLine;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idInvoice")
    private int idInvoice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idArticle")
    private int idArticle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;

    public InvoiceLines() {
    }

    public InvoiceLines(Integer idLine) {
        this.idLine = idLine;
    }

    public InvoiceLines(Integer idLine, int idInvoice, int idArticle, int quantity) {
        this.idLine = idLine;
        this.idInvoice = idInvoice;
        this.idArticle = idArticle;
        this.quantity = quantity;
    }

    public Integer getIdLine() {
        return idLine;
    }

    public void setIdLine(Integer idLine) {
        this.idLine = idLine;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLine != null ? idLine.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceLines)) {
            return false;
        }
        InvoiceLines other = (InvoiceLines) object;
        if ((this.idLine == null && other.idLine != null) || (this.idLine != null && !this.idLine.equals(other.idLine))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.entities.Invoicelines[ idLine=" + idLine + " ]";
    }
    
}
