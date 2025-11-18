package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;

import interfaces.GameController;

public class HiloCliente extends Thread{
	 	private DatagramSocket socket;
	    private int servidorPort = 5555;
	    private String ipServidorStr = "255.255.255.255";
	    private InetAddress ipServidor;
	    private boolean fin = false;
	    private GameController gameController;
	    
	    public HiloCliente(GameController gameController) {
	        try {
	        	this.gameController = gameController;
	        	this.ipServidor = InetAddress.getByName(this.ipServidorStr);
	            this.socket = new DatagramSocket();
	        } catch (SocketException | UnknownHostException e) {
//	            throw new RuntimeException(e);
	        }
	    }
	    @Override
	    public void run() {
	        do {
	            DatagramPacket paquete = new DatagramPacket(new byte[1024], 1024);
	            try {
	                this.socket.receive(paquete);
	                procesarMensaje(paquete);
	            } catch (IOException e) {
//	                throw new RuntimeException(e);
	            }
	        } while(!this.fin);
	    }
	    
	    private void procesarMensaje(DatagramPacket packet) {
	        String mensaje = (new String(packet.getData())).trim();
	        String[] partes = mensaje.split(":");

	        System.out.println("Mensaje recibido: " + mensaje);

	        switch(partes[0]){
	            case "Yaconectado":
	                System.out.println("Ya estas conectado");
	                break;
	            case "Conectado":
	                System.out.println("Conectado al servidor");
	                this.ipServidor = packet.getAddress();
	                gameController.conectar(Integer.parseInt(partes[1]));
	                break;
	            case "Lleno":
	                System.out.println("Servidor lleno");
	                this.fin = true;
	                break;
	            case "Empezar":
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
	            case "CajaRota":
	            	this.gameController.eliminarCaja(partes);
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
	        }

	    }

	    public void sendMessage(String message) {
	        byte[] byteMessage = message.getBytes();
	        DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, this.ipServidor, this.servidorPort);
	        try {
	            this.socket.send(packet);
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    public void terminate() {
	        this.fin = true;
	        this.socket.close();
	        this.interrupt();
	    }
}
