/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import logica.Insumo;
import logica.Receta;

/**
 *
 * @author josepino
 */
public class ControladoraPersistencia {
    
    InsumoJpaController insumoJpaController= new InsumoJpaController();
    RecetaJpaController recetaJpaController= new RecetaJpaController();
    
    public void crearInsumo(Insumo insumo){
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
   
    
    
}
