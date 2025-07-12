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
import logica.Producto;
import logica.Receta;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class RecetaJpaController implements Serializable {

    public RecetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public RecetaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("persistenciaPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Receta receta) {
        if (receta.getRecetaDetalleList() == null) {
            receta.setRecetaDetalleList(new ArrayList<RecetaDetalle>());
        }
        if (receta.getProductoList() == null) {
            receta.setProductoList(new ArrayList<Producto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RecetaDetalle> attachedRecetaDetalleList = new ArrayList<RecetaDetalle>();
            for (RecetaDetalle recetaDetalleListRecetaDetalleToAttach : receta.getRecetaDetalleList()) {
                recetaDetalleListRecetaDetalleToAttach = em.getReference(recetaDetalleListRecetaDetalleToAttach.getClass(), recetaDetalleListRecetaDetalleToAttach.getIdRecetaDetalle());
                attachedRecetaDetalleList.add(recetaDetalleListRecetaDetalleToAttach);
            }
            receta.setRecetaDetalleList(attachedRecetaDetalleList);
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : receta.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getIdProducto());
                attachedProductoList.add(productoListProductoToAttach);
            }
            receta.setProductoList(attachedProductoList);
            em.persist(receta);
            for (RecetaDetalle recetaDetalleListRecetaDetalle : receta.getRecetaDetalleList()) {
                Receta oldIdRecetaOfRecetaDetalleListRecetaDetalle = recetaDetalleListRecetaDetalle.getIdReceta();
                recetaDetalleListRecetaDetalle.setIdReceta(receta);
                recetaDetalleListRecetaDetalle = em.merge(recetaDetalleListRecetaDetalle);
                if (oldIdRecetaOfRecetaDetalleListRecetaDetalle != null) {
                    oldIdRecetaOfRecetaDetalleListRecetaDetalle.getRecetaDetalleList().remove(recetaDetalleListRecetaDetalle);
                    oldIdRecetaOfRecetaDetalleListRecetaDetalle = em.merge(oldIdRecetaOfRecetaDetalleListRecetaDetalle);
                }
            }
            for (Producto productoListProducto : receta.getProductoList()) {
                Receta oldIdRecetaOfProductoListProducto = productoListProducto.getIdReceta();
                productoListProducto.setIdReceta(receta);
                productoListProducto = em.merge(productoListProducto);
                if (oldIdRecetaOfProductoListProducto != null) {
                    oldIdRecetaOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldIdRecetaOfProductoListProducto = em.merge(oldIdRecetaOfProductoListProducto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Receta receta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Receta persistentReceta = em.find(Receta.class, receta.getIdReceta());
            List<RecetaDetalle> recetaDetalleListOld = persistentReceta.getRecetaDetalleList();
            List<RecetaDetalle> recetaDetalleListNew = receta.getRecetaDetalleList();
            List<Producto> productoListOld = persistentReceta.getProductoList();
            List<Producto> productoListNew = receta.getProductoList();
            List<String> illegalOrphanMessages = null;
            for (RecetaDetalle recetaDetalleListOldRecetaDetalle : recetaDetalleListOld) {
                if (!recetaDetalleListNew.contains(recetaDetalleListOldRecetaDetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecetaDetalle " + recetaDetalleListOldRecetaDetalle + " since its idReceta field is not nullable.");
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
            receta.setRecetaDetalleList(recetaDetalleListNew);
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getIdProducto());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            receta.setProductoList(productoListNew);
            receta = em.merge(receta);
            for (RecetaDetalle recetaDetalleListNewRecetaDetalle : recetaDetalleListNew) {
                if (!recetaDetalleListOld.contains(recetaDetalleListNewRecetaDetalle)) {
                    Receta oldIdRecetaOfRecetaDetalleListNewRecetaDetalle = recetaDetalleListNewRecetaDetalle.getIdReceta();
                    recetaDetalleListNewRecetaDetalle.setIdReceta(receta);
                    recetaDetalleListNewRecetaDetalle = em.merge(recetaDetalleListNewRecetaDetalle);
                    if (oldIdRecetaOfRecetaDetalleListNewRecetaDetalle != null && !oldIdRecetaOfRecetaDetalleListNewRecetaDetalle.equals(receta)) {
                        oldIdRecetaOfRecetaDetalleListNewRecetaDetalle.getRecetaDetalleList().remove(recetaDetalleListNewRecetaDetalle);
                        oldIdRecetaOfRecetaDetalleListNewRecetaDetalle = em.merge(oldIdRecetaOfRecetaDetalleListNewRecetaDetalle);
                    }
                }
            }
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    productoListOldProducto.setIdReceta(null);
                    productoListOldProducto = em.merge(productoListOldProducto);
                }
            }
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    Receta oldIdRecetaOfProductoListNewProducto = productoListNewProducto.getIdReceta();
                    productoListNewProducto.setIdReceta(receta);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldIdRecetaOfProductoListNewProducto != null && !oldIdRecetaOfProductoListNewProducto.equals(receta)) {
                        oldIdRecetaOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldIdRecetaOfProductoListNewProducto = em.merge(oldIdRecetaOfProductoListNewProducto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = receta.getIdReceta();
                if (findReceta(id) == null) {
                    throw new NonexistentEntityException("The receta with id " + id + " no longer exists.");
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
            Receta receta;
            try {
                receta = em.getReference(Receta.class, id);
                receta.getIdReceta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The receta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RecetaDetalle> recetaDetalleListOrphanCheck = receta.getRecetaDetalleList();
            for (RecetaDetalle recetaDetalleListOrphanCheckRecetaDetalle : recetaDetalleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Receta (" + receta + ") cannot be destroyed since the RecetaDetalle " + recetaDetalleListOrphanCheckRecetaDetalle + " in its recetaDetalleList field has a non-nullable idReceta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Producto> productoList = receta.getProductoList();
            for (Producto productoListProducto : productoList) {
                productoListProducto.setIdReceta(null);
                productoListProducto = em.merge(productoListProducto);
            }
            em.remove(receta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Receta> findRecetaEntities() {
        return findRecetaEntities(true, -1, -1);
    }

    public List<Receta> findRecetaEntities(int maxResults, int firstResult) {
        return findRecetaEntities(false, maxResults, firstResult);
    }

    private List<Receta> findRecetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Receta.class));
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

    public Receta findReceta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Receta.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Receta> rt = cq.from(Receta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Receta buscarRecetaPorNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            // Usamos JPQL con parámetro nombrado para evitar SQL injection
            Query query = em.createQuery("SELECT r FROM Receta r WHERE r.nombreReceta= :nombre");
            query.setParameter("nombre", nombre);
            // Obtenemos el resultado único (o null si no existe)
            try {
                return (Receta) query.setHint("javax.persistence.cache.storeMode", "REFRESH").getSingleResult();
            } catch (javax.persistence.NoResultException e) {
                return null; // No se encontró ningúna Receta con ese nombre
            }
        } finally {
            em.close();
        }

    }

}
