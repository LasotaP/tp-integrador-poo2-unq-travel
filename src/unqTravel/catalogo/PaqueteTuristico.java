package unqTravel.catalogo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import unqTravel.catalogo.atributos.ServicioInvalidoException;
import unqTravel.reportes.Visitante;

public class PaqueteTuristico implements Servicio {
    private String nombre;
    private String descripcion;
    private double descuentoPromocional; // 0..1
    private Categoria categoria;
    private List<Servicio> servicios;

    // CONSTRUCTOR
    public PaqueteTuristico (String nombre, Categoria categoria, String descripcion, double descuentoPromocional){
        validarTexto(nombre, "nombre");
        validarTexto(descripcion, "descripcion");
        validarFraccion(descuentoPromocional, "descuentoPromocional");
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.descuentoPromocional = descuentoPromocional;
        this.servicios = new ArrayList<>();
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
    public Integer cupoDisponible(LocalDate fecha) {
        return servicios.stream()
                .mapToInt(s -> s.cupoDisponible(fecha))
                .min()
                .orElse(0);
    }
    
    @Override
    public double getPrecioFinal() {
        return getPrecioBase() * (1.0 - descuentoPromocional);
    }

    @Override
    public double getPrecioBase() {
        return servicios.stream().mapToDouble(Servicio::getPrecioFinal).sum();
    }
    
    @Override
    public boolean hayCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
        return !servicios.isEmpty()
                && servicios.stream().allMatch(s -> s.hayCupo(fecIni, fecFin, cantidad));
    }
    
    @Override
    public void reservarCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
        if (!hayCupo(fecIni, fecFin, cantidad)) {
            throw new IllegalArgumentException("No hay cupo suficiente");
        }
        List<Servicio> reservados = new ArrayList<>();
        try {
            for (Servicio s : servicios) {
                s.reservarCupo(fecIni, fecFin, cantidad);
                reservados.add(s);
            }
        } catch (RuntimeException e) {
            reservados.forEach(s -> s.restaurarCupo(fecIni, fecFin, cantidad));
            throw e;
        };
    }
    
    @Override
    public void restaurarCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad) {
        servicios.forEach(s -> s.restaurarCupo(fecIni, fecFin, cantidad));
    }

    @Override
    public Categoria getCategoria() {
    	return categoria;
    }
    
    // HELPERS
    public void addServicio(Servicio servicio) {
        if (servicio == null) {
            throw new ServicioInvalidoException("El servicio no puede ser nulo");
        }
        if (servicio.contiene(this)) {
            throw new ServicioInvalidoException("Un paquete no puede contenerse a sí mismo");
        }
        servicios.add(servicio);
    }
    
    @Override
    public boolean contiene(Servicio servicio) {
        return this == servicio || servicios.stream().anyMatch(s -> s.contiene(servicio));
    }

    // VALIDACION

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ServicioInvalidoException("El campo " + campo + " es obligatorio");
        }
    }
    
    private void validarFraccion(double valor, String campo) {
        if (valor < 0 || valor > 1) {
            throw new ServicioInvalidoException("El campo " + campo + " debe estar entre 0 y 1");
        }
    }

    @Override
    public double getDescuentoPromocional() {
        return descuentoPromocional;
    }

    @Override
    public boolean validarServicio() {
        return !servicios.isEmpty()
            && servicios.stream().allMatch(Servicio::validarServicio);
    }
    
    
    @Override
    public void aceptar(Visitante visitante) {
        visitante.visitarPaquete(this);
        servicios.forEach(s -> s.aceptar(visitante));
    }

}
