package servicios;

import java.time.*;

public interface Servicio{
	String getNombre();
    String getDescripcion();
    Integer cupoDisponible(LocalDate fecha);
    boolean hayCupo(LocalDate fecha,Integer cantidad);
    void decrementarCupo(LocalDate fecha, Integer cantidad);
    void aumentarCupo(LocalDate fecha,Integer cantidad);
    double getPrecioBase();
    double getDescuentoPromocional();
    double getPrecioFinal();
    boolean validarServicio();
}
