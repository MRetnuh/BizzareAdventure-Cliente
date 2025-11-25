package estilos;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ListenerBotonTexto extends InputListener {

    private String textoNormal;
    private String textoHover;
    private Runnable accionClic; 
    
    public ListenerBotonTexto(String textoNormal, Runnable accionClic) {
        this.textoNormal = textoNormal;
        this.textoHover = "- " + textoNormal + " -";
        this.accionClic = accionClic;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (pointer == -1) { 
            if (event.getListenerActor() instanceof TextButton) {
                ((TextButton) event.getListenerActor()).setText(this.textoHover);
            }
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (pointer == -1) {
            if (event.getListenerActor() instanceof TextButton) {
                ((TextButton) event.getListenerActor()).setText(this.textoNormal);
            }
        }
    }
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true; 
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (this.accionClic != null) {
           this.accionClic.run(); 
        }
    }
}