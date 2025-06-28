package logica;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.RecetaDetalle;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-06-28T10:06:53", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Insumo.class)
public class Insumo_ { 

    public static volatile SingularAttribute<Insumo, BigDecimal> costoInsumo;
    public static volatile SingularAttribute<Insumo, Integer> idInsumo;
    public static volatile ListAttribute<Insumo, RecetaDetalle> recetaDetalleList;
    public static volatile SingularAttribute<Insumo, String> nombreInsumo;
    public static volatile SingularAttribute<Insumo, String> unidadMedida;
    public static volatile SingularAttribute<Insumo, BigDecimal> stock;

}