package enemigos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import niveles.NivelBase;
import personajes.Personaje;
import personajes.TipoAtaque;

public abstract class EnemigoBase extends Personaje{
	  	protected float rangoMovimiento = 200;
	  	protected float rangoVision = 250;
	  	protected float puntoInicialX;
	  	protected Personaje objetivoActual = null;
	  	protected float tiempoSinVerJugador = 0f;
	  	protected final float TIEMPOPARAOLVIDAR = 1.0f; 
	  	protected final float TOLERANCIAVERTICAL = 100f; 
	    
	  	private TipoEnemigo tipoEnemigo;
	  	
	public EnemigoBase(String nombre, int velocidad, String nombreAtaque, int vida, TipoEnemigo tipo) {
		super(nombre, velocidad, nombreAtaque, vida, null);
		this.tipoEnemigo = tipo;
		
	}
	
	public abstract void actualizarIA(float delta, Personaje jugador1, Personaje jugador2, float volumen, NivelBase nivel);
		
	
	protected void seleccionarObjetivo(Personaje j1, Personaje j2) {
		 Personaje nuevoObjetivo = null;

	        boolean j1Vivo = j1 != null && j1.getVida() > 0;
	        boolean j2Vivo = j2 != null && j2.getVida() > 0;

	        if (this.objetivoActual != null) {
	            if (this.objetivoActual.getVida() > 0 && detectarRangoConTolerancia(this.objetivoActual)) {
	                this.tiempoSinVerJugador = 0;
	                return;
	            } else {
	            	
	                this.tiempoSinVerJugador += Gdx.graphics.getDeltaTime();
	                if (this.tiempoSinVerJugador >= this.TIEMPOPARAOLVIDAR) {
	                    this.objetivoActual = null;
	                    this.tiempoSinVerJugador = 0;
	                }
	                return;
	            }
	        }
	        
	        if (j1Vivo && detectarRangoConTolerancia(j1)) nuevoObjetivo = j1;
	        else if (j2Vivo && detectarRangoConTolerancia(j2)) nuevoObjetivo = j2;

	        this.objetivoActual = nuevoObjetivo;
	}
	
	private boolean detectarRangoConTolerancia(Personaje jugador) {
		  float distanciaX = Math.abs(jugador.getX() - super.getX());
	        float distanciaY = Math.abs(jugador.getY() - super.getY());

	        return distanciaX <= this.rangoVision && distanciaY <= this.TOLERANCIAVERTICAL;
	}
	
	
	
	protected void patrullar(float delta, NivelBase nivel) {
		 float nuevaX = super.getX() + (this.moviendoDerecha ? super.velocidad : -super.velocidad) * delta;
	        Rectangle hitbox = new Rectangle(nuevaX, super.getY(), getWidth(), getHeight());

	        if (!nivel.detectarColision(hitbox)) {
	            super.aplicarMovimiento(nuevaX, super.getY(), delta, 10000, 1000);
	        } else {
	            this.moviendoDerecha = !this.moviendoDerecha;
	        }

	        if (super.getX() > this.puntoInicialX + this.rangoMovimiento) this.moviendoDerecha = false;
	        if (super.getX() < this.puntoInicialX - this.rangoMovimiento) this.moviendoDerecha = true;
	    }
	
	public TipoEnemigo getTipoEnemigo() {
		return this.tipoEnemigo;
	}
}


