package unqTravel.catalogo;

import java.time.*;

import unqTravel.reportes.Visitante;
import unqTravel.servicios.catalogo.CriterioBusqueda;

public interface Servicio{
	String getNombre();
    String getDescripcion();
    Integer cupoDisponible(LocalDate fecha);
    Categoria getCategoria();
    boolean hayCupo(LocalDate fecIni, LocalDate fecFin,Integer cantidad);
    void reservarCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad);
    void restaurarCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad);
    double getPrecioBase();
    double getDescuentoPromocional();
    double getPrecioFinal();
    boolean validarServicio();
    void aceptar(Visitante visitante);
}
