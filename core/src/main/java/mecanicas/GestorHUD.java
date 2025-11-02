package mecanicas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import estilos.EstiloTexto;
import jugadores.Jugador;

public class GestorHUD {

    private final Stage stageHUD;
    private final Skin skin;

    private Label nombrePersonaje1Label;
    private Label vidaPersonaje1Label;
    private Label nombrePersonaje2Label;
    private Label vidaPersonaje2Label;

    private final Jugador jugador1;
    private final Jugador jugador2;

    public GestorHUD(Stage stageHUD, Jugador jugador1, Jugador jugador2) {
        this.stageHUD = stageHUD;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        inicializarHUD();
    }
    
    private void inicializarHUD() {
        this.nombrePersonaje1Label = new Label(
            "Nombre: " + jugador1.getPersonajeElegido().getNombre(),
            EstiloTexto.ponerEstiloLabel(40, Color.RED)
        );
        this.vidaPersonaje1Label = new Label(
            "Vida: " + jugador1.getPersonajeElegido().getVida(),
            EstiloTexto.ponerEstiloLabel(40, Color.RED)
        );

        this.nombrePersonaje2Label = new Label(
            "Nombre: " + jugador2.getPersonajeElegido().getNombre(),
            EstiloTexto.ponerEstiloLabel(40, Color.BLUE)
        );
        this.vidaPersonaje2Label = new Label(
            "Vida: " + jugador2.getPersonajeElegido().getVida(),
            EstiloTexto.ponerEstiloLabel(40, Color.BLUE)
        );

        Table table1 = new Table();
        table1.left().top();
        table1.add(nombrePersonaje1Label).size(350, 50).padBottom(5).row();
        table1.add(vidaPersonaje1Label).size(350, 50);

        Table table2 = new Table();
        table2.right().top();
        table2.add(nombrePersonaje2Label).size(350, 50).padBottom(5).row();
        table2.add(vidaPersonaje2Label).size(350, 50);

        // Contenedores visuales
        Container<Table> cont1 = new Container<>(table1);
        Container<Table> cont2 = new Container<>(table2);

        cont1.setSize(400, 130);
        cont2.setSize(400, 130);

        cont1.setBackground(skin.getDrawable("default-round"));
        cont2.setBackground(skin.getDrawable("default-round"));

        cont1.setPosition(0, Gdx.graphics.getHeight() - cont1.getHeight());
        cont2.setPosition(Gdx.graphics.getWidth() - cont2.getWidth(), Gdx.graphics.getHeight() - cont2.getHeight());

        this.stageHUD.addActor(cont1);
        this.stageHUD.addActor(cont2);
    }
    
    public void actualizar() {
        if (jugador1.getPersonajeElegido() != null) {
            this.nombrePersonaje1Label.setText("Nombre: " + jugador1.getPersonajeElegido().getNombre());
            this.vidaPersonaje1Label.setText("Vida: " + jugador1.getPersonajeElegido().getVida());
        }

        if (jugador2.getPersonajeElegido() != null) {
            this.nombrePersonaje2Label.setText("Nombre: " + jugador2.getPersonajeElegido().getNombre());
            this.vidaPersonaje2Label.setText("Vida: " + jugador2.getPersonajeElegido().getVida());
        }
    }
    
    public void dispose() {
        if (skin != null) skin.dispose();
    }
}
