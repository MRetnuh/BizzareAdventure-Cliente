package niveles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import enemigos.EnemigoBase;
import mecanicas.GestorColisiones;

public abstract class NivelBase {
	private final GestorColisiones gestorColisiones = new GestorColisiones();
	private String nombreMapa;
    private String nombreNivel;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int anchoMapa;
    private int alturaMapa;
    private Set<String> cajasDestruidas = new HashSet<>();
   
    protected Set<String> enemigosMuertos = new HashSet<>(); 
    protected List<EnemigoBase> enemigos = new ArrayList<>();
    protected float inicioX1, inicioY1;
    protected float inicioX2, inicioY2;
    
    
    public NivelBase(String nombreNivel, String nombreMapa) {
        this.nombreMapa = nombreMapa;
        this.nombreNivel = nombreNivel;
        cargarMapa();
        definirPosicionesIniciales(); 
    }
    
    public abstract void definirPosicionesIniciales();
    public abstract void crearEnemigos(); 
    public abstract boolean comprobarVictoria(float nuevaX1, float nuevaY1, float nuevaX2, float nuevaY2);

    
    private void cargarMapa() {
        this.mapa = new TmxMapLoader().load(this.nombreMapa);
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.mapa);
        this.anchoMapa = mapa.getProperties().get("width", Integer.class) * this.mapa.getProperties().get("tilewidth", Integer.class);
        this.alturaMapa = mapa.getProperties().get("height", Integer.class) * this.mapa.getProperties().get("tileheight", Integer.class);
    }
    
    public boolean detectarColision(Rectangle hitbox) {
        return gestorColisiones.detectarColision(this.mapa, hitbox);
    }

    public boolean destruirCajaEnHitbox(Rectangle hitbox) {
        return gestorColisiones.destruirCajaEnHitbox(this.mapa, hitbox, this.cajasDestruidas);
    }
    
    public void restaurarEstadoCajas() {
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) this.mapa.getLayers().get("cajasInteractivas");
        if (tileLayer == null) return;
        for (String key : this.cajasDestruidas) {
            String[] partes = key.split("_");
            int x = Integer.parseInt(partes[0]);
            int y = Integer.parseInt(partes[1]);
            tileLayer.setCell(x, y, null); 
        }
    }
  
    public void dispose() {
        if (this.mapa != null) this.mapa.dispose();
        if (this.mapRenderer != null) this.mapRenderer.dispose();
    }
    
    public void agregarEnemigosMuertos(EnemigoBase enemigo) {
    	this.enemigosMuertos.add(enemigo.getNombre());
    }  

    public OrthogonalTiledMapRenderer getMapRenderer() { return this.mapRenderer; }
    public List<EnemigoBase> getEnemigos() { return this.enemigos; }
    public int getAnchoMapa() { return this.anchoMapa; }
    public int getAlturaMapa() { return this.alturaMapa; }
    public float getInicioX1() { return this.inicioX1; }
    public float getInicioY1() { return this.inicioY1; }
    public float getInicioX2() { return this.inicioX2; }
    public float getInicioY2() { return this.inicioY2; }
    public String getNombreNivel()  { return this.nombreNivel; }
}