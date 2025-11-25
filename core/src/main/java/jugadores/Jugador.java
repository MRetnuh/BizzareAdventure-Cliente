package jugadores;

import java.util.Random;
import personajes.FabricaDePersonajes;
import personajes.Personaje;

public class Jugador {
	private int id;
    private boolean partidaEmpezada = false;
    private Personaje personajeElegido;
    private Random r = new Random();
    private int indicePersonaje;
    private FabricaDePersonajes[] personajesDisponibles = FabricaDePersonajes.values();
    private FabricaDePersonajes elegido;
    
    public Jugador(int id) {
    	this.id = id;
    }

    public void asignarPersonaje(int indice) {
        this.indicePersonaje = indice;
        this.elegido = this.personajesDisponibles[this.indicePersonaje];
        this.personajeElegido = this.elegido.crear();
        this.partidaEmpezada = true;
    }

    public Personaje cambiarPersonaje(float x, float y) {
        int nuevoIndice;
        do {
            nuevoIndice = this.r.nextInt(this.personajesDisponibles.length);
        } while (nuevoIndice == this.indicePersonaje);

        this.indicePersonaje = nuevoIndice;
        this.elegido = this.personajesDisponibles[this.indicePersonaje];
        this.personajeElegido = this.elegido.crear();
        this.personajeElegido.cargarUbicaciones(x, y);
        this.personajeElegido.aumentarVida();
        return this.personajeElegido;
    }

    public Personaje getPersonajeElegido() {
        return this.personajeElegido;
    }

    public boolean getPartidaEmpezada() {
        return this.partidaEmpezada;
    }

	public int getNumPlayer() {
		return this.id;
	}
}
