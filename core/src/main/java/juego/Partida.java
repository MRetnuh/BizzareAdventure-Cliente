package juego;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import audios.Musica;
import input.InputController;
import jugadores.Jugador;
import mecanicas.*;
import niveles.Nivel1;
import niveles.Nivel2;
import niveles.NivelBase;
import personajes.Personaje;

public class Partida implements Screen {
    private GestorDerrota gestorDerrota = new GestorDerrota();
    private Musica musicaPartida;
    private Stage stage;
    private Stage stageHUD;
    private GestorHUD gestorHUD;
    private final int JUGADOR1 = 0, JUGADOR2 = 1;
    private final Jugador[] JUGADORES = new Jugador[2];
    private Skin skin;
    private OrthographicCamera camara;
    private SpriteBatch batch;
    private InputController inputController;
    private NivelBase[] niveles = {new Nivel1(), new Nivel2()};
    private NivelBase nivelActual;
    private final Game JUEGO;
    private boolean nivelIniciado  = false;
    private GestorNiveles gestorNiveles;
    
    public Partida(Game juego, Musica musica) {
        this.JUEGO = juego;
        this.musicaPartida = musica;
        this.camara = new OrthographicCamera();
        this.camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), this.batch);
        this.stageHUD = new Stage(new ScreenViewport(), this.batch);
        this.nivelActual = this.niveles[0];
        this.gestorNiveles = new GestorNiveles(juego, this.niveles, this.nivelActual);
        inicializarJugadores();
    }

    @Override
    public void show() {
        if (!this.nivelIniciado) {
            if (!this.JUGADORES[this.JUGADOR1].getPartidaEmpezada()) this.JUGADORES[this.JUGADOR1].generarPersonajeAleatorio();
            if (!this.JUGADORES[this.JUGADOR2].getPartidaEmpezada()) this.JUGADORES[this.JUGADOR2].generarPersonajeAleatorio();

            this.inputController = new InputController();
            this.nivelIniciado = true;

            this.gestorNiveles.inicializarNivel(this.JUGADORES, this.JUGADOR1, this.JUGADOR2, this.stage, this.gestorDerrota);
        }
        this.gestorHUD = new GestorHUD(this.stageHUD,
        	    this.JUGADORES[this.JUGADOR1],
        	    this.JUGADORES[this.JUGADOR2]);

        Gdx.input.setInputProcessor(this.inputController);
    }

    @Override
    public void render(float delta) {

        GestorInputs.procesarInputs(this.JUGADORES[this.JUGADOR1].getPersonajeElegido(),
        this.JUGADORES[this.JUGADOR2].getPersonajeElegido(), this.inputController,
        this.musicaPartida, this.nivelActual, delta, this.JUEGO, this);

        actualizarPersonaje(this.JUGADORES[this.JUGADOR1], this.JUGADORES[this.JUGADOR1].getPersonajeElegido(), delta, true);

        actualizarPersonaje(this.JUGADORES[this.JUGADOR2], this.JUGADORES[this.JUGADOR2].getPersonajeElegido(), delta, false);

        GestorCamara.actualizar(this.camara, this.JUGADORES[this.JUGADOR1].getPersonajeElegido(),
        this.JUGADORES[this.JUGADOR2].getPersonajeElegido(), this.nivelActual.getAnchoMapa(), this.nivelActual.getAlturaMapa());

        this.gestorNiveles.comprobarVictoriaYAvanzar(JUGADORES, this);
        this.nivelActual = this.gestorNiveles.getNivelActual();
        this.gestorHUD.actualizar();

        this.nivelActual.getMapRenderer().setView(this.camara);
        this.nivelActual.getMapRenderer().render();

        OrthographicCamera stageCam = (OrthographicCamera) this.stage.getCamera();
        stageCam.position.set(this.camara.position.x, this.camara.position.y, this.camara.position.z);
        stageCam.zoom = this.camara.zoom;
        stageCam.update();

        this.batch.setProjectionMatrix(this.camara.combined);

        GestorEnemigos.actualizar(delta, this.nivelActual, this.JUGADORES, this.stage, this.musicaPartida);

        this.stage.act(delta);
        this.stage.draw();
        this.stageHUD.act(delta);
        this.stageHUD.draw();
    }
    
    public void inicializarSiguienteNivel() {
        this.gestorNiveles.inicializarSiguienteNivel(this.JUGADORES, this.JUGADOR1, this.JUGADOR2, this.stage, this.gestorDerrota);
        if (this.inputController != null) {
            this.inputController.resetearInputs(); 
        }
        Gdx.input.setInputProcessor(null);
    }

    private void actualizarPersonaje(Jugador jugador, Personaje personaje, float delta, boolean esJugador1) {
        this.gestorDerrota.manejarMuerteJugador(personaje, esJugador1, this.musicaPartida, this.stageHUD);
        if (this.gestorDerrota.partidaTerminada()) return;

        GestorCombate.procesarCombate(personaje, this.nivelActual, this.musicaPartida, delta);

        GestorGravedad.aplicarGravedad(personaje, delta, this.nivelActual);

        GestorMovimiento.aplicarMovimiento(personaje, delta, this.nivelActual, this.JUGADORES, this.JUGADOR1, this.JUGADOR2,
	    esJugador1);

        GestorInteracciones.procesarGolpeCaja(personaje, jugador, esJugador1,
        this.nivelActual, this.stage, this.gestorHUD, this.JUGADORES);
    }

    @Override
    public void dispose() {
        for (NivelBase nivel : this.niveles) nivel.dispose();
        if (this.gestorHUD != null) this.gestorHUD.dispose();
        this.batch.dispose();
        this.stage.dispose();
        if (this.skin != null) this.skin.dispose();
    }

    private void inicializarJugadores() {
    	for (int i = 0; i < this.JUGADORES.length; i++) {
            this.JUGADORES[i] = new Jugador();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}