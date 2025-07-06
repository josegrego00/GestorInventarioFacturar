package logica;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Insumo;
import logica.Receta;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-07-05T21:56:52", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(RecetaDetalle.class)
public class RecetaDetalle_ { 

    public static volatile SingularAttribute<RecetaDetalle, BigDecimal> costoInsumo;
    public static volatile SingularAttribute<RecetaDetalle, Insumo> idInsumo;
    public static volatile SingularAttribute<RecetaDetalle, Receta> idReceta;
    public static volatile SingularAttribute<RecetaDetalle, BigDecimal> cantidadInsumo;
    public static volatile SingularAttribute<RecetaDetalle, Integer> idRecetaDetalle;

}