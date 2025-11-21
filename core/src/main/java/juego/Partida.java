package juego;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
import pantallas.Menu;
import pantallas.NivelSuperado;
import pantallas.PantallaEspera;
import pantallas.Victoria;
import personajes.Personaje;
import proyectiles.Proyectil;
import red.HiloCliente;

public class Partida implements Screen, GameController {
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
        this.hiloCliente = new HiloCliente(this);
        inicializarJugadores();
    }

    @Override
    public void show() {
    	 if (!this.juegoEmpezado) {   
    		 	this.JUEGO.setScreen(new PantallaEspera(this.JUEGO, this.hiloCliente, this));
    	        this.hiloCliente.start();
    	        this.hiloCliente.sendMessage("Conectado");
    	     
    	    }

    	    // IMPORTANTE: volver a darle control de input a Partida
    	    if (this.inputController != null) {
    	        Gdx.input.setInputProcessor(this.inputController);
    	    }
    }


    @Override
    public void render(float delta) {
    	if (!this.juegoEmpezado) {
            // Lógica para dibujar una pantalla de espera o un mensaje en el HUD
           // Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
            //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // Dibuja un mensaje: "Esperando al otro jugador..." usando tu Stage/SpriteBatch
            this.stageHUD.act(delta);
            this.stageHUD.draw();
            return; // Detiene el resto del renderizado y el envío de inputs.
        }
    	int jugadorLocalIndex = this.idJugadorLocal; // Asumiendo que el 1 es el local por ahora, esto debe ser dinámico

    	GestorInputs.procesarInputs(this.inputController,this.hiloCliente,jugadorLocalIndex, this.JUEGO, this, this.musicaPartida);
        GestorCamara.actualizar(this.camara, this.JUGADORES[this.JUGADOR1].getPersonajeElegido(),
        this.JUGADORES[this.JUGADOR2].getPersonajeElegido(), this.nivelActual.getAnchoMapa(), this.nivelActual.getAlturaMapa());
        if(this.JUGADORES[this.JUGADOR1].getPersonajeElegido().getEstaAtacando())this.JUGADORES[this.JUGADOR1].getPersonajeElegido().atacar(delta);
        if(this.JUGADORES[this.JUGADOR2].getPersonajeElegido().getEstaAtacando())this.JUGADORES[this.JUGADOR2].getPersonajeElegido().atacar(delta);
       
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
        this.hiloCliente.finalizar();
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
	        this.JUGADORES[this.JUGADOR1].asignarPersonaje(p1Id);
	        this.JUGADORES[this.JUGADOR2].asignarPersonaje(p2Id);

	        this.juegoEmpezado = true;

	        if (!this.nivelIniciado) {
	        	this.gestorNiveles = new GestorNiveles(this.JUEGO, this.niveles, this.nivelActual);
	            this.nivelIniciado = true;
	            this.gestorNiveles.inicializarNivel(this.JUGADORES, this.JUGADOR1, this.JUGADOR2, this.stage);
	            this.gestorHUD = new GestorHUD(this.stageHUD,
	            	    this.JUGADORES[this.JUGADOR1],
	            	    this.JUGADORES[this.JUGADOR2]);
	            if (this.inputController == null) {
	                this.inputController = new InputController();
	            }
	            Gdx.input.setInputProcessor(this.inputController);
	        }
	    });
	}

		
	

	@Override
	public void perder() {
		 Gdx.app.postRunnable(() -> {
		        this.musicaPartida.cambiarMusica("Derrota");
		        this.JUGADORES[0].getPersonajeElegido().morir(this.stageHUD, this.hiloCliente);
		        this.JUGADORES[1].getPersonajeElegido().morir(this.stageHUD, this.hiloCliente);
		    });
	}

	@Override
	public void volverAlMenu() {
        this.hiloCliente.finalizar();
		 Gdx.app.postRunnable(() -> {
			 this.musicaPartida.cambiarMusica("Menu");
			 this.JUEGO.setScreen(new Menu(this.JUEGO));
		 });
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
			p1.setVida(Integer.parseInt(datos[4]));
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
	    if (index < 0 || index >= this.JUGADORES.length) return;

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

	@Override
	public void actualizarMovimientoEnemigos(String[] datos) {

		for (int i = 1; i < datos.length; i++) {

			String[] info = datos[i].split(",");
			if (info.length < 6) continue;

			String id = info[0];
			float x = Float.parseFloat(info[1]);
			float y = Float.parseFloat(info[2]);
			int vida = Integer.parseInt(info[3]);
			boolean mirandoDer = Boolean.parseBoolean(info[4]);
			boolean moviendose = Boolean.parseBoolean(info[5]);

			for (EnemigoBase enemigo : nivelActual.getEnemigos()) {
				if (enemigo.getNombre().equals(id)) {

					enemigo.setX(x);
					enemigo.setY(y);
					enemigo.setVida(vida);
					enemigo.setMirandoDerecha(mirandoDer);
					enemigo.setEstaMoviendose(moviendose);

					break;
				}
			}
		}
	}


	@Override
	public void actualizarBalasEnemigos(String[] datos) {

		String[] info = datos[1].split(",");
		String idEnemigo = info[0];
		float x = Float.parseFloat(info[1]);
		float y = Float.parseFloat(info[2]);
		String ruta = info[3];

		for (EnemigoBase enemigo : this.nivelActual.getEnemigos()) {
			if (enemigo.getNombre().equals(idEnemigo) && enemigo.getVida() > 0) {

				// Evitar duplicados
				for (Proyectil existente : enemigo.getBalas()) {
					if (Math.abs(existente.getX() - x) < 5f &&
							Math.abs(existente.getY() - y) < 5f) {
						return; // Bala ya existe
					}
				}
				Gdx.app.postRunnable(() -> {
					Proyectil nueva = new Proyectil(x, y, enemigo.getMirandoDerecha(), ruta, false);
					enemigo.getBalas().add(nueva);
					this.stage.addActor(nueva);
					return;
				});
			}
		}
	}


	@Override
	public void asignarNivel(int indice) {
		this.nivelActual = this.niveles[indice];
		
	}

	@Override
	public void eliminarCaja(String[] datos) {
		  TiledMapTileLayer layer = (TiledMapTileLayer) this.nivelActual.getMapa().getLayers().get("cajasInteractivas");
		    if (layer == null) return;
		    String[] info = datos[1].split(",");
		    int x = Integer.parseInt(info[0]);
		    int y = Integer.parseInt(info[1]);
		    layer.getCell(x, y).setTile(this.nivelActual.getMapa().getTileSets().getTile(0));
		}

	@Override
	public void cambiarPersonaje(int jugador, int idPersonaje) {
		Gdx.app.postRunnable(() -> {
		 this.stage.getActors().removeValue(this.JUGADORES[jugador - 1].getPersonajeElegido(), true);
		 this.JUGADORES[jugador - 1].asignarPersonaje(idPersonaje);
		 this.stage.addActor(this.JUGADORES[jugador - 1].getPersonajeElegido());
		});
		
	}

	@Override
	public void avanzarNivel(String[] datos) {
	    Gdx.app.postRunnable(() -> {
	        String nivelAnterior = datos[1];
	        String siguiente = datos[2];
	        this.JUEGO.setScreen(new NivelSuperado(nivelAnterior, this.JUEGO, siguiente, this));
	   
	    	this.nivelActual = this.niveles[Integer.parseInt(datos[3])];
	    	this.gestorNiveles = new GestorNiveles(this.JUEGO, this.niveles, this.nivelActual);
	    	this.gestorNiveles.inicializarNivel(this.JUGADORES, this.JUGADOR1, this.JUGADOR2, this.stage); });
	}

	@Override
	public void cambiarPersonajesPorNivel(String[] datos) {
		  Gdx.app.postRunnable(() -> {
		     for(int i = 0; i < this.JUGADORES.length; i++) {
		    	 this.stage.getActors().removeValue(this.JUGADORES[i].getPersonajeElegido(), true);
		     }
		     for(int j = 0; j < this.JUGADORES.length; j++) {
		    	 this.JUGADORES[j].asignarPersonaje(Integer.parseInt(datos[j + 1]));
				 this.stage.addActor(this.JUGADORES[j].getPersonajeElegido());
		     }
		
	});
	}

	@Override
	public void animarPersonajeAtaque(String[] datos) {
		int idJugador = Integer.parseInt(datos[1]) - 1;
		float delta = Float.parseFloat(datos[2]);
		Gdx.app.postRunnable(() -> {
		this.JUGADORES[idJugador].getPersonajeElegido().iniciarAtaque(this.musicaPartida.getVolumen(), delta, this.nivelActual);
		this.JUGADORES[idJugador].getPersonajeElegido().setEstaAtacando(true);
		
		});
	}

	@Override
	public void eliminarBala(String[] datos) {

		String idEnemigo = datos[1];
		Gdx.app.postRunnable(() -> {
		for (EnemigoBase enemigo : this.nivelActual.getEnemigos()) {
			if (enemigo.getNombre().equals(idEnemigo)) {

				Iterator<Proyectil> it = enemigo.getBalas().iterator();
				while (it.hasNext()) {
					Proyectil b = it.next();
					it.remove();
					b.remove(); // remover del stage
				}

				return;
			}
		}});
	}

	@Override
	public void ganarPartida() {
		  Gdx.app.postRunnable(() -> {
			  this.JUEGO.setScreen(new Victoria(this.JUEGO, this.hiloCliente));
		  });
		
	}

    @Override
    public void actualizarPosicionBalas(String[] datos) {
        String[] info = datos[1].split(",");
        String idEnemigo = info[0];
        float x = Float.parseFloat(info[1]);
        float y = Float.parseFloat(info[2]);

        for (EnemigoBase e : this.nivelActual.getEnemigos()) {
            if (e.getNombre().equals(idEnemigo)) {
                for (Proyectil b : e.getBalas()) {
                    b.setPosition(x, y);
                }
            }
        }
    }


}