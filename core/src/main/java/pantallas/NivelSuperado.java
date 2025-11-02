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
import com.badlogic.gdx.utils.Timer;

import audios.Musica;
import estilos.EstiloTexto;
import juego.Partida;

public class NivelSuperado implements Screen {

    private String nivelSuperado;	
    private String siguienteNivel;
    private final Game JUEGO;
    private final Partida partida; // 🔹 referencia a la partida
    private Stage stage;
    private Skin skin;
    private Musica musicaOpciones;	

    public NivelSuperado(String nivelSuperado, Game juego, String siguienteNivel, Partida partida) {
        this.nivelSuperado = nivelSuperado;
        this.JUEGO = juego;
        this.siguienteNivel = siguienteNivel;
        this.partida = partida;
        this.stage = new Stage();
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label titulo = new Label(this.nivelSuperado, EstiloTexto.ponerEstiloLabel(60, Color.WHITE));
        Label subTitulo = new Label("SUPERADO", EstiloTexto.ponerEstiloLabel(80, Color.PURPLE));
        Label subTitulo2 = new Label("Proximo nivel: " + this.siguienteNivel, EstiloTexto.ponerEstiloLabel(80, Color.CYAN));
        titulo.setAlignment(Align.center);
        subTitulo.setAlignment(Align.center);
        subTitulo2.setAlignment(Align.center);
        
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();
        tabla.defaults().center();
        tabla.add(titulo).padBottom(5).row();
        tabla.add(subTitulo).padBottom(40).row();
        tabla.add(subTitulo2);
        
        this.stage.addActor(tabla);

        // 🔹 Esperar 5 segundos y luego pasar al siguiente nivel
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                pasarASiguienteNivel();
            }
        }, 5);
    }
    
    // 🔹 Método que vuelve a la partida con el siguiente nivel ya cargado
    private void pasarASiguienteNivel() {
        this.partida.inicializarSiguienteNivel();
        this.JUEGO.setScreen(this.partida);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
