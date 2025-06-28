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
@Table(name = "receta")
@NamedQueries({
    @NamedQuery(name = "Receta.findAll", query = "SELECT r FROM Receta r"),
    @NamedQuery(name = "Receta.findByIdReceta", query = "SELECT r FROM Receta r WHERE r.idReceta = :idReceta"),
    @NamedQuery(name = "Receta.findByNombreReceta", query = "SELECT r FROM Receta r WHERE r.nombreReceta = :nombreReceta"),
    @NamedQuery(name = "Receta.findByCostoReceta", query = "SELECT r FROM Receta r WHERE r.costoReceta = :costoReceta")})
public class Receta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_receta")
    private Integer idReceta;
    @Basic(optional = false)
    @Column(name = "nombre_receta")
    private String nombreReceta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "costo_receta")
    private BigDecimal costoReceta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReceta")
    private List<RecetaDetalle> recetaDetalleList;
    @OneToMany(mappedBy = "idReceta")
    private List<Producto> productoList;

    public Receta() {
    }

    public Receta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public Receta(Integer idReceta, String nombreReceta, BigDecimal costoReceta) {
        this.idReceta = idReceta;
        this.nombreReceta = nombreReceta;
        this.costoReceta = costoReceta;
    }

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public BigDecimal getCostoReceta() {
        return costoReceta;
    }

    public void setCostoReceta(BigDecimal costoReceta) {
        this.costoReceta = costoReceta;
    }

    public List<RecetaDetalle> getRecetaDetalleList() {
        return recetaDetalleList;
    }

    public void setRecetaDetalleList(List<RecetaDetalle> recetaDetalleList) {
        this.recetaDetalleList = recetaDetalleList;
    }

    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReceta != null ? idReceta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Receta)) {
            return false;
        }
        Receta other = (Receta) object;
        if ((this.idReceta == null && other.idReceta != null) || (this.idReceta != null && !this.idReceta.equals(other.idReceta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "logica.Receta[ idReceta=" + idReceta + " ]";
    }
    
}
