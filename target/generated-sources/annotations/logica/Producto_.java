package logica;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.CategoriaProducto;
import logica.Receta;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-07-11T11:11:52", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Producto.class)
public class Producto_ { 

    public static volatile SingularAttribute<Producto, Integer> stockMinimo;
    public static volatile SingularAttribute<Producto, BigDecimal> costo;
    public static volatile SingularAttribute<Producto, Boolean> esFabricado;
    public static volatile SingularAttribute<Producto, Receta> idReceta;
    public static volatile SingularAttribute<Producto, Integer> idProducto;
    public static volatile SingularAttribute<Producto, BigDecimal> precioVenta;
    public static volatile SingularAttribute<Producto, Integer> stock;
    public static volatile SingularAttribute<Producto, CategoriaProducto> idCategoria;
    public static volatile SingularAttribute<Producto, String> nombreProducto;
    public static volatile SingularAttribute<Producto, Boolean> activo;

}