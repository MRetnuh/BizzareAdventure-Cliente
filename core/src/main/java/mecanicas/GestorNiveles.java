package mecanicas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import audios.Musica;
import enemigos.EnemigoBase;
import juego.Partida;
import jugadores.Jugador;
import mecanicas.GestorDerrota;
import niveles.NivelBase;
import pantallas.NivelSuperado;
public class GestorNiveles {

    private final Game JUEGO;
    private final NivelBase[] niveles;
    private NivelBase nivelActual;
    private int indiceNivelActual = 0;

    public GestorNiveles(Game juego, NivelBase[] niveles, NivelBase nivelActual) {
        this.JUEGO = juego;
        this.niveles = niveles;
        this.nivelActual = nivelActual;
    }
 
    public void inicializarNivel(Jugador[] jugadores, int jugador1, int jugador2,
                                 Stage stage, GestorDerrota gestorDerrota) {

    	this.nivelActual.restaurarEstadoCajas();
        this.nivelActual.crearEnemigos();

        if (jugadores[jugador1].getPersonajeElegido() != null) {
            jugadores[jugador1].getPersonajeElegido()
                    .setPosicion(nivelActual.getInicioX1(), nivelActual.getInicioY1());
            jugadores[jugador1].generarPersonajeAleatorio();
        }
        if (jugadores[jugador2].getPersonajeElegido() != null) {
            jugadores[jugador2].getPersonajeElegido()
                    .setPosicion(nivelActual.getInicioX2(), nivelActual.getInicioY2());
            jugadores[jugador2].generarPersonajeAleatorio();
        }

        stage.clear();

        if (jugadores[jugador1].getPersonajeElegido() != null)
            stage.addActor(jugadores[jugador1].getPersonajeElegido());

        if (jugadores[jugador2].getPersonajeElegido() != null)
            stage.addActor(jugadores[jugador2].getPersonajeElegido());

        for (EnemigoBase enemigo : nivelActual.getEnemigos()) {
            stage.addActor(enemigo);
        }

        gestorDerrota.resetear();
    }

    public void comprobarVictoriaYAvanzar(Jugador[] jugadores, Partida partida) {

        boolean victoria = nivelActual.comprobarVictoria(
            jugadores[0].getPersonajeElegido().getX(),
            jugadores[0].getPersonajeElegido().getY(),
            jugadores[1].getPersonajeElegido().getX(),
            jugadores[1].getPersonajeElegido().getY()
        );

        if (victoria) {
        	 indiceNivelActual++;
                NivelSuperado nivelSuperado = new NivelSuperado(
                        this.nivelActual.getNombreNivel(),
                        this.JUEGO,
                        niveles[this.indiceNivelActual].getNombreNivel(),
                        partida
                );
                JUEGO.setScreen(nivelSuperado);
            }
        }
    
    public void inicializarSiguienteNivel(Jugador[] jugadores, int jugador1, int jugador2,
                                          Stage stage, GestorDerrota gestorDerrota) {
    		this.nivelActual = this.niveles[this.indiceNivelActual];
            inicializarNivel(jugadores, jugador1, jugador2, stage, gestorDerrota);
    }
    
    public NivelBase getNivelActual() {
        return this.nivelActual;
    }
    
}
