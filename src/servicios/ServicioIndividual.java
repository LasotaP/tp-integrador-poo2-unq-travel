package servicios;

import java.time.LocalDate;

import java.util.Collections;
import java.util.HashMap;

import java.util.Map;


public class ServicioIndividual implements Servicio {
    private String id;
    private String nombre;
    private String descripcion;
    private String proveedor;
    private Categoria categoria;
    private double precioBase;
    private Cupo cupo;
    private final Map<String, AtributoDinamico> atributosDinamicos;
    private final double descuentoPromocional; // fraccion entre 0 y 1 (0.10 = 10 %)
    
    // CONSTRUCTOR
    public ServicioIndividual(String id, String nombre, String descripcion, String proveedor, Categoria categoria, double precioBase, 
    		Cupo cupo, double descuentoPromocional) {
        validarTexto(id, "id");
        validarTexto(nombre, "nombre");
        validarTexto(descripcion, "descripcion");
        validarTexto(proveedor, "proveedor");
        validarNoNulo(categoria, "categoria");
        validarNoNulo(cupo, "cupo");
        validarPositivo(precioBase, "precioBase");
        validarFraccion(descuentoPromocional, "descuentoPromocional");

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.cupo = cupo;
        this.precioBase = precioBase;
        this.descuentoPromocional = descuentoPromocional;
        this.atributosDinamicos = new HashMap<>();
    }

    // GETTERS
    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public double getPrecioFinal() {
        return precioBase * (1.0 - descuentoPromocional);
    }
    
    @Override
	public Integer cupoDisponible(LocalDate fecha) {
    	return this.cupo.getDisponibilidad(fecha);
	}
    
    @Override
    public boolean hayCupo(LocalDate fecIni, LocalDate fecFin,Integer cantidad) {
    	return  this.cupo.hayDisponibilidad(fecIni,fecFin,cantidad);
    }
    // HELPERS
    
    // Agrega el atributo con su valor. Si ya existe uno con ese nombre lo reemplaza.
    public void agregarAtributoDinamico(AtributoDinamico atributo) {
        if (atributo == null) {
            throw new ServicioInvalidoException("El atributo no puede ser nulo");
        }
        atributosDinamicos.put(atributo.getNombre(), atributo);
    }
    
    @Override
    public void reservarCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
    	this.getCupo().reservar(fecIni, fecFin ,cantidad);
    }
    
    @Override
    public void restaurarCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
    	this.getCupo().restaurar(fecIni, fecFin ,cantidad);
    }
    
    private Cupo getCupo() {
    	return this.cupo;
    }

    // VALIDACION
    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ServicioInvalidoException("El campo " + campo + " es obligatorio");
        }
    }

    private void validarNoNulo(Object valor, String campo) {
        if (valor == null) {
            throw new ServicioInvalidoException("El campo " + campo + " es obligatorio");
        }
    }

    private void validarPositivo(double valor, String campo) {
        if (valor <= 0) {
            throw new ServicioInvalidoException("El campo " + campo + " debe ser positivo");
        }
    }

    private void validarFraccion(double valor, String campo) {
        if (valor < 0 || valor > 1) {
            throw new ServicioInvalidoException("El campo " + campo + " debe estar entre 0 y 1");
        }
    }
    
    
    @Override
    public double getPrecioBase() {
        return precioBase;
    }

    @Override
    public double getDescuentoPromocional() {
        return descuentoPromocional;
    }

    public Map<String, AtributoDinamico> getAtributosDinamicos() {
        return Collections.unmodifiableMap(atributosDinamicos);
    }

    @Override
    public boolean validarServicio() {
        return atributosDinamicos.values().stream()
                .allMatch(AtributoDinamico::esValido);
    }

	public String getId() {
		return id;
	}

	public String getProveedor() {
		return proveedor;
	}

	public Categoria getCategoria() {
		return categoria;
	}
    	
}

