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
    
    private void aplicarPrediccionLocal(float delta) {
        
        int arrayIndex = this.idJugadorLocal - 1; // ID 1 -> índice 0 (JUGADOR1), ID 2 -> índice 1 (JUGADOR2)
        if (arrayIndex < 0 || arrayIndex >= JUGADORES.length) return;
        
        Personaje personajeLocal = this.JUGADORES[arrayIndex].getPersonajeElegido();
        
        if (personajeLocal.getVida() <= 0) return;
        
        // --- Lógica de Input y Ataque (Adaptada de GestorInputs) ---
        // 1. Aplicar Inputs
        personajeLocal.setMoviendoDerecha(this.inputController.getDerecha1());
        personajeLocal.setMoviendoIzquierda(this.inputController.getIzquierda1());
        personajeLocal.setEstaSaltando(this.inputController.getSaltar1());
        
        // 2. Aplicar Ataque (el cliente lo simula, el servidor lo resuelve)
        if (this.inputController.getAtacar1()) {
            personajeLocal.iniciarAtaque(this.musicaPartida.getVolumen(), delta, this.nivelActual);
            this.inputController.setAtacarFalso1(); // Esto DEBE seguir aquí para ataques simples.
        }
        
        // --- Lógica de Movimiento (Adaptada del flujo original) ---
        
        // Aplicar Gravedad
        GestorGravedad.aplicarGravedad(personajeLocal, delta, this.nivelActual);
        
        // Aplicar Movimiento (Usamos el GestorMovimiento original pero sin la complejidad de red)
        // NOTA: Para este caso, el parámetro 'esJugador1' debe ser el que corresponda al índice local
        boolean esJugador1Local = (arrayIndex == this.JUGADOR1);
        
        GestorMovimiento.aplicarMovimiento(personajeLocal, delta, this.nivelActual, 
                                           this.JUGADORES, this.JUGADOR1, this.JUGADOR2,
                                           esJugador1Local, // Indica si el personaje a mover es el P1
                                           this.inputController.getDerecha1(), // Inputs
                                           this.inputController.getIzquierda1(),
                                           this.inputController.getSaltar1());

        // NOTA: Si GestorMovimiento sigue teniendo la antigua firma, usa:
        // GestorMovimiento.aplicarMovimiento(personajeLocal, delta, this.nivelActual, 
        //                                    this.JUGADORES, this.JUGADOR1, this.JUGADOR2,
        //                                    esJugador1Local);
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

	// En juego.Partida.java (Cliente)

	@Override
	public void actualizarEstado(String[] datos) {
	    // Formato: [0:UpdateState, 1:1, 2:posX1, 3:posY1, 4:vida1, 5:ESTADO1, 6:2, 7:posX2, 8:posY2, 9:vida2, 10:ESTADO2]

	    // Usamos el ID asignado por el servidor (1 o 2) para saber quiénes somos.
	    if (this.idJugadorLocal == 1) { 
	        // Soy el Jugador 1. Actualizo al Jugador 2 (índice 1 en el array local).
	        Personaje p2 = this.JUGADORES[this.JUGADOR2].getPersonajeElegido();
	        
	        // Datos del Jugador 2 (índices 7 y 8)
	        p2.setX(Float.parseFloat(datos[7].replace(',', '.'))); 
	        p2.setY(Float.parseFloat(datos[8].replace(',', '.'))); 
	    }
	    else if (this.idJugadorLocal == 2){
	        // Soy el Jugador 2. Actualizo al Jugador 1 (índice 0 en el array local).
	        Personaje p1 = this.JUGADORES[this.JUGADOR1].getPersonajeElegido();
	        
	        // Datos del Jugador 1 (índices 2 y 3)
	        p1.setX(Float.parseFloat(datos[2].replace(',', '.'))); 
	        p1.setY(Float.parseFloat(datos[3].replace(',', '.'))); 
	    }
	    // NOTA: Incluí el .replace(',', '.') para evitar el error de formato decimal.
	}
	    
	    // Nota: Ya no es necesario el condicional basado en this.JUGADORES[this.JUGADOR1].getNumPlayer(

}