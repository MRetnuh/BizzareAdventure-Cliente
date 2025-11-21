package pantallas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import estilos.EstiloTexto;
import juego.Partida;
import red.HiloCliente;

public class PantallaEspera implements Screen{
	  private final Game game;
	    private Stage stage;
	    private Skin skin;
	    private HiloCliente hiloCliente;
	    private Partida partida;

	    private Label titulo;
	    private Label puntosLabel;

	    private int contadorPuntos = 0;
	    private float acumuladorTiempo = 0;

	    private float anchoPuntosFijo; // <- ancho fijo para evitar retrocesos

	    public PantallaEspera(Game game, HiloCliente hiloCliente, Partida partida) {
	        this.game = game;
	        this.hiloCliente = hiloCliente;
	        this.partida = partida;
	        this.stage = new Stage();
	    }

	    @Override
	    public void show() {
	        Gdx.input.setInputProcessor(this.stage);
	        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

	        // Texto principal
	        this.titulo = new Label("Esperando al otro jugador", EstiloTexto.ponerEstiloLabel(60, Color.PURPLE));
	        this.titulo.setAlignment(Align.center);

	        // Label de puntos (se le asignará ancho fijo luego)
	        this.puntosLabel = new Label("", EstiloTexto.ponerEstiloLabel(60, Color.CYAN));
	        this.puntosLabel.setAlignment(Align.left);

	        // ======================================
	        // FIJAR ANCHO MÁXIMO PARA 3 PUNTOS (evita retrocesos)
	        // ======================================
	        this.puntosLabel.setText("..."); // simular tamaño max
	        this.anchoPuntosFijo = this.puntosLabel.getPrefWidth();
	        this.puntosLabel.setText(""); // volver a vacío
	        // ======================================

	        Table tabla = new Table();
	        tabla.setFillParent(true);
	        tabla.center();

	        tabla.add(this.titulo).padBottom(5);
	        tabla.add(this.puntosLabel).width(this.anchoPuntosFijo).padBottom(5).row();

	        this.stage.addActor(tabla);
	    }

	    private void actualizarAnimacion(float delta) {
	        this.acumuladorTiempo += delta;
	        if (this.acumuladorTiempo < 1f) return;
	        this.acumuladorTiempo = 0;

	        this.contadorPuntos++;
	        if (this.contadorPuntos > 3) this.contadorPuntos = 0;

	        StringBuilder puntos = new StringBuilder();
	        for (int i = 0; i < this.contadorPuntos; i++) {
	            puntos.append(".");
	        }

	        this.puntosLabel.setText(puntos.toString());
	    }

	    @Override
	    public void render(float delta) {
	        Gdx.gl.glClearColor(0, 0, 0, 1);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	        actualizarAnimacion(delta);

	        // Cambiar a la pantalla de partida cuando haya 2 jugadores
	        if (this.hiloCliente.getConexionEntreJugadores()) {
	            Gdx.app.postRunnable(() -> this.game.setScreen(this.partida));
	        }

	        this.stage.act(delta);
	        this.stage.draw();
	    }

	    @Override public void resize(int width, int height) {}
	    @Override public void pause() {}
	    @Override public void resume() {}
	    @Override public void hide() {}

	    @Override
	    public void dispose() {
	        this.stage.dispose();
	        if (this.skin != null) this.skin.dispose();
	    }
	}