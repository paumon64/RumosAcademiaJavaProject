/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ejb;

import com.example.ejb.exceptions.IllegalOrphanException;
import com.example.ejb.exceptions.NonexistentEntityException;
import com.example.ejb.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.example.entities.Articles;
import com.example.entities.Subcategory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author paumon64
 */
public class SubcategoryJpaController implements Serializable {

    public SubcategoryJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subcategory subcategory) throws RollbackFailureException, Exception {
        if (subcategory.getArticlesCollection() == null) {
            subcategory.setArticlesCollection(new ArrayList<Articles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Articles> attachedArticlesCollection = new ArrayList<Articles>();
            for (Articles articlesCollectionArticlesToAttach : subcategory.getArticlesCollection()) {
                articlesCollectionArticlesToAttach = em.getReference(articlesCollectionArticlesToAttach.getClass(), articlesCollectionArticlesToAttach.getIdArticle());
                attachedArticlesCollection.add(articlesCollectionArticlesToAttach);
            }
            subcategory.setArticlesCollection(attachedArticlesCollection);
            em.persist(subcategory);
            for (Articles articlesCollectionArticles : subcategory.getArticlesCollection()) {
                Subcategory oldSubCategoryOfArticlesCollectionArticles = articlesCollectionArticles.getSubCategory();
                articlesCollectionArticles.setSubCategory(subcategory);
                articlesCollectionArticles = em.merge(articlesCollectionArticles);
                if (oldSubCategoryOfArticlesCollectionArticles != null) {
                    oldSubCategoryOfArticlesCollectionArticles.getArticlesCollection().remove(articlesCollectionArticles);
                    oldSubCategoryOfArticlesCollectionArticles = em.merge(oldSubCategoryOfArticlesCollectionArticles);
                }
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

    public void edit(Subcategory subcategory) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Subcategory persistentSubcategory = em.find(Subcategory.class, subcategory.getIdSubCategory());
            Collection<Articles> articlesCollectionOld = persistentSubcategory.getArticlesCollection();
            Collection<Articles> articlesCollectionNew = subcategory.getArticlesCollection();
            List<String> illegalOrphanMessages = null;
            for (Articles articlesCollectionOldArticles : articlesCollectionOld) {
                if (!articlesCollectionNew.contains(articlesCollectionOldArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Articles " + articlesCollectionOldArticles + " since its subCategory field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Articles> attachedArticlesCollectionNew = new ArrayList<Articles>();
            for (Articles articlesCollectionNewArticlesToAttach : articlesCollectionNew) {
                articlesCollectionNewArticlesToAttach = em.getReference(articlesCollectionNewArticlesToAttach.getClass(), articlesCollectionNewArticlesToAttach.getIdArticle());
                attachedArticlesCollectionNew.add(articlesCollectionNewArticlesToAttach);
            }
            articlesCollectionNew = attachedArticlesCollectionNew;
            subcategory.setArticlesCollection(articlesCollectionNew);
            subcategory = em.merge(subcategory);
            for (Articles articlesCollectionNewArticles : articlesCollectionNew) {
                if (!articlesCollectionOld.contains(articlesCollectionNewArticles)) {
                    Subcategory oldSubCategoryOfArticlesCollectionNewArticles = articlesCollectionNewArticles.getSubCategory();
                    articlesCollectionNewArticles.setSubCategory(subcategory);
                    articlesCollectionNewArticles = em.merge(articlesCollectionNewArticles);
                    if (oldSubCategoryOfArticlesCollectionNewArticles != null && !oldSubCategoryOfArticlesCollectionNewArticles.equals(subcategory)) {
                        oldSubCategoryOfArticlesCollectionNewArticles.getArticlesCollection().remove(articlesCollectionNewArticles);
                        oldSubCategoryOfArticlesCollectionNewArticles = em.merge(oldSubCategoryOfArticlesCollectionNewArticles);
                    }
                }
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
                Integer id = subcategory.getIdSubCategory();
                if (findSubcategory(id) == null) {
                    throw new NonexistentEntityException("The subcategory with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Subcategory subcategory;
            try {
                subcategory = em.getReference(Subcategory.class, id);
                subcategory.getIdSubCategory();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subcategory with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Articles> articlesCollectionOrphanCheck = subcategory.getArticlesCollection();
            for (Articles articlesCollectionOrphanCheckArticles : articlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subcategory (" + subcategory + ") cannot be destroyed since the Articles " + articlesCollectionOrphanCheckArticles + " in its articlesCollection field has a non-nullable subCategory field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(subcategory);
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

    public List<Subcategory> findSubcategoryEntities() {
        return findSubcategoryEntities(true, -1, -1);
    }

    public List<Subcategory> findSubcategoryEntities(int maxResults, int firstResult) {
        return findSubcategoryEntities(false, maxResults, firstResult);
    }

    private List<Subcategory> findSubcategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subcategory.class));
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

    public Subcategory findSubcategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subcategory.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubcategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subcategory> rt = cq.from(Subcategory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
