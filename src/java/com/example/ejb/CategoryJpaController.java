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
import com.example.entities.Category;
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
public class CategoryJpaController implements Serializable {

    public CategoryJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) throws RollbackFailureException, Exception {
        if (category.getArticlesCollection() == null) {
            category.setArticlesCollection(new ArrayList<Articles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Articles> attachedArticlesCollection = new ArrayList<Articles>();
            for (Articles articlesCollectionArticlesToAttach : category.getArticlesCollection()) {
                articlesCollectionArticlesToAttach = em.getReference(articlesCollectionArticlesToAttach.getClass(), articlesCollectionArticlesToAttach.getIdArticle());
                attachedArticlesCollection.add(articlesCollectionArticlesToAttach);
            }
            category.setArticlesCollection(attachedArticlesCollection);
            em.persist(category);
            for (Articles articlesCollectionArticles : category.getArticlesCollection()) {
                Category oldCategoryOfArticlesCollectionArticles = articlesCollectionArticles.getCategory();
                articlesCollectionArticles.setCategory(category);
                articlesCollectionArticles = em.merge(articlesCollectionArticles);
                if (oldCategoryOfArticlesCollectionArticles != null) {
                    oldCategoryOfArticlesCollectionArticles.getArticlesCollection().remove(articlesCollectionArticles);
                    oldCategoryOfArticlesCollectionArticles = em.merge(oldCategoryOfArticlesCollectionArticles);
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

    public void edit(Category category) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Category persistentCategory = em.find(Category.class, category.getIdCategory());
            Collection<Articles> articlesCollectionOld = persistentCategory.getArticlesCollection();
            Collection<Articles> articlesCollectionNew = category.getArticlesCollection();
            List<String> illegalOrphanMessages = null;
            for (Articles articlesCollectionOldArticles : articlesCollectionOld) {
                if (!articlesCollectionNew.contains(articlesCollectionOldArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Articles " + articlesCollectionOldArticles + " since its category field is not nullable.");
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
            category.setArticlesCollection(articlesCollectionNew);
            category = em.merge(category);
            for (Articles articlesCollectionNewArticles : articlesCollectionNew) {
                if (!articlesCollectionOld.contains(articlesCollectionNewArticles)) {
                    Category oldCategoryOfArticlesCollectionNewArticles = articlesCollectionNewArticles.getCategory();
                    articlesCollectionNewArticles.setCategory(category);
                    articlesCollectionNewArticles = em.merge(articlesCollectionNewArticles);
                    if (oldCategoryOfArticlesCollectionNewArticles != null && !oldCategoryOfArticlesCollectionNewArticles.equals(category)) {
                        oldCategoryOfArticlesCollectionNewArticles.getArticlesCollection().remove(articlesCollectionNewArticles);
                        oldCategoryOfArticlesCollectionNewArticles = em.merge(oldCategoryOfArticlesCollectionNewArticles);
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
                Integer id = category.getIdCategory();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getIdCategory();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Articles> articlesCollectionOrphanCheck = category.getArticlesCollection();
            for (Articles articlesCollectionOrphanCheckArticles : articlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Category (" + category + ") cannot be destroyed since the Articles " + articlesCollectionOrphanCheckArticles + " in its articlesCollection field has a non-nullable category field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(category);
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

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
