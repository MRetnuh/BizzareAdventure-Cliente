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
    private boolean saltar2 = false;
    private boolean derecha2 = false;
    private boolean izquierda2 = false;
    private boolean atacar2 = false;
    private boolean opciones2 = false;
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
                this.saltar2 = false;
                this.izquierda2 = false;
                this.derecha2 = false;
                this.atacar2 = false;
                break;
            case (Input.Keys.K):
                this.atacar1 = true;
                break;
            case (Input.Keys.RIGHT):
                this.derecha2 = true;
                break;
            case (Input.Keys.LEFT):
                this.izquierda2 = true;
                break;
            case (Input.Keys.UP):
                this.saltar2 = true;
                break;
            case (Input.Keys.M):
                this.atacar2 = true;
                break;
            case (Input.Keys.O):
            	this.saltar1 = false;
            	this.izquierda1 = false;
            	this.derecha1 = false;
            	this.atacar1 = false;
                this.opciones2 = true;
                this.saltar2 = false;
                this.izquierda2 = false;
                this.derecha2 = false;
                this.atacar2 = false;
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
            case (Input.Keys.RIGHT):
                this.derecha2 = false;
                break;
            case (Input.Keys.LEFT):
                this.izquierda2 = false;
                break;
            case (Input.Keys.UP):
                this.saltar2 = false;
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
	    derecha1 = false;
	    izquierda1 = false;
	    saltar1 = false;
	    atacar1 = false;
	    opciones1 = false;

	    derecha2 = false;
	    izquierda2 = false;
	    saltar2 = false;
	    atacar2 = false;
	    opciones2 = false;
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
    
    public void setAtacarFalso2() {
        this.atacar2 = false;
    }
    
    public boolean getOpciones1() {
        return  this.opciones1;
    }

    public boolean getOpciones2() {
        return  this.opciones2;
    }

    public boolean getSaltar2() {
        return  this.saltar2;
    }

    public boolean getDerecha2() {
        return  this.derecha2;
    }

    public boolean getIzquierda2() {
        return  this.izquierda2;
    }

    public boolean getAtacar2() {
        return  this.atacar2;
    }

	public void setOpcionesFalso1() {
		this.opciones1 = false;
	}
	
	public void setOpcionesFalso2() {
		this.opciones2 = false;
	}
}