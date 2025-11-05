package juego;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import audios.Musica;
import input.InputController;
import interfaces.GameController;
import jugadores.Jugador;
import mecanicas.*;
import niveles.Nivel1;
import niveles.Nivel2;
import niveles.NivelBase;
import personajes.Personaje;
import red.HiloCliente;

public class Partida implements Screen, GameController {
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
    private HiloCliente hiloCliente;
    private boolean finJuego = false;
    private boolean juegoEmpezado = false; 
    private int idJugadorLocal = 0;
    
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
        this.hiloCliente = new HiloCliente(this);
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
        this.hiloCliente.start();
        this.hiloCliente.sendMessage("Conectado");
    }

    @Override
    public void render(float delta) {
    	if (!this.juegoEmpezado) {
            // Lógica para dibujar una pantalla de espera o un mensaje en el HUD
            Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // Dibuja un mensaje: "Esperando al otro jugador..." usando tu Stage/SpriteBatch
            this.stageHUD.act(delta);
            this.stageHUD.draw();
            return; // Detiene el resto del renderizado y el envío de inputs.
        }
    	int jugadorLocalIndex = this.idJugadorLocal; // Asumiendo que el 1 es el local por ahora, esto debe ser dinámico

    	String mensajeInput = "Mover:" + jugadorLocalIndex + ":" + this.inputController.generarMensajeInput();
        this.hiloCliente.sendMessage(mensajeInput);
    	this.hiloCliente.sendMessage(mensajeInput);
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

    @Override
    public void dispose() {
        for (NivelBase nivel : this.niveles) nivel.dispose();
        if (this.gestorHUD != null) this.gestorHUD.dispose();
        this.batch.dispose();
        this.hiloCliente.terminate();
        this.stage.dispose();
        if (this.skin != null) this.skin.dispose();
    }

    private void inicializarJugadores() {
    	for (int i = 0; i < this.JUGADORES.length; i++) {
            this.JUGADORES[i] = new Jugador(i + 1);
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

	@Override
	public void conectar(int numPlayer) {
	    this.idJugadorLocal = numPlayer; // <-- Guardamos el 1 o el 2 asignado por el servidor
	}

	@Override
	public void empezar() {
		this.juegoEmpezado = true;
		
	}

	@Override
	public void perder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void volverAlMenu() {
		// TODO Auto-generated method stub
		
	}

	// En juego.Partida.java (Cliente) -> actualizarEstado(String[] datos)

	@Override
	public void actualizarEstado(String[] datos) {
	    // Formato: [0:UpdateState, 1:1, 2:posX1, 3:posY1, 4:vida1, 5:ESTADO1, 6:2, 7:posX2, 8:posY2, 9:vida2, 10:ESTADO2]

	    if (this.idJugadorLocal == 1) { 
	        // Eres el Jugador 1. Actualiza al Jugador 2 (índice JUGADOR2=1 en el array local)
	        Personaje p2 = this.JUGADORES[this.JUGADOR2].getPersonajeElegido();
	        // Los datos del Jugador 2 están en los índices 7 (X) y 8 (Y) del array 'datos'
	        p2.setX(Float.parseFloat(datos[7])); 
	        p2.setY(Float.parseFloat(datos[8])); 
	        // p2.setVida(Integer.parseInt(datos[9]));
	    }
	    else if (this.idJugadorLocal == 2){
	        // Eres el Jugador 2. Actualiza al Jugador 1 (índice JUGADOR1=0 en el array local)
	        Personaje p1 = this.JUGADORES[this.JUGADOR1].getPersonajeElegido();
	        // Los datos del Jugador 1 están en los índices 2 (X) y 3 (Y) del array 'datos'
	        p1.setX(Float.parseFloat(datos[2])); 
	        p1.setY(Float.parseFloat(datos[3])); 
	        // p1.setVida(Integer.parseInt(datos[4]));
	    }
	    
	    // Nota: Ya no es necesario el condicional basado en this.JUGADORES[this.JUGADOR1].getNumPlayer()
	}

}