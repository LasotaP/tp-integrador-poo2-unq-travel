package servicios;

import java.util.ArrayList;
import java.util.List;

public class PaqueteTuristico implements Servicio {
    private String nombre;
    private String descripcion;
    private double descuento;
    private List<Servicio> servicios;

    // CONSTRUCTOR
    public PaqueteTuristico (String nombre, String descripcion, double descuento){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.servicios = new ArrayList<>();

        validarPaquete();
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
    public double getPrecioBaseCalculado() {
        return getPrecioBaseTotal() * (1 - (descuento / 100));
    }

    private double getPrecioBaseTotal() {
        return servicios.stream()
                        .mapToDouble(s -> s.getPrecioBaseCalculado())
                        .sum();
    }

    // HELPERS
    public void addServicio(Servicio servicio){
        servicios.add(servicio);
    }

    // VALIDACION
    private void validarPaquete() {
        if(nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Error: nombre invalido");
        }
        if(descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Error: descripcion invalida");
        }
        if(descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("Error: descuento invalido");
        }
    }

}
