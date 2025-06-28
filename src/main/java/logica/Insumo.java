/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author josepino
 */
@Entity
@Table(name = "insumo")
@NamedQueries({
    @NamedQuery(name = "Insumo.findAll", query = "SELECT i FROM Insumo i"),
    @NamedQuery(name = "Insumo.findByIdInsumo", query = "SELECT i FROM Insumo i WHERE i.idInsumo = :idInsumo"),
    @NamedQuery(name = "Insumo.findByNombreInsumo", query = "SELECT i FROM Insumo i WHERE i.nombreInsumo = :nombreInsumo"),
    @NamedQuery(name = "Insumo.findByUnidadMedida", query = "SELECT i FROM Insumo i WHERE i.unidadMedida = :unidadMedida"),
    @NamedQuery(name = "Insumo.findByCostoInsumo", query = "SELECT i FROM Insumo i WHERE i.costoInsumo = :costoInsumo"),
    @NamedQuery(name = "Insumo.findByStock", query = "SELECT i FROM Insumo i WHERE i.stock = :stock")})
public class Insumo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_insumo")
    private Integer idInsumo;
    @Basic(optional = false)
    @Column(name = "nombre_insumo")
    private String nombreInsumo;
    @Basic(optional = false)
    @Column(name = "unidad_medida")
    private String unidadMedida;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "costo_insumo")
    private BigDecimal costoInsumo;
    @Basic(optional = false)
    @Column(name = "stock")
    private BigDecimal stock;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idInsumo")
    private List<RecetaDetalle> recetaDetalleList;

    public Insumo() {
    }

    public Insumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public Insumo(Integer idInsumo, String nombreInsumo, String unidadMedida, BigDecimal costoInsumo, BigDecimal stock) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.unidadMedida = unidadMedida;
        this.costoInsumo = costoInsumo;
        this.stock = stock;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getCostoInsumo() {
        return costoInsumo;
    }

    public void setCostoInsumo(BigDecimal costoInsumo) {
        this.costoInsumo = costoInsumo;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public List<RecetaDetalle> getRecetaDetalleList() {
        return recetaDetalleList;
    }

    public void setRecetaDetalleList(List<RecetaDetalle> recetaDetalleList) {
        this.recetaDetalleList = recetaDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInsumo != null ? idInsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Insumo)) {
            return false;
        }
        Insumo other = (Insumo) object;
        if ((this.idInsumo == null && other.idInsumo != null) || (this.idInsumo != null && !this.idInsumo.equals(other.idInsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombreInsumo();
    }
    
}
