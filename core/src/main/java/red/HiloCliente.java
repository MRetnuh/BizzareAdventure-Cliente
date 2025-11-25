package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;

import interfaces.GameController;

public class HiloCliente extends Thread {
    private DatagramSocket socket;
    private int servidorPort = 5555;
    private String ipServidorStr = "255.255.255.255";
    private InetAddress ipServidor;
    private boolean fin = false;
    private GameController gameController;
    private boolean conexionEntreJugadores = false;
    private boolean enJuego = false;
    private long ultimaActividadServidor = System.currentTimeMillis();
    private final long TIMEOUT_SERVIDOR = 4000; 

    public HiloCliente(GameController gameController) {
        try {
            this.gameController = gameController;
            this.ipServidor = InetAddress.getByName(this.ipServidorStr);
            this.socket = new DatagramSocket();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        new Thread(this::verificarTimeout).start(); // Comienza el hilo de control de timeout

        do {
            DatagramPacket paquete = new DatagramPacket(new byte[1024], 1024);
            try {
                this.socket.receive(paquete);
                procesarMensaje(paquete);  // Procesa el mensaje y actualiza la actividad
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!this.fin);
    }

    private void verificarTimeout() {
        while (!this.fin) {

            // ❗ Si NO estás en partida, no hacer timeout
            if (!this.enJuego) {
                this.ultimaActividadServidor = System.currentTimeMillis();
                try { Thread.sleep(500); } catch (Exception e){}
                continue;
            }

            long ahora = System.currentTimeMillis();
            if (ahora - this.ultimaActividadServidor > this.TIMEOUT_SERVIDOR) {
                System.out.println("Servidor no responde. Desconectado.");
                desconectarPorTimeout();
            }

            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
    }


    private void procesarMensaje(DatagramPacket packet) {
        String mensaje = (new String(packet.getData())).trim();
        this.ultimaActividadServidor = System.currentTimeMillis();  // Actualiza el tiempo de última actividad
        String[] partes = mensaje.split(":");

        System.out.println("Mensaje recibido: " + mensaje);

        switch (partes[0]) {
            case "Yaconectado":
                System.out.println("Ya estas conectado");
                break;
            case "Conectado":
                System.out.println("Conectado al servidor");
                this.ipServidor = packet.getAddress();
                this.gameController.conectar(Integer.parseInt(partes[1]));
                break;
            case "Lleno":
                System.out.println("Servidor lleno");
                this.fin = true;
                break;
            case "Empezar":
                this.conexionEntreJugadores = true;
                this.gameController.empezar(Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
                break;
            case "Nivel":
                this.gameController.asignarNivel(Integer.parseInt(partes[1]));
                break;
            case "UpdateState":
                this.gameController.actualizarEstado(partes);
                break;
            case "Derrota":
                this.gameController.perder();
                break;
            case "Animar":
                this.gameController.animar(Integer.parseInt(partes[1]), Boolean.parseBoolean(partes[2]),
                        Boolean.parseBoolean(partes[3]), Boolean.parseBoolean(partes[4]));
                break;
            case "Atacar":
                this.gameController.animarPersonajeAtaque(partes);
                break;
            case "MovimientoEnemigos":
                this.gameController.actualizarMovimientoEnemigos(partes);
                break;
            case "BalasEnemigos":
                this.gameController.actualizarBalasEnemigos(partes);
                break;
            case "ActualizarBalas":
                this.gameController.actualizarPosicionBalas(partes);
                break;
            case "CajaRota":
                this.gameController.eliminarCaja(partes);
                break;
            case "BalaImpactada":
                this.gameController.eliminarBala(partes);
                break;
            case "CambioPersonaje":
                this.gameController.cambiarPersonaje(Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
                break;
            case "NivelCompletado":
                this.gameController.avanzarNivel(partes);
                break;
            case "CambioPersonajesNivel":
                this.gameController.cambiarPersonajesPorNivel(partes);
                break;
            case "Desconectado":
                this.gameController.volverAlMenu();
                break;
            case "Victoria":
                this.gameController.ganarPartida();
                break;
            case "ErrorJugador":
                System.out.println("El servidor ha desconectado al jugador.");
                this.gameController.tirarErrorPorDesconexion();
                break;
        }
    }

    public void enviarMensaje(String message) {
        byte[] byteMessage = message.getBytes();
        DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, this.ipServidor, this.servidorPort);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalizar() {
        this.fin = true;
        this.socket.close();
        this.interrupt();
        System.out.println("Conexión finalizada");
    }

    public boolean getConexionEntreJugadores() {
        return this.conexionEntreJugadores;
    }
    
    public void setEnJuego(boolean e) {
        this.enJuego = e;
        enviarMensaje("ActivarEnJuego");
    }

    private void desconectarPorTimeout() {
       finalizar();
        // Avisar al juego sobre la desconexión por timeout
        Gdx.app.postRunnable(() -> {
            this.gameController.tirarErrorPorDesconexion();
        });
    }
}
