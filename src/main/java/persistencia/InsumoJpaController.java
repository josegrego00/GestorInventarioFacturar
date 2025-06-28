/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.RecetaDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logica.Insumo;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class InsumoJpaController implements Serializable {

    public InsumoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public InsumoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("persistenciaPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Insumo insumo) {
        if (insumo.getRecetaDetalleList() == null) {
            insumo.setRecetaDetalleList(new ArrayList<RecetaDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RecetaDetalle> attachedRecetaDetalleList = new ArrayList<RecetaDetalle>();
            for (RecetaDetalle recetaDetalleListRecetaDetalleToAttach : insumo.getRecetaDetalleList()) {
                recetaDetalleListRecetaDetalleToAttach = em.getReference(recetaDetalleListRecetaDetalleToAttach.getClass(), recetaDetalleListRecetaDetalleToAttach.getIdRecetaDetalle());
                attachedRecetaDetalleList.add(recetaDetalleListRecetaDetalleToAttach);
            }
            insumo.setRecetaDetalleList(attachedRecetaDetalleList);
            em.persist(insumo);
            for (RecetaDetalle recetaDetalleListRecetaDetalle : insumo.getRecetaDetalleList()) {
                Insumo oldIdInsumoOfRecetaDetalleListRecetaDetalle = recetaDetalleListRecetaDetalle.getIdInsumo();
                recetaDetalleListRecetaDetalle.setIdInsumo(insumo);
                recetaDetalleListRecetaDetalle = em.merge(recetaDetalleListRecetaDetalle);
                if (oldIdInsumoOfRecetaDetalleListRecetaDetalle != null) {
                    oldIdInsumoOfRecetaDetalleListRecetaDetalle.getRecetaDetalleList().remove(recetaDetalleListRecetaDetalle);
                    oldIdInsumoOfRecetaDetalleListRecetaDetalle = em.merge(oldIdInsumoOfRecetaDetalleListRecetaDetalle);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Insumo insumo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo persistentInsumo = em.find(Insumo.class, insumo.getIdInsumo());
            List<RecetaDetalle> recetaDetalleListOld = persistentInsumo.getRecetaDetalleList();
            List<RecetaDetalle> recetaDetalleListNew = insumo.getRecetaDetalleList();
            List<String> illegalOrphanMessages = null;
            for (RecetaDetalle recetaDetalleListOldRecetaDetalle : recetaDetalleListOld) {
                if (!recetaDetalleListNew.contains(recetaDetalleListOldRecetaDetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecetaDetalle " + recetaDetalleListOldRecetaDetalle + " since its idInsumo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RecetaDetalle> attachedRecetaDetalleListNew = new ArrayList<RecetaDetalle>();
            for (RecetaDetalle recetaDetalleListNewRecetaDetalleToAttach : recetaDetalleListNew) {
                recetaDetalleListNewRecetaDetalleToAttach = em.getReference(recetaDetalleListNewRecetaDetalleToAttach.getClass(), recetaDetalleListNewRecetaDetalleToAttach.getIdRecetaDetalle());
                attachedRecetaDetalleListNew.add(recetaDetalleListNewRecetaDetalleToAttach);
            }
            recetaDetalleListNew = attachedRecetaDetalleListNew;
            insumo.setRecetaDetalleList(recetaDetalleListNew);
            insumo = em.merge(insumo);
            for (RecetaDetalle recetaDetalleListNewRecetaDetalle : recetaDetalleListNew) {
                if (!recetaDetalleListOld.contains(recetaDetalleListNewRecetaDetalle)) {
                    Insumo oldIdInsumoOfRecetaDetalleListNewRecetaDetalle = recetaDetalleListNewRecetaDetalle.getIdInsumo();
                    recetaDetalleListNewRecetaDetalle.setIdInsumo(insumo);
                    recetaDetalleListNewRecetaDetalle = em.merge(recetaDetalleListNewRecetaDetalle);
                    if (oldIdInsumoOfRecetaDetalleListNewRecetaDetalle != null && !oldIdInsumoOfRecetaDetalleListNewRecetaDetalle.equals(insumo)) {
                        oldIdInsumoOfRecetaDetalleListNewRecetaDetalle.getRecetaDetalleList().remove(recetaDetalleListNewRecetaDetalle);
                        oldIdInsumoOfRecetaDetalleListNewRecetaDetalle = em.merge(oldIdInsumoOfRecetaDetalleListNewRecetaDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = insumo.getIdInsumo();
                if (findInsumo(id) == null) {
                    throw new NonexistentEntityException("The insumo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo insumo;
            try {
                insumo = em.getReference(Insumo.class, id);
                insumo.getIdInsumo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The insumo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RecetaDetalle> recetaDetalleListOrphanCheck = insumo.getRecetaDetalleList();
            for (RecetaDetalle recetaDetalleListOrphanCheckRecetaDetalle : recetaDetalleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Insumo (" + insumo + ") cannot be destroyed since the RecetaDetalle " + recetaDetalleListOrphanCheckRecetaDetalle + " in its recetaDetalleList field has a non-nullable idInsumo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(insumo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Insumo> findInsumoEntities() {
        return findInsumoEntities(true, -1, -1);
    }

    public List<Insumo> findInsumoEntities(int maxResults, int firstResult) {
        return findInsumoEntities(false, maxResults, firstResult);
    }

    private List<Insumo> findInsumoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Insumo.class));
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

    public Insumo findInsumo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Insumo.class, id);
        } finally {
            em.close();
        }
    }

    public int getInsumoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Insumo> rt = cq.from(Insumo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Insumo buscarInsumoPorNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            // Usamos JPQL con parámetro nombrado para evitar SQL injection
            Query query = em.createQuery("SELECT i FROM Insumo i WHERE i.nombreInsumo = :nombre");
            query.setParameter("nombre", nombre);

            // Obtenemos el resultado único (o null si no existe)
            try {
                return (Insumo) query.getSingleResult();
            } catch (javax.persistence.NoResultException e) {
                return null; // No se encontró ningún insumo con ese nombre
            }
        } finally {
            em.close();
        }

    }

}
