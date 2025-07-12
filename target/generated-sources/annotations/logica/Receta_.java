package logica;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Producto;
import logica.RecetaDetalle;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-07-12T12:10:22", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Receta.class)
public class Receta_ { 

    public static volatile ListAttribute<Receta, RecetaDetalle> recetaDetalleList;
    public static volatile ListAttribute<Receta, Producto> productoList;
    public static volatile SingularAttribute<Receta, String> nombreReceta;
    public static volatile SingularAttribute<Receta, Integer> idReceta;
    public static volatile SingularAttribute<Receta, BigDecimal> costoReceta;

}