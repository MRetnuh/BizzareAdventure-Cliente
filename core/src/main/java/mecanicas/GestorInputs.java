package mecanicas;

import com.badlogic.gdx.Game;

import audios.Musica;
import input.InputController;
import juego.Partida;
import pantallas.Opciones;
import red.HiloCliente;

public class GestorInputs {

    public static void procesarInputs(InputController inputController,HiloCliente hiloCliente,int idJugador, Game juego, 
	Partida partidaActual, Musica musicaPartida) {
        boolean derecha = inputController.getDerecha1();
        boolean izquierda = inputController.getIzquierda1();
        boolean saltar = inputController.getSaltar1();
        boolean atacar = inputController.getAtacar1();

        // Enviar al servidor los inputs del jugador
        if (hiloCliente != null) {
            String mensaje = String.format(
                "Mover:%d:%b:%b:%b:%b",
                idJugador,
                derecha,
                izquierda,
                saltar,
                atacar
            );
            hiloCliente.sendMessage(mensaje);
        }

        // Limpiar los ataques/acciones de botón único
        if (atacar) {inputController.setAtacarFalso1();}
        if (inputController.getOpciones1()) {juego.setScreen(new Opciones(juego, partidaActual, musicaPartida));
        inputController.setOpcionesFalso1();}
}
}