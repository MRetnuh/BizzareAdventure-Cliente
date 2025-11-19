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

public class PantallaError implements Screen{
		private final Game game;
	    private Stage stage;
	    private Skin skin;
	    private HiloCliente hiloCliente;
	    private Partida partida;

	    private Label titulo;
	    private Label puntosLabel;

	    public PantallaError(Game game, HiloCliente hiloCliente, Partida partida) {
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
	        titulo = new Label("Error inesperado", EstiloTexto.ponerEstiloLabel(60, Color.PURPLE));
	        titulo.setAlignment(Align.center);

	      
	        // ======================================

	        Table tabla = new Table();
	        tabla.setFillParent(true);
	        tabla.center();

	        tabla.add(titulo).padBottom(5);

	        this.stage.addActor(tabla);
	    }


	    @Override
	    public void render(float delta) {
	        Gdx.gl.glClearColor(0, 0, 0, 1);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


	        stage.act(delta);
	        stage.draw();
	    }

	    @Override public void resize(int width, int height) {}
	    @Override public void pause() {}
	    @Override public void resume() {}
	    @Override public void hide() {}

	    @Override
	    public void dispose() {
	        stage.dispose();
	        if (skin != null) skin.dispose();
	    }
	}