package mecanicas;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import java.util.Set;
import java.util.HashSet;

public class GestorColisiones {

    private final int ID_TILE_TRANSPARENTE = 0;

    public boolean detectarColision(TiledMap mapa, Rectangle hitbox) {
    	Polygon hitboxPoligono = convertirEnPoligono(hitbox);

        for (MapObject object : mapa.getLayers().get("colisiones").getObjects()) {
            String clase = object.getProperties().get("type", String.class);
            if (clase == null || !clase.equals("Tierra")) continue;

            if (object instanceof RectangleMapObject) {
                Rectangle rectMapa = ((RectangleMapObject) object).getRectangle();
                if (hitbox.overlaps(rectMapa)) return true;
            } else if (object instanceof PolygonMapObject) {
                PolygonMapObject polygonObject = (PolygonMapObject) object;
                Polygon polygon = polygonObject.getPolygon();
                float x = polygonObject.getProperties().get("x", Float.class);
                float y = polygonObject.getProperties().get("y", Float.class);
                Polygon poligonoTransformado = new Polygon(polygon.getVertices());
                poligonoTransformado.setPosition(x, y);
                if (Intersector.overlapConvexPolygons(hitboxPoligono, poligonoTransformado)) {
                    return true;
                }
            }
        }
        for (MapObject object : mapa.getLayers().get("interactivos").getObjects()) {
            String clase = object.getProperties().get("type", String.class);
            if (clase == null || !clase.equals("Tierra")) continue;

            Rectangle rectMapa = null;

            if (object instanceof RectangleMapObject) {
                rectMapa = ((RectangleMapObject) object).getRectangle();
                if (!hitbox.overlaps(rectMapa)) continue;
            } else if (object instanceof PolygonMapObject) {
                PolygonMapObject polygonObject = (PolygonMapObject) object;
                Polygon polygon = polygonObject.getPolygon();
                float x = polygonObject.getProperties().get("x", Float.class);
                float y = polygonObject.getProperties().get("y", Float.class);
                Polygon poligonoTransformado = new Polygon(polygon.getVertices());
                poligonoTransformado.setPosition(x, y);
                if (!Intersector.overlapConvexPolygons(hitboxPoligono, poligonoTransformado)) continue;

                rectMapa = poligonoTransformado.getBoundingRectangle();
            }

            TiledMapTileLayer tileLayer = (TiledMapTileLayer) mapa.getLayers().get("cajasInteractivas");
            if (tileLayer != null) {
                int startX = (int) (rectMapa.x / tileLayer.getTileWidth());
                int endX = (int) ((rectMapa.x + rectMapa.width) / tileLayer.getTileWidth());
                int startY = (int) (rectMapa.y / tileLayer.getTileHeight());
                int endY = (int) ((rectMapa.y + rectMapa.height) / tileLayer.getTileHeight());

                boolean tieneTile = false;
                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                        TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                        if (cell != null && cell.getTile() != null) {
                            int tileId = cell.getTile().getId();
                            if (tileId != this.ID_TILE_TRANSPARENTE) {
                                tieneTile = true;
                                break;
                            }
                        }
                    }
                    if (tieneTile) break;
                }

                if (!tieneTile) continue;
            }

            return true;
        }

        return false;
    }

    public boolean destruirCajaEnHitbox(TiledMap mapa, Rectangle hitbox, Set<String> cajasDestruidas) {
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) mapa.getLayers().get("cajasInteractivas");
        if (tileLayer == null) return false;

        float tileWidth = tileLayer.getTileWidth();
        float tileHeight = tileLayer.getTileHeight();

        Set<String> cajasProcesadas = new HashSet<>();
        boolean seDestruyoAlgunaCaja = false;

        final float EPSILON = 0.0001f;
        int startX = (int) Math.floor(hitbox.x / tileWidth);
        int startY = (int) Math.floor(hitbox.y / tileHeight);
        int endX = (int) Math.floor((hitbox.x + hitbox.width - EPSILON) / tileWidth);
        int endY = (int) Math.floor((hitbox.y + hitbox.height - EPSILON) / tileHeight);

        for (int mapX = startX; mapX <= endX; mapX++) {
            for (int mapY = startY; mapY <= endY; mapY++) {
                TiledMapTileLayer.Cell cell = tileLayer.getCell(mapX, mapY);

                if (cell != null && cell.getTile() != null && cell.getTile().getId() != ID_TILE_TRANSPARENTE) {

                    int origenX = (mapX / 2) * 2;
                    int origenY = (mapY / 2) * 2;
                    String keyOrigen = origenX + "_" + origenY;

                    if (!cajasProcesadas.contains(keyOrigen)) {
                        cajasProcesadas.add(keyOrigen);

                        boolean destruccionExitosa = destruirMatrizCaja(mapa, tileLayer, origenX, origenY, cajasDestruidas);

                        if (destruccionExitosa) {
                            seDestruyoAlgunaCaja = true;
                        }
                    }
                }
            }
        }
        return seDestruyoAlgunaCaja;
    }

    private boolean destruirMatrizCaja(TiledMap mapa, TiledMapTileLayer tileLayer, int origenX, int origenY, Set<String> cajasDestruidas) {
        boolean destruida = false;
        for (int dx = 0; dx < 2; dx++) {
            for (int dy = 0; dy < 2; dy++) {
                int tileX = origenX + dx;
                int tileY = origenY + dy;

                if (tileX < tileLayer.getWidth() && tileY < tileLayer.getHeight()) {
                    TiledMapTileLayer.Cell cell = tileLayer.getCell(tileX, tileY);
                    if (cell != null && cell.getTile() != null && cell.getTile().getId() != ID_TILE_TRANSPARENTE) {
                        cajasDestruidas.add(tileX + "_" + tileY);
                        cell.setTile(mapa.getTileSets().getTile(ID_TILE_TRANSPARENTE));
                        destruida = true;
                    }
                }
            }
        }
        return destruida;
    }

    private Polygon convertirEnPoligono(Rectangle rect) {
        Polygon poly = new Polygon(new float[]{
                0, 0,
                rect.width, 0,
                rect.width, rect.height,
                0, rect.height
        });
        poly.setPosition(rect.x, rect.y);
        return poly;
    }
}
