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
import red.HiloCliente;

public class PantallaError implements Screen {

    private final Game game;
    private Stage stage;
    private Skin skin;
    private Label titulo;
    private float tiempoTranscurrido = 0;
    private HiloCliente hiloCliente;
    public PantallaError(Game game, HiloCliente hilo) {
        this.game = game;
        this.stage = new Stage();
        this.hiloCliente = hilo;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Texto principal
        this.titulo = new Label("¡Error! Se ha desconectado del servidor.", EstiloTexto.ponerEstiloLabel(60, Color.RED));
        this.titulo.setAlignment(Align.center);

        // Crear una tabla para organizar los elementos
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();
        tabla.add(this.titulo).padBottom(5);

        this.stage.addActor(tabla);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act(delta);
        this.stage.draw();

        tiempoTranscurrido += delta;

        // Pasados 5 segundos → cambiar pantalla
        if (tiempoTranscurrido >= 3f) {
            this.hiloCliente.finalizar();
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.stage.dispose();
        if (this.skin != null) this.skin.dispose();
    }
}
