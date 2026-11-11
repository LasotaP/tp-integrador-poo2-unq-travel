package reserva;


import java.util.Objects;




public class Cliente {

    private final String nombre;
    private final String email;

    public Cliente(String nombre, String email) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.email = Objects.requireNonNull(email, "El email no puede ser nulo");
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
}
