package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
	                this.gameController.empezar();
	                break;
	            case "UpdateState": // NUEVO CASO para recibir la actualización del servidor
	                // Formato ejemplo: "UpdateState:1:posX1:posY1:vida1:estado1:2:posX2:posY2:vida2:estado2"
	                // El servidor enviará la posición/estado de AMBOS jugadores.
	                this.gameController.actualizarEstado(partes);
	                break;
	            case "FinJuego":
	                this.gameController.perder();
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
