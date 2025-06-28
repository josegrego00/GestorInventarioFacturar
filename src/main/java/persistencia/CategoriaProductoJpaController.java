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
import logica.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import logica.CategoriaProducto;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class CategoriaProductoJpaController implements Serializable {

    public CategoriaProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CategoriaProducto categoriaProducto) {
        if (categoriaProducto.getProductoList() == null) {
            categoriaProducto.setProductoList(new ArrayList<Producto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : categoriaProducto.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getIdProducto());
                attachedProductoList.add(productoListProductoToAttach);
            }
            categoriaProducto.setProductoList(attachedProductoList);
            em.persist(categoriaProducto);
            for (Producto productoListProducto : categoriaProducto.getProductoList()) {
                CategoriaProducto oldIdCategoriaOfProductoListProducto = productoListProducto.getIdCategoria();
                productoListProducto.setIdCategoria(categoriaProducto);
                productoListProducto = em.merge(productoListProducto);
                if (oldIdCategoriaOfProductoListProducto != null) {
                    oldIdCategoriaOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldIdCategoriaOfProductoListProducto = em.merge(oldIdCategoriaOfProductoListProducto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CategoriaProducto categoriaProducto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoriaProducto persistentCategoriaProducto = em.find(CategoriaProducto.class, categoriaProducto.getIdCategoria());
            List<Producto> productoListOld = persistentCategoriaProducto.getProductoList();
            List<Producto> productoListNew = categoriaProducto.getProductoList();
            List<String> illegalOrphanMessages = null;
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Producto " + productoListOldProducto + " since its idCategoria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getIdProducto());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            categoriaProducto.setProductoList(productoListNew);
            categoriaProducto = em.merge(categoriaProducto);
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    CategoriaProducto oldIdCategoriaOfProductoListNewProducto = productoListNewProducto.getIdCategoria();
                    productoListNewProducto.setIdCategoria(categoriaProducto);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldIdCategoriaOfProductoListNewProducto != null && !oldIdCategoriaOfProductoListNewProducto.equals(categoriaProducto)) {
                        oldIdCategoriaOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldIdCategoriaOfProductoListNewProducto = em.merge(oldIdCategoriaOfProductoListNewProducto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoriaProducto.getIdCategoria();
                if (findCategoriaProducto(id) == null) {
                    throw new NonexistentEntityException("The categoriaProducto with id " + id + " no longer exists.");
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
            CategoriaProducto categoriaProducto;
            try {
                categoriaProducto = em.getReference(CategoriaProducto.class, id);
                categoriaProducto.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoriaProducto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Producto> productoListOrphanCheck = categoriaProducto.getProductoList();
            for (Producto productoListOrphanCheckProducto : productoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CategoriaProducto (" + categoriaProducto + ") cannot be destroyed since the Producto " + productoListOrphanCheckProducto + " in its productoList field has a non-nullable idCategoria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoriaProducto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CategoriaProducto> findCategoriaProductoEntities() {
        return findCategoriaProductoEntities(true, -1, -1);
    }

    public List<CategoriaProducto> findCategoriaProductoEntities(int maxResults, int firstResult) {
        return findCategoriaProductoEntities(false, maxResults, firstResult);
    }

    private List<CategoriaProducto> findCategoriaProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CategoriaProducto.class));
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

    public CategoriaProducto findCategoriaProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategoriaProducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CategoriaProducto> rt = cq.from(CategoriaProducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
