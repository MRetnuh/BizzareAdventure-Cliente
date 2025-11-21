package input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import juego.Partida;
import personajes.Personaje;

public class InputController implements InputProcessor {


    private boolean saltar1 = false;
    private boolean derecha1 = false;
    private boolean izquierda1 = false;
    private boolean atacar1 = false;
    private boolean opciones1 = false;
    public InputController() {
}

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case (Input.Keys.D):
                this.derecha1 = true;
                break;
            case (Input.Keys.A):
                this.izquierda1 = true;
                break;
            case (Input.Keys.W):
                this.saltar1 = true;
                break;
            case (Input.Keys.P):
                this.opciones1 = true;
                this.saltar1 = false;
                this.izquierda1 = false;
                this.derecha1 = false;
                this.atacar1 = false;
                break;
            case (Input.Keys.K):
                this.atacar1 = true;
                break;
            case (Input.Keys.O):
            	this.saltar1 = false;
            	this.izquierda1 = false;
            	this.derecha1 = false;
            	this.atacar1 = false;
        }
            return false;
        }


    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case (Input.Keys.D):
                this.derecha1 = false;
                break;
            case (Input.Keys.A):
                this.izquierda1 = false;
                break;
            case (Input.Keys.W):
                this.saltar1 = false;
                break;

        }
        return false;
    }


    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	public void resetearInputs() {
	    this.derecha1 = false;
	    this.izquierda1 = false;
	    this.saltar1 = false;
	    this.atacar1 = false;
	    this.opciones1 = false;
	}

	
    public boolean getSaltar1() {
        return this.saltar1;
    }

    public boolean getDerecha1() {
        return this.derecha1;
    }

    public boolean getIzquierda1() {
        return this.izquierda1;
    }

    public boolean getAtacar1() {
        return  this.atacar1;
    }
    
    public void setAtacarFalso1() {
        this.atacar1 = false;
    }
    public boolean getOpciones1() {
        return  this.opciones1;
    }

   
	public void setOpcionesFalso1() {
		this.opciones1 = false;
	}
	
}