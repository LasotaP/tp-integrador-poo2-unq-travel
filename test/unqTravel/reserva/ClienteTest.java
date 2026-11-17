package unqTravel.reserva;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import unqTravel.SetUpTest;

class ClienteTest extends SetUpTest {

    @Test
    void unClienteConoceSuNombreYSuEmail() {
        assertEquals("Juan Perez", cliente.getNombre());
        assertEquals("juan.perez@mail.com", cliente.getEmail());
    }

    @Test
    void noSePuedeCrearUnClienteSinNombre() {
        assertThrows(NullPointerException.class, () -> new Cliente(null, "juan.perez@mail.com"));
    }

    @Test
    void noSePuedeCrearUnClienteSinEmail() {
        assertThrows(NullPointerException.class, () -> new Cliente("Juan Perez", null));
    }
}
