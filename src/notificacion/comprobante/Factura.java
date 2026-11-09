package notificacion.comprobante;

import java.time.LocalDate;
import java.util.List;

import servicios.Servicio;
import reserva.Cliente;

public class Factura extends Comprobante {

    public Factura(String numero, LocalDate fecha, Cliente cliente, List<Servicio> servicios) {
        super(numero, fecha, cliente);
    }

    public double getTotal() {
        return getItems().stream().mapToDouble(Servicio::getPrecioFinal).sum();
    }
}
