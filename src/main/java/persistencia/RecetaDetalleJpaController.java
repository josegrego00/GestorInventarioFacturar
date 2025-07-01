/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.Insumo;
import logica.Receta;
import logica.RecetaDetalle;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class RecetaDetalleJpaController implements Serializable {

    public RecetaDetalleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    RecetaDetalleJpaController() {
        this.emf = Persistence.createEntityManagerFactory("persistenciaPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecetaDetalle recetaDetalle) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo idInsumo = recetaDetalle.getIdInsumo();
            if (idInsumo != null) {
                idInsumo = em.getReference(idInsumo.getClass(), idInsumo.getIdInsumo());
                recetaDetalle.setIdInsumo(idInsumo);
            }
            Receta idReceta = recetaDetalle.getIdReceta();
            if (idReceta != null) {
                idReceta = em.getReference(idReceta.getClass(), idReceta.getIdReceta());
                recetaDetalle.setIdReceta(idReceta);
            }
            em.persist(recetaDetalle);
            if (idInsumo != null) {
                idInsumo.getRecetaDetalleList().add(recetaDetalle);
                idInsumo = em.merge(idInsumo);
            }
            if (idReceta != null) {
                idReceta.getRecetaDetalleList().add(recetaDetalle);
                idReceta = em.merge(idReceta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecetaDetalle recetaDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecetaDetalle persistentRecetaDetalle = em.find(RecetaDetalle.class, recetaDetalle.getIdRecetaDetalle());
            Insumo idInsumoOld = persistentRecetaDetalle.getIdInsumo();
            Insumo idInsumoNew = recetaDetalle.getIdInsumo();
            Receta idRecetaOld = persistentRecetaDetalle.getIdReceta();
            Receta idRecetaNew = recetaDetalle.getIdReceta();
            if (idInsumoNew != null) {
                idInsumoNew = em.getReference(idInsumoNew.getClass(), idInsumoNew.getIdInsumo());
                recetaDetalle.setIdInsumo(idInsumoNew);
            }
            if (idRecetaNew != null) {
                idRecetaNew = em.getReference(idRecetaNew.getClass(), idRecetaNew.getIdReceta());
                recetaDetalle.setIdReceta(idRecetaNew);
            }
            recetaDetalle = em.merge(recetaDetalle);
            if (idInsumoOld != null && !idInsumoOld.equals(idInsumoNew)) {
                idInsumoOld.getRecetaDetalleList().remove(recetaDetalle);
                idInsumoOld = em.merge(idInsumoOld);
            }
            if (idInsumoNew != null && !idInsumoNew.equals(idInsumoOld)) {
                idInsumoNew.getRecetaDetalleList().add(recetaDetalle);
                idInsumoNew = em.merge(idInsumoNew);
            }
            if (idRecetaOld != null && !idRecetaOld.equals(idRecetaNew)) {
                idRecetaOld.getRecetaDetalleList().remove(recetaDetalle);
                idRecetaOld = em.merge(idRecetaOld);
            }
            if (idRecetaNew != null && !idRecetaNew.equals(idRecetaOld)) {
                idRecetaNew.getRecetaDetalleList().add(recetaDetalle);
                idRecetaNew = em.merge(idRecetaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recetaDetalle.getIdRecetaDetalle();
                if (findRecetaDetalle(id) == null) {
                    throw new NonexistentEntityException("The recetaDetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecetaDetalle recetaDetalle;
            try {
                recetaDetalle = em.getReference(RecetaDetalle.class, id);
                recetaDetalle.getIdRecetaDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recetaDetalle with id " + id + " no longer exists.", enfe);
            }
            Insumo idInsumo = recetaDetalle.getIdInsumo();
            if (idInsumo != null) {
                idInsumo.getRecetaDetalleList().remove(recetaDetalle);
                idInsumo = em.merge(idInsumo);
            }
            Receta idReceta = recetaDetalle.getIdReceta();
            if (idReceta != null) {
                idReceta.getRecetaDetalleList().remove(recetaDetalle);
                idReceta = em.merge(idReceta);
            }
            em.remove(recetaDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecetaDetalle> findRecetaDetalleEntities() {
        return findRecetaDetalleEntities(true, -1, -1);
    }

    public List<RecetaDetalle> findRecetaDetalleEntities(int maxResults, int firstResult) {
        return findRecetaDetalleEntities(false, maxResults, firstResult);
    }

    private List<RecetaDetalle> findRecetaDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecetaDetalle.class));
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

    public RecetaDetalle findRecetaDetalle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecetaDetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecetaDetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecetaDetalle> rt = cq.from(RecetaDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void insertarDetalleRecetaConSP(int idReceta, int idInsumo, BigDecimal cantidadInsumo) {
        EntityManager em = emf.createEntityManager(); // Asegurate de tener 'emf' definido como EntityManagerFactory

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("insertar_receta_detalle");

            query.registerStoredProcedureParameter("p_id_receta", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_insumo", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_cantidad_insumo", BigDecimal.class, ParameterMode.IN);

            query.setParameter("p_id_receta", idReceta);
            query.setParameter("p_id_insumo", idInsumo);
            query.setParameter("p_cantidad_insumo", cantidadInsumo);

            query.execute();

        } catch (Exception e) {
            System.err.println("Error al ejecutar el procedimiento almacenado: " + e.getMessage());
            throw e; // o manejalo como prefieras
        } finally {
            em.close();
        }
    }

}
