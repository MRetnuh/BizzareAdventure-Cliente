package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HiloCliente extends Thread{
	 	private DatagramSocket socket;
	    private int servidorPort = 5555;
	    private String ipServidorStr = "255.255.255.255";
	    private InetAddress ipServidor;
	    private boolean fin = false;
	    
	    public HiloCliente() {
	        try {
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
	            } catch (IOException e) {
//	                throw new RuntimeException(e);
	            }
	        } while(!this.fin);
	    }
}
