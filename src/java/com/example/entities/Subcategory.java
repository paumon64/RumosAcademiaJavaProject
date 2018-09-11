/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author paumon64
 */
@Entity
@Table(name = "subcategory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subcategory.findAll", query = "SELECT s FROM Subcategory s")
    , @NamedQuery(name = "Subcategory.findByIdSubCategory", query = "SELECT s FROM Subcategory s WHERE s.idSubCategory = :idSubCategory")
    , @NamedQuery(name = "Subcategory.findBySubCategoryDescript", query = "SELECT s FROM Subcategory s WHERE s.subCategoryDescript = :subCategoryDescript")
    , @NamedQuery(name = "Subcategory.findByCategory", query = "SELECT s FROM Subcategory s WHERE s.category = :category")})
public class Subcategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSubCategory")
    private Integer idSubCategory;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "SubCategoryDescript")
    private String subCategoryDescript;
    @Basic(optional = false)
    @NotNull
    @Column(name = "category")
    private int category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subCategory")
    private Collection<Articles> articlesCollection;

    public Subcategory() {
    }

    public Subcategory(Integer idSubCategory) {
        this.idSubCategory = idSubCategory;
    }

    public Subcategory(Integer idSubCategory, String subCategoryDescript, int category) {
        this.idSubCategory = idSubCategory;
        this.subCategoryDescript = subCategoryDescript;
        this.category = category;
    }

    public Integer getIdSubCategory() {
        return idSubCategory;
    }

    public void setIdSubCategory(Integer idSubCategory) {
        this.idSubCategory = idSubCategory;
    }

    public String getSubCategoryDescript() {
        return subCategoryDescript;
    }

    public void setSubCategoryDescript(String subCategoryDescript) {
        this.subCategoryDescript = subCategoryDescript;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @XmlTransient
    public Collection<Articles> getArticlesCollection() {
        return articlesCollection;
    }

    public void setArticlesCollection(Collection<Articles> articlesCollection) {
        this.articlesCollection = articlesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubCategory != null ? idSubCategory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subcategory)) {
            return false;
        }
        Subcategory other = (Subcategory) object;
        if ((this.idSubCategory == null && other.idSubCategory != null) || (this.idSubCategory != null && !this.idSubCategory.equals(other.idSubCategory))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.ejb.Subcategory[ idSubCategory=" + idSubCategory + " ]";
    }
    
}
