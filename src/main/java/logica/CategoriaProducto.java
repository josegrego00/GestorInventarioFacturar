/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.io.Serializable;
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
@Table(name = "categoria_producto")
@NamedQueries({
    @NamedQuery(name = "CategoriaProducto.findAll", query = "SELECT c FROM CategoriaProducto c"),
    @NamedQuery(name = "CategoriaProducto.findByIdCategoria", query = "SELECT c FROM CategoriaProducto c WHERE c.idCategoria = :idCategoria"),
    @NamedQuery(name = "CategoriaProducto.findByNombreCategoria", query = "SELECT c FROM CategoriaProducto c WHERE c.nombreCategoria = :nombreCategoria")})
public class CategoriaProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_categoria")
    private Integer idCategoria;
    @Basic(optional = false)
    @Column(name = "nombre_categoria")
    private String nombreCategoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCategoria")
    private List<Producto> productoList;

    public CategoriaProducto() {
    }

    public CategoriaProducto(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public CategoriaProducto(Integer idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
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
        hash += (idCategoria != null ? idCategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriaProducto)) {
            return false;
        }
        CategoriaProducto other = (CategoriaProducto) object;
        if ((this.idCategoria == null && other.idCategoria != null) || (this.idCategoria != null && !this.idCategoria.equals(other.idCategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "logica.CategoriaProducto[ idCategoria=" + idCategoria + " ]";
    }
    
}
