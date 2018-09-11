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
import com.example.entities.Invoice;
import java.util.ArrayList;
import java.util.Collection;
import com.example.entities.Articles;
import com.example.entities.Employees;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author paumon64
 */
public class EmployeesJpaController implements Serializable {

    public EmployeesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employees employees) throws RollbackFailureException, Exception {
        if (employees.getInvoiceCollection() == null) {
            employees.setInvoiceCollection(new ArrayList<Invoice>());
        }
        if (employees.getArticlesCollection() == null) {
            employees.setArticlesCollection(new ArrayList<Articles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Invoice> attachedInvoiceCollection = new ArrayList<Invoice>();
            for (Invoice invoiceCollectionInvoiceToAttach : employees.getInvoiceCollection()) {
                invoiceCollectionInvoiceToAttach = em.getReference(invoiceCollectionInvoiceToAttach.getClass(), invoiceCollectionInvoiceToAttach.getIdInvoice());
                attachedInvoiceCollection.add(invoiceCollectionInvoiceToAttach);
            }
            employees.setInvoiceCollection(attachedInvoiceCollection);
            Collection<Articles> attachedArticlesCollection = new ArrayList<Articles>();
            for (Articles articlesCollectionArticlesToAttach : employees.getArticlesCollection()) {
                articlesCollectionArticlesToAttach = em.getReference(articlesCollectionArticlesToAttach.getClass(), articlesCollectionArticlesToAttach.getIdArticle());
                attachedArticlesCollection.add(articlesCollectionArticlesToAttach);
            }
            employees.setArticlesCollection(attachedArticlesCollection);
            em.persist(employees);
            for (Invoice invoiceCollectionInvoice : employees.getInvoiceCollection()) {
                Employees oldIdEmployeeOfInvoiceCollectionInvoice = invoiceCollectionInvoice.getIdEmployee();
                invoiceCollectionInvoice.setIdEmployee(employees);
                invoiceCollectionInvoice = em.merge(invoiceCollectionInvoice);
                if (oldIdEmployeeOfInvoiceCollectionInvoice != null) {
                    oldIdEmployeeOfInvoiceCollectionInvoice.getInvoiceCollection().remove(invoiceCollectionInvoice);
                    oldIdEmployeeOfInvoiceCollectionInvoice = em.merge(oldIdEmployeeOfInvoiceCollectionInvoice);
                }
            }
            for (Articles articlesCollectionArticles : employees.getArticlesCollection()) {
                Employees oldOwnerOfArticlesCollectionArticles = articlesCollectionArticles.getOwner();
                articlesCollectionArticles.setOwner(employees);
                articlesCollectionArticles = em.merge(articlesCollectionArticles);
                if (oldOwnerOfArticlesCollectionArticles != null) {
                    oldOwnerOfArticlesCollectionArticles.getArticlesCollection().remove(articlesCollectionArticles);
                    oldOwnerOfArticlesCollectionArticles = em.merge(oldOwnerOfArticlesCollectionArticles);
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

    public void edit(Employees employees) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Employees persistentEmployees = em.find(Employees.class, employees.getIdEmployee());
            Collection<Invoice> invoiceCollectionOld = persistentEmployees.getInvoiceCollection();
            Collection<Invoice> invoiceCollectionNew = employees.getInvoiceCollection();
            Collection<Articles> articlesCollectionOld = persistentEmployees.getArticlesCollection();
            Collection<Articles> articlesCollectionNew = employees.getArticlesCollection();
            List<String> illegalOrphanMessages = null;
            for (Invoice invoiceCollectionOldInvoice : invoiceCollectionOld) {
                if (!invoiceCollectionNew.contains(invoiceCollectionOldInvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Invoice " + invoiceCollectionOldInvoice + " since its idEmployee field is not nullable.");
                }
            }
            for (Articles articlesCollectionOldArticles : articlesCollectionOld) {
                if (!articlesCollectionNew.contains(articlesCollectionOldArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Articles " + articlesCollectionOldArticles + " since its owner field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Invoice> attachedInvoiceCollectionNew = new ArrayList<Invoice>();
            for (Invoice invoiceCollectionNewInvoiceToAttach : invoiceCollectionNew) {
                invoiceCollectionNewInvoiceToAttach = em.getReference(invoiceCollectionNewInvoiceToAttach.getClass(), invoiceCollectionNewInvoiceToAttach.getIdInvoice());
                attachedInvoiceCollectionNew.add(invoiceCollectionNewInvoiceToAttach);
            }
            invoiceCollectionNew = attachedInvoiceCollectionNew;
            employees.setInvoiceCollection(invoiceCollectionNew);
            Collection<Articles> attachedArticlesCollectionNew = new ArrayList<Articles>();
            for (Articles articlesCollectionNewArticlesToAttach : articlesCollectionNew) {
                articlesCollectionNewArticlesToAttach = em.getReference(articlesCollectionNewArticlesToAttach.getClass(), articlesCollectionNewArticlesToAttach.getIdArticle());
                attachedArticlesCollectionNew.add(articlesCollectionNewArticlesToAttach);
            }
            articlesCollectionNew = attachedArticlesCollectionNew;
            employees.setArticlesCollection(articlesCollectionNew);
            employees = em.merge(employees);
            for (Invoice invoiceCollectionNewInvoice : invoiceCollectionNew) {
                if (!invoiceCollectionOld.contains(invoiceCollectionNewInvoice)) {
                    Employees oldIdEmployeeOfInvoiceCollectionNewInvoice = invoiceCollectionNewInvoice.getIdEmployee();
                    invoiceCollectionNewInvoice.setIdEmployee(employees);
                    invoiceCollectionNewInvoice = em.merge(invoiceCollectionNewInvoice);
                    if (oldIdEmployeeOfInvoiceCollectionNewInvoice != null && !oldIdEmployeeOfInvoiceCollectionNewInvoice.equals(employees)) {
                        oldIdEmployeeOfInvoiceCollectionNewInvoice.getInvoiceCollection().remove(invoiceCollectionNewInvoice);
                        oldIdEmployeeOfInvoiceCollectionNewInvoice = em.merge(oldIdEmployeeOfInvoiceCollectionNewInvoice);
                    }
                }
            }
            for (Articles articlesCollectionNewArticles : articlesCollectionNew) {
                if (!articlesCollectionOld.contains(articlesCollectionNewArticles)) {
                    Employees oldOwnerOfArticlesCollectionNewArticles = articlesCollectionNewArticles.getOwner();
                    articlesCollectionNewArticles.setOwner(employees);
                    articlesCollectionNewArticles = em.merge(articlesCollectionNewArticles);
                    if (oldOwnerOfArticlesCollectionNewArticles != null && !oldOwnerOfArticlesCollectionNewArticles.equals(employees)) {
                        oldOwnerOfArticlesCollectionNewArticles.getArticlesCollection().remove(articlesCollectionNewArticles);
                        oldOwnerOfArticlesCollectionNewArticles = em.merge(oldOwnerOfArticlesCollectionNewArticles);
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
                Integer id = employees.getIdEmployee();
                if (findEmployees(id) == null) {
                    throw new NonexistentEntityException("The employees with id " + id + " no longer exists.");
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
            Employees employees;
            try {
                employees = em.getReference(Employees.class, id);
                employees.getIdEmployee();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employees with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Invoice> invoiceCollectionOrphanCheck = employees.getInvoiceCollection();
            for (Invoice invoiceCollectionOrphanCheckInvoice : invoiceCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employees (" + employees + ") cannot be destroyed since the Invoice " + invoiceCollectionOrphanCheckInvoice + " in its invoiceCollection field has a non-nullable idEmployee field.");
            }
            Collection<Articles> articlesCollectionOrphanCheck = employees.getArticlesCollection();
            for (Articles articlesCollectionOrphanCheckArticles : articlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employees (" + employees + ") cannot be destroyed since the Articles " + articlesCollectionOrphanCheckArticles + " in its articlesCollection field has a non-nullable owner field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(employees);
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

    public List<Employees> findEmployeesEntities() {
        return findEmployeesEntities(true, -1, -1);
    }

    public List<Employees> findEmployeesEntities(int maxResults, int firstResult) {
        return findEmployeesEntities(false, maxResults, firstResult);
    }

    private List<Employees> findEmployeesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employees.class));
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

    public Employees findEmployees(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employees.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employees> rt = cq.from(Employees.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
