package interfaces;

public interface GameController {
    void conectar(int numPlayer);
    void asignarPersonajes(int personajeIndice);
    void empezar();
    void perder();
    void actualizarEstado(String[] datos);
    void volverAlMenu();
}
