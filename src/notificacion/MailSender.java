package notificacion;

public interface MailSender {

    void enviarMail(String direccionDestino, String titulo, String mensaje, Object adjunto);
}
