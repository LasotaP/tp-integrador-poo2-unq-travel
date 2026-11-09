package notificacion.comprobante;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import servicios.Servicio;
import reserva.Cliente;

public abstract class Comprobante {

    private final String numero;
    private final LocalDate fecha;
    private final Cliente cliente;
    private final List<Servicio> servicios;


    protected Comprobante(String numero, LocalDate fecha, Cliente cliente) {
        this.numero = Objects.requireNonNull(numero, "El numero no puede ser nulo");
        this.fecha = Objects.requireNonNull(fecha, "La fecha no puede ser nula");
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        this.servicios = new ArrayList<>();
    }

    public String getNumero() { return numero; }
    public LocalDate getFecha() { return fecha; }
    public Cliente getCliente() { return cliente; }
    public List<Servicio> getItems() { return servicios; }
}
