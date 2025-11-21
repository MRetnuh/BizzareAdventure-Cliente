package niveles;

import enemigos.EnemigoBase;
import enemigos.EnemigoPerseguidor;
import enemigos.EnemigoPesado;
import enemigos.EnemigoTirador;
import enemigos.TipoEnemigo;

public class Nivel2 extends NivelBase {

    public Nivel2() {
        super("Nivel 2", "mapacorregido.tmx");
    }

    @Override
    public void definirPosicionesIniciales() {
        this.inicioX1 = 500;
        this.inicioY1 = 930;
        this.inicioX2 = 500;
        this.inicioY2 = 930;
    }

    @Override
    public void crearEnemigos() {
        this.enemigos.clear();

        Object[][] enemigosDatos = {
            {"enemigo1", TipoEnemigo.TIRADOR, 1000f, 928f},
            {"enemigo2", TipoEnemigo.TIRADOR, 1200f, 928f},
            {"enemigo3", TipoEnemigo.TIRADOR, 1400f, 928f},
        };

        for (Object[] datos : enemigosDatos) {
            String id = (String) datos[0];
            TipoEnemigo tipo = (TipoEnemigo) datos[1];
            float x = (float) datos[2];
            float y = (float) datos[3];

            if (!super.enemigosMuertos.contains(id)) {
                EnemigoBase enemigo = null;

                // MISMO ESTILO QUE NIVEL1
                if (tipo == TipoEnemigo.PERSEGUIDOR) {
                    enemigo = new EnemigoPerseguidor(id, x, y);
                } else if (tipo == TipoEnemigo.TIRADOR) {
                    enemigo = new EnemigoTirador(id, x, y);
                } else {
                    enemigo = new EnemigoPesado(id, x, y);
                }

                this.enemigos.add(enemigo);
            }
        }
    }

    @Override
    public boolean comprobarVictoria(float nuevaX1, float nuevaY1, float nuevaX2, float nuevaY2) {
        return (nuevaX1 >= 3502.00 && nuevaX1 <= 3700.00 && nuevaY1 >= 1250.00) ||
               (nuevaX2 >= 3502.00 && nuevaX2 <= 3700.00 && nuevaY2 >= 1250.00);
    }
}
