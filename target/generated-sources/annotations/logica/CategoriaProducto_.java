package logica;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Producto;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-06-28T10:06:53", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(CategoriaProducto.class)
public class CategoriaProducto_ { 

    public static volatile ListAttribute<CategoriaProducto, Producto> productoList;
    public static volatile SingularAttribute<CategoriaProducto, Integer> idCategoria;
    public static volatile SingularAttribute<CategoriaProducto, String> nombreCategoria;

}