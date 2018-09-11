/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ejb;

import com.example.ejb.exceptions.NonexistentEntityException;
import com.example.ejb.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.example.entities.Employees;
import com.example.entities.Invoice;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author paumon64
 */
public class InvoiceJpaController implements Serializable {

    public InvoiceJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Invoice invoice) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Employees idEmployee = invoice.getIdEmployee();
            if (idEmployee != null) {
                idEmployee = em.getReference(idEmployee.getClass(), idEmployee.getIdEmployee());
                invoice.setIdEmployee(idEmployee);
            }
            em.persist(invoice);
            if (idEmployee != null) {
                idEmployee.getInvoiceCollection().add(invoice);
                idEmployee = em.merge(idEmployee);
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

    public void edit(Invoice invoice) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Invoice persistentInvoice = em.find(Invoice.class, invoice.getIdInvoice());
            Employees idEmployeeOld = persistentInvoice.getIdEmployee();
            Employees idEmployeeNew = invoice.getIdEmployee();
            if (idEmployeeNew != null) {
                idEmployeeNew = em.getReference(idEmployeeNew.getClass(), idEmployeeNew.getIdEmployee());
                invoice.setIdEmployee(idEmployeeNew);
            }
            invoice = em.merge(invoice);
            if (idEmployeeOld != null && !idEmployeeOld.equals(idEmployeeNew)) {
                idEmployeeOld.getInvoiceCollection().remove(invoice);
                idEmployeeOld = em.merge(idEmployeeOld);
            }
            if (idEmployeeNew != null && !idEmployeeNew.equals(idEmployeeOld)) {
                idEmployeeNew.getInvoiceCollection().add(invoice);
                idEmployeeNew = em.merge(idEmployeeNew);
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
                Integer id = invoice.getIdInvoice();
                if (findInvoice(id) == null) {
                    throw new NonexistentEntityException("The invoice with id " + id + " no longer exists.");
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
            Invoice invoice;
            try {
                invoice = em.getReference(Invoice.class, id);
                invoice.getIdInvoice();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The invoice with id " + id + " no longer exists.", enfe);
            }
            Employees idEmployee = invoice.getIdEmployee();
            if (idEmployee != null) {
                idEmployee.getInvoiceCollection().remove(invoice);
                idEmployee = em.merge(idEmployee);
            }
            em.remove(invoice);
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

    public List<Invoice> findInvoiceEntities() {
        return findInvoiceEntities(true, -1, -1);
    }

    public List<Invoice> findInvoiceEntities(int maxResults, int firstResult) {
        return findInvoiceEntities(false, maxResults, firstResult);
    }

    private List<Invoice> findInvoiceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Invoice.class));
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

    public Invoice findInvoice(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Invoice.class, id);
        } finally {
            em.close();
        }
    }

    public int getInvoiceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Invoice> rt = cq.from(Invoice.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
