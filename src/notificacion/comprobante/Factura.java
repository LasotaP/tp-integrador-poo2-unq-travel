package notificacion.comprobante;

import java.time.LocalDate;
import java.util.List;

import servicios.Servicio;
import reserva.Cliente;
import reserva.ItemReserva;

public class Factura extends Comprobante {

    public Factura(LocalDate fecha, Cliente cliente, List<ItemReserva> servicios,double monto) {
        super(fecha, cliente, monto);
    }

}
