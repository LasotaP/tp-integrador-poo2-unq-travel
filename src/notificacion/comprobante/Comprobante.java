package notificacion.comprobante;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import servicios.Servicio;
import reserva.Cliente;
import reserva.ItemReserva;

public abstract class Comprobante {

    private final LocalDate fecha;
    private final Cliente cliente;
    private final List<ItemReserva> itemsReserva;
    private double monto;


    public Comprobante(LocalDate fecha, Cliente cliente, double monto) {
        this.fecha = Objects.requireNonNull(fecha, "La fecha no puede ser nula");
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        this.itemsReserva = new ArrayList<>();
        this.monto = monto;
        
    }

    public LocalDate getFecha() { return fecha; }
    public Cliente getCliente() { return cliente; }
    public List<ItemReserva> getItems() { return itemsReserva; }
}
