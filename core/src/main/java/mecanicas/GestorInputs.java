package mecanicas;

import com.badlogic.gdx.Game;
import audios.Musica;
import input.InputController;
import juego.Partida;
import niveles.NivelBase;
import pantallas.Opciones;
import personajes.Personaje;
import red.HiloCliente; // o el package donde esté tu clase de red

public class GestorInputs {

    public static void procesarInputs(
        Personaje personaje1,
        InputController inputController,
        Musica musicaPartida, NivelBase nivelActual,
        float delta, Game juego, Partida partidaActual,
        HiloCliente hiloCliente, int idJugador
    ) {

        // ----- PROCESO DE INPUT LOCAL -----
        if (personaje1.getVida() > 0) {
            personaje1.setMoviendoDerecha(inputController.getDerecha1());
            personaje1.setMoviendoIzquierda(inputController.getIzquierda1());
            personaje1.setEstaSaltando(inputController.getSaltar1());

            if (inputController.getAtacar1()) {
                personaje1.iniciarAtaque(musicaPartida.getVolumen(), delta, nivelActual);
                inputController.setAtacarFalso1();
            }
            if (inputController.getOpciones1()) {
                juego.setScreen(new Opciones(juego, partidaActual, musicaPartida));
                inputController.setOpcionesFalso1();
            }
        }

        if (hiloCliente != null) {
            String mensajeInput = String.format(
                "Mover:%d:%b:%b:%b:%b",
                idJugador,
                inputController.getDerecha1(),
                inputController.getIzquierda1(),
                inputController.getSaltar1(),
                inputController.getAtacar1()
            );
            hiloCliente.sendMessage(mensajeInput);
        }
    }
}
