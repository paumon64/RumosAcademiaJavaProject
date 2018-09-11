/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paumon64
 */
@Entity
@Table(name = "articles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articles.findAll", query = "SELECT a FROM Articles a")
    , @NamedQuery(name = "Articles.findByIdArticle", query = "SELECT a FROM Articles a WHERE a.idArticle = :idArticle")
    , @NamedQuery(name = "Articles.findByDescription", query = "SELECT a FROM Articles a WHERE a.description = :description")
    , @NamedQuery(name = "Articles.findByInventory", query = "SELECT a FROM Articles a WHERE a.inventory = :inventory")
    , @NamedQuery(name = "Articles.findByPrice", query = "SELECT a FROM Articles a WHERE a.price = :price")})
public class Articles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idArticle")
    private Integer idArticle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "description")
    private String description;
    @Column(name = "inventory")
    private Integer inventory;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private Double price;
    @JoinColumn(name = "category", referencedColumnName = "idCategory")
    @ManyToOne(optional = false)
    private Category category;
    @JoinColumn(name = "subCategory", referencedColumnName = "idSubCategory")
    @ManyToOne(optional = false)
    private Subcategory subCategory;
    @JoinColumn(name = "owner", referencedColumnName = "idEmployee")
    @ManyToOne(optional = false)
    private Employees owner;

    public Articles() {
    }

    public Articles(Integer idArticle) {
        this.idArticle = idArticle;
    }

    public Articles(Integer idArticle, String description) {
        this.idArticle = idArticle;
        this.description = description;
    }

    public Integer getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Integer idArticle) {
        this.idArticle = idArticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Subcategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Subcategory subCategory) {
        this.subCategory = subCategory;
    }

    public Employees getOwner() {
        return owner;
    }

    public void setOwner(Employees owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArticle != null ? idArticle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articles)) {
            return false;
        }
        Articles other = (Articles) object;
        if ((this.idArticle == null && other.idArticle != null) || (this.idArticle != null && !this.idArticle.equals(other.idArticle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.ejb.Articles[ idArticle=" + idArticle + " ]";
    }
    
}
