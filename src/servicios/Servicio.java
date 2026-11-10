package servicios;

import java.time.*;

public interface Servicio{
	String getNombre();
    String getDescripcion();
    Integer cupoDisponible(LocalDate fecha);
    boolean hayCupo(LocalDate fecIni, LocalDate fecFin,Integer cantidad);
    void reservarCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad);
    void restaurarCupo(LocalDate fecIni, LocalDate fecFin, Integer cantidad);
    double getPrecioBase();
    double getDescuentoPromocional();
    double getPrecioFinal();
    boolean validarServicio();
}
