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

public class Victoria implements Screen {

    private final Game game;
    private Stage stage;
    private Skin skin;
    private HiloCliente hiloCliente;
    private Label titulo;
    // ⬇⬇⬇ NUEVO: temporizador para esperar 5 segundos
    private float tiempoTranscurrido = 0;
    private boolean cambioRealizado = false;

    public Victoria(Game game, HiloCliente hiloCliente) {
        this.game = game;
        this.hiloCliente = hiloCliente;
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        titulo = new Label("Ganaste el juego", EstiloTexto.ponerEstiloLabel(60, Color.PURPLE));
        titulo.setAlignment(Align.center);

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

        // ⬇⬇⬇ NUEVO: sumar tiempo
        tiempoTranscurrido += delta;

        // Pasados 5 segundos → cambiar pantalla
        if (!cambioRealizado && tiempoTranscurrido >= 5f) {

            cambioRealizado = true; 
            
            
        }
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
