package servicios;

public interface Servicio{
	String getNombre();
    String getDescripcion();
    double getPrecioBaseCalculado();
    Integer cupoDisponible();
    boolean hayCupo(Integer cantidad);
    void decrementarCupo(Integer cantidad);
    void aumentarCupo(Integer cantidad);
}
