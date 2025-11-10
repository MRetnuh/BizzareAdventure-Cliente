package juego;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import audios.Musica;
import enemigos.EnemigoBase;
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
    	if (this.juegoEmpezado) {
        if (!this.nivelIniciado) {
            this.nivelIniciado = true;

             }
    	}
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

    	GestorInputs.procesarInputs(this.inputController,this.hiloCliente,jugadorLocalIndex);
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
	    this.idJugadorLocal = numPlayer;
	}

	@Override
	public void empezar(int p1Id, int p2Id) {
	    // Ejecutar en el hilo principal de render de LibGDX
	    Gdx.app.postRunnable(() -> {
	        this.JUGADORES[JUGADOR1].asignarPersonaje(p1Id);
	        this.JUGADORES[JUGADOR2].asignarPersonaje(p2Id);

	        this.juegoEmpezado = true;

	        if (!this.nivelIniciado) {
	            this.inputController = new InputController();
	            this.nivelIniciado = true;
	            this.gestorNiveles.inicializarNivel(this.JUGADORES, this.JUGADOR1, this.JUGADOR2, this.stage, this.gestorDerrota);
	            this.gestorHUD = new GestorHUD(this.stageHUD,
	            	    this.JUGADORES[this.JUGADOR1],
	            	    this.JUGADORES[this.JUGADOR2]);
	            Gdx.input.setInputProcessor(this.inputController);
	        }
	    });
	}

		
	

	@Override
	public void perder() {
		
	}

	@Override
	public void volverAlMenu() {
		
	}

	@Override
	public void actualizarEstado(String[] datos) {
		if (this.juegoEmpezado) {
	    // Formato: [0:UpdateState, 1:1, 2:posX1, 3:posY1, 4:vida1, 5:ESTADO1, 6:2, 7:posX2, 8:posY2, 9:vida2, 10:ESTADO2]

	    // --- JUGADOR 1 (Índice 0) ---
	    Personaje p1 = this.JUGADORES[this.JUGADOR1].getPersonajeElegido();
	    float p1X = Float.parseFloat(datos[2]); 
	    float p1Y = Float.parseFloat(datos[3]);
	    
	    // --- JUGADOR 2 (Índice 1) ---
	    Personaje p2 = this.JUGADORES[this.JUGADOR2].getPersonajeElegido();
	    float p2X = Float.parseFloat(datos[7]); 
	    float p2Y = Float.parseFloat(datos[8]);

	    if (this.idJugadorLocal == 1) { 
	        // 1. Soy J1: Actualizo al J2 (oponente)
	        p2.setX(p2X);
	        p2.setY(p2Y);
	        p2.setVida(Integer.parseInt(datos[9]));
	        
	        // 2. Soy J1: Corrijo/Sincronizo al J1 (local)
	        p1.setX(p1X);
	        p1.setY(p1Y);
	        p1.setVida(Integer.parseInt(datos[4]));
	    }
	    else if (this.idJugadorLocal == 2){
	        // 1. Soy J2: Actualizo al J1 (oponente)
	        p1.setX(p1X);
	        p1.setY(p1Y);
	        // 2. Soy J2: Corrijo/Sincronizo al J2 (local)
	        p2.setX(p2X);
	        p2.setY(p2Y);
	        p2.setVida(Integer.parseInt(datos[9]));
	    }
		}
	    // Nota: Deberías incluir también la actualización de vida y estado aquí.
	}

	public void animar(int idJugador, boolean izquierda, boolean derecha, boolean saltar) {
	    if (!this.juegoEmpezado) return;

	    int index = idJugador - 1;
	    if (index < 0 || index >= JUGADORES.length) return;

	    Personaje p = this.JUGADORES[index].getPersonajeElegido();

	    // Setear flags que usa tu sistema de animación / movimiento
	    p.setMoviendoDerecha(derecha);
	    p.setMoviendoIzquierda(izquierda);
	    p.setEstaSaltando(saltar);
	    boolean estaMoviendo = derecha || izquierda || saltar;
	    p.setEstaMoviendose(estaMoviendo);
	    if(derecha) p.setMirandoDerecha(false);
	    else if(izquierda) p.setMirandoDerecha(true);
	    
	}

	public void actualizarEnemigos(String[] datos) {
		if (this.nivelActual == null) return;

		for (int i = 1; i < datos.length; i++) {
			String[] info = datos[i].split(",");
			if (info.length < 4) continue;

			String id = info[0];
			float x = Float.parseFloat(info[1]);
			float y = Float.parseFloat(info[2]);
			int vida = Integer.parseInt(info[3]);

			for (EnemigoBase enemigo : this.nivelActual.getEnemigos()) {
				if (enemigo.getNombre().equals(id)) {

					// Detectar si se movió (para animar)
					boolean seMovio = (enemigo.getX() != x);

					// Actualizar dirección según hacia dónde se movió
					if (seMovio) {
						enemigo.setEstaMoviendose(true);
						enemigo.setMirandoDerecha(x > enemigo.getX());
					} else {
						enemigo.setEstaMoviendose(false);
					}

					enemigo.setX(x);
					enemigo.setY(y);
					enemigo.setVida(vida);

					break;
				}
			}
		}
	}



}