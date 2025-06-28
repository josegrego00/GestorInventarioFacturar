/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author josepino
 */
@Entity
@Table(name = "receta_detalle")
@NamedQueries({
    @NamedQuery(name = "RecetaDetalle.findAll", query = "SELECT r FROM RecetaDetalle r"),
    @NamedQuery(name = "RecetaDetalle.findByIdRecetaDetalle", query = "SELECT r FROM RecetaDetalle r WHERE r.idRecetaDetalle = :idRecetaDetalle"),
    @NamedQuery(name = "RecetaDetalle.findByCantidadInsumo", query = "SELECT r FROM RecetaDetalle r WHERE r.cantidadInsumo = :cantidadInsumo"),
    @NamedQuery(name = "RecetaDetalle.findByCostoInsumo", query = "SELECT r FROM RecetaDetalle r WHERE r.costoInsumo = :costoInsumo")})
public class RecetaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_receta_detalle")
    private Integer idRecetaDetalle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "cantidad_insumo")
    private BigDecimal cantidadInsumo;
    @Basic(optional = false)
    @Column(name = "costo_insumo")
    private BigDecimal costoInsumo;
    @JoinColumn(name = "id_insumo", referencedColumnName = "id_insumo")
    @ManyToOne(optional = false)
    private Insumo idInsumo;
    @JoinColumn(name = "id_receta", referencedColumnName = "id_receta")
    @ManyToOne(optional = false)
    private Receta idReceta;

    public RecetaDetalle() {
    }

    public RecetaDetalle(Integer idRecetaDetalle) {
        this.idRecetaDetalle = idRecetaDetalle;
    }

    public RecetaDetalle(Integer idRecetaDetalle, BigDecimal cantidadInsumo, BigDecimal costoInsumo) {
        this.idRecetaDetalle = idRecetaDetalle;
        this.cantidadInsumo = cantidadInsumo;
        this.costoInsumo = costoInsumo;
    }

    public Integer getIdRecetaDetalle() {
        return idRecetaDetalle;
    }

    public void setIdRecetaDetalle(Integer idRecetaDetalle) {
        this.idRecetaDetalle = idRecetaDetalle;
    }

    public BigDecimal getCantidadInsumo() {
        return cantidadInsumo;
    }

    public void setCantidadInsumo(BigDecimal cantidadInsumo) {
        this.cantidadInsumo = cantidadInsumo;
    }

    public BigDecimal getCostoInsumo() {
        return costoInsumo;
    }

    public void setCostoInsumo(BigDecimal costoInsumo) {
        this.costoInsumo = costoInsumo;
    }

    public Insumo getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Insumo idInsumo) {
        this.idInsumo = idInsumo;
    }

    public Receta getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Receta idReceta) {
        this.idReceta = idReceta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecetaDetalle != null ? idRecetaDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecetaDetalle)) {
            return false;
        }
        RecetaDetalle other = (RecetaDetalle) object;
        if ((this.idRecetaDetalle == null && other.idRecetaDetalle != null) || (this.idRecetaDetalle != null && !this.idRecetaDetalle.equals(other.idRecetaDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "logica.RecetaDetalle[ idRecetaDetalle=" + idRecetaDetalle + " ]";
    }
    
}
