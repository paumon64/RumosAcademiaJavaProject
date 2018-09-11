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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i")
    , @NamedQuery(name = "Invoice.findByIdInvoice", query = "SELECT i FROM Invoice i WHERE i.idInvoice = :idInvoice")
    , @NamedQuery(name = "Invoice.findByTotalInvoice", query = "SELECT i FROM Invoice i WHERE i.totalInvoice = :totalInvoice")})
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idInvoice")
    private Integer idInvoice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalInvoice")
    private int totalInvoice;
    @JoinColumn(name = "idEmployee", referencedColumnName = "idEmployee")
    @ManyToOne(optional = false)
    private Employees idEmployee;

    public Invoice() {
    }

    public Invoice(Integer idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Invoice(Integer idInvoice, int totalInvoice) {
        this.idInvoice = idInvoice;
        this.totalInvoice = totalInvoice;
    }

    public Integer getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(Integer idInvoice) {
        this.idInvoice = idInvoice;
    }

    public int getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(int totalInvoice) {
        this.totalInvoice = totalInvoice;
    }

    public Employees getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Employees idEmployee) {
        this.idEmployee = idEmployee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInvoice != null ? idInvoice.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.idInvoice == null && other.idInvoice != null) || (this.idInvoice != null && !this.idInvoice.equals(other.idInvoice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.ejb.Invoice[ idInvoice=" + idInvoice + " ]";
    }
    
}
