/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import logica.Insumo;
import logica.Receta;
import logica.RecetaDetalle;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class ControladoraPersistencia {

    InsumoJpaController insumoJpaController = new InsumoJpaController();
    RecetaJpaController recetaJpaController = new RecetaJpaController();
    RecetaDetalleJpaController recetaDetalleJpaController = new RecetaDetalleJpaController();

    public void crearInsumo(Insumo insumo) {
        insumoJpaController.create(insumo);
    }

    public Insumo esNombrePermitido(String nombre) {
        return insumoJpaController.buscarInsumoPorNombre(nombre);
    }

    public Receta esNombreRecetaPermitido(String nombre) {
        return recetaJpaController.buscarRecetaPorNombre(nombre);
    }

    public List<Insumo> listarInsumos() {
        return insumoJpaController.findInsumoEntities();
    }

    public void crearReceta(Receta receta) {
        recetaJpaController.create(receta);
    }

    public Receta buscarReceta(String nombreReceta) {
        return recetaJpaController.buscarRecetaPorNombre(nombreReceta);
    }

    public void insertarDetalleRecetaConSP(int idReceta, int idInsumo, BigDecimal cantidadInsumo) {
        recetaDetalleJpaController.insertarDetalleRecetaConSP(idReceta, idInsumo, cantidadInsumo);
    }

    public Insumo buscarInsumo(String nombreInsumo) {
        return insumoJpaController.buscarInsumoPorNombre(nombreInsumo);
    }

    public Insumo buscarInsumoPorId(int id) {
          return insumoJpaController.findInsumo(id);
    }

    public void modificarInsumo(Insumo insumoModificar) {
        try {
            insumoJpaController.edit(insumoModificar);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarInsumoPorId(int id) {
        try {
            insumoJpaController.destroy(id);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Receta> listarRecetas() {
        return recetaJpaController.findRecetaEntities();
    }

    public void eliminarRecetaPorId(int id) {
        try {
            recetaJpaController.destroy(id);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<RecetaDetalle> obtenerDetalleReceta() {
        return recetaDetalleJpaController.findRecetaDetalleEntities();
    }

   

}
