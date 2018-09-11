/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ejb;

import com.example.ejb.exceptions.NonexistentEntityException;
import com.example.ejb.exceptions.RollbackFailureException;
import com.example.entities.Invoicelines;
import java.io.Serializable;
import java.util.List;
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
public class InvoicelinesJpaController implements Serializable {

    public InvoicelinesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Invoicelines invoicelines) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(invoicelines);
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

    public void edit(Invoicelines invoicelines) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            invoicelines = em.merge(invoicelines);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = invoicelines.getIdLine();
                if (findInvoicelines(id) == null) {
                    throw new NonexistentEntityException("The invoicelines with id " + id + " no longer exists.");
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
            Invoicelines invoicelines;
            try {
                invoicelines = em.getReference(Invoicelines.class, id);
                invoicelines.getIdLine();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The invoicelines with id " + id + " no longer exists.", enfe);
            }
            em.remove(invoicelines);
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

    public List<Invoicelines> findInvoicelinesEntities() {
        return findInvoicelinesEntities(true, -1, -1);
    }

    public List<Invoicelines> findInvoicelinesEntities(int maxResults, int firstResult) {
        return findInvoicelinesEntities(false, maxResults, firstResult);
    }

    private List<Invoicelines> findInvoicelinesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Invoicelines.class));
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

    public Invoicelines findInvoicelines(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Invoicelines.class, id);
        } finally {
            em.close();
        }
    }

    public int getInvoicelinesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Invoicelines> rt = cq.from(Invoicelines.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
