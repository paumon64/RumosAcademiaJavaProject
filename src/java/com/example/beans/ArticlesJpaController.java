/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.beans;

import com.example.ejb.exceptions.NonexistentEntityException;
import com.example.ejb.exceptions.RollbackFailureException;
import com.example.entities.Articles;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.example.entities.Category;
import com.example.entities.Subcategory;
import com.example.entities.Employees;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author paumon64
 */

@Named(value = "articlesController")
@SessionScoped

public class ArticlesJpaController implements Serializable {

    public ArticlesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Articles articles) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Category category = articles.getCategory();
            if (category != null) {
                category = em.getReference(category.getClass(), category.getIdCategory());
                articles.setCategory(category);
            }
            Subcategory subCategory = articles.getSubCategory();
            if (subCategory != null) {
                subCategory = em.getReference(subCategory.getClass(), subCategory.getIdSubCategory());
                articles.setSubCategory(subCategory);
            }
            Employees owner = articles.getOwner();
            if (owner != null) {
                owner = em.getReference(owner.getClass(), owner.getIdEmployee());
                articles.setOwner(owner);
            }
            em.persist(articles);
            if (category != null) {
                category.getArticlesCollection().add(articles);
                category = em.merge(category);
            }
            if (subCategory != null) {
                subCategory.getArticlesCollection().add(articles);
                subCategory = em.merge(subCategory);
            }
            if (owner != null) {
                owner.getArticlesCollection().add(articles);
                owner = em.merge(owner);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Articles articles) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Articles persistentArticles = em.find(Articles.class, articles.getIdArticle());
            Category categoryOld = persistentArticles.getCategory();
            Category categoryNew = articles.getCategory();
            Subcategory subCategoryOld = persistentArticles.getSubCategory();
            Subcategory subCategoryNew = articles.getSubCategory();
            Employees ownerOld = persistentArticles.getOwner();
            Employees ownerNew = articles.getOwner();
            if (categoryNew != null) {
                categoryNew = em.getReference(categoryNew.getClass(), categoryNew.getIdCategory());
                articles.setCategory(categoryNew);
            }
            if (subCategoryNew != null) {
                subCategoryNew = em.getReference(subCategoryNew.getClass(), subCategoryNew.getIdSubCategory());
                articles.setSubCategory(subCategoryNew);
            }
            if (ownerNew != null) {
                ownerNew = em.getReference(ownerNew.getClass(), ownerNew.getIdEmployee());
                articles.setOwner(ownerNew);
            }
            articles = em.merge(articles);
            if (categoryOld != null && !categoryOld.equals(categoryNew)) {
                categoryOld.getArticlesCollection().remove(articles);
                categoryOld = em.merge(categoryOld);
            }
            if (categoryNew != null && !categoryNew.equals(categoryOld)) {
                categoryNew.getArticlesCollection().add(articles);
                categoryNew = em.merge(categoryNew);
            }
            if (subCategoryOld != null && !subCategoryOld.equals(subCategoryNew)) {
                subCategoryOld.getArticlesCollection().remove(articles);
                subCategoryOld = em.merge(subCategoryOld);
            }
            if (subCategoryNew != null && !subCategoryNew.equals(subCategoryOld)) {
                subCategoryNew.getArticlesCollection().add(articles);
                subCategoryNew = em.merge(subCategoryNew);
            }
            if (ownerOld != null && !ownerOld.equals(ownerNew)) {
                ownerOld.getArticlesCollection().remove(articles);
                ownerOld = em.merge(ownerOld);
            }
            if (ownerNew != null && !ownerNew.equals(ownerOld)) {
                ownerNew.getArticlesCollection().add(articles);
                ownerNew = em.merge(ownerNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = articles.getIdArticle();
                if (findArticles(id) == null) {
                    throw new NonexistentEntityException("The articles with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Articles articles;
            try {
                articles = em.getReference(Articles.class, id);
                articles.getIdArticle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articles with id " + id + " no longer exists.", enfe);
            }
            Category category = articles.getCategory();
            if (category != null) {
                category.getArticlesCollection().remove(articles);
                category = em.merge(category);
            }
            Subcategory subCategory = articles.getSubCategory();
            if (subCategory != null) {
                subCategory.getArticlesCollection().remove(articles);
                subCategory = em.merge(subCategory);
            }
            Employees owner = articles.getOwner();
            if (owner != null) {
                owner.getArticlesCollection().remove(articles);
                owner = em.merge(owner);
            }
            em.remove(articles);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Articles> findArticlesEntities() {
        return findArticlesEntities(true, -1, -1);
    }

    public List<Articles> findArticlesEntities(int maxResults, int firstResult) {
        return findArticlesEntities(false, maxResults, firstResult);
    }

    private List<Articles> findArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Articles.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Articles findArticles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Articles.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Articles> rt = cq.from(Articles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
