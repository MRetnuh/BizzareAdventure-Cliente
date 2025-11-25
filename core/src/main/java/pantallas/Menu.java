package pantallas;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import audios.Musica;
import estilos.EstiloTexto;
import estilos.ListenerBotonTexto;
import io.github.some.Principal;
import juego.Partida;

public class Menu implements Screen {
	private Musica musicaMenu;
	private final Game JUEGO;
    private Stage stage;
    private Skin skin;
    private Texture fondoTextura;
    private Image fondoImagen;

    public Menu(Game juego) {
        this.stage = new Stage();
        this.JUEGO = juego;
        this.musicaMenu = new Musica("Menu");
        this.musicaMenu.show(this.stage);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(this.stage);
    
        this.fondoTextura = new Texture(Gdx.files.internal("imagenes/fondos/Portada.jpg"));
        this.fondoImagen = new Image(this.fondoTextura);
        this.fondoImagen.setFillParent(true);
        this.stage.addActor(this.fondoImagen);

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
   
        TextButton jugarBtn = new TextButton("Jugar", EstiloTexto.ponerEstiloBoton(this.skin, 70, Color.PURPLE));
        TextButton opcionesBtn = new TextButton("Opciones", EstiloTexto.ponerEstiloBoton(this.skin, 70, Color.PURPLE));
        TextButton salirBtn = new TextButton("Salir", EstiloTexto.ponerEstiloBoton(this.skin, 70, Color.PURPLE));

        jugarBtn.addListener(new ListenerBotonTexto("Jugar", new Runnable() {
            @Override
            public void run() {
                musicaMenu.cambiarMusica("PrimerNivel");
                JUEGO.setScreen(new Partida(JUEGO, musicaMenu));
            }
        }));

        // 2. Botón Opciones
        opcionesBtn.addListener(new ListenerBotonTexto("Opciones", new Runnable() {
            @Override
            public void run() {
                JUEGO.setScreen(new Opciones(JUEGO, Menu.this, musicaMenu, false));
            }
        }));


        // 3. Botón Salir
        salirBtn.addListener(new ListenerBotonTexto("Salir", new Runnable() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        }));

      
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();
        tabla.padTop(250);
        tabla.defaults().center();
        tabla.add(jugarBtn).padBottom(25).row();
        tabla.add(opcionesBtn).padBottom(25).row();
        tabla.add(salirBtn);

        this.stage.addActor(tabla);
    }

    @Override
    public void render(float delta) {          
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
        this.skin.dispose();
    }
}
