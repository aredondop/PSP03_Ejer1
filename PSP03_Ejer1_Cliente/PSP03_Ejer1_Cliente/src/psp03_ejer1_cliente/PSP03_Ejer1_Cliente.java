package psp03_ejer1_cliente;

import java.io.*;
import java.net.*;

/**
 * Clase que implementa un cliente para un juego de adivinanza de números.
 * El cliente se conecta a un servidor, intenta adivinar un número y recibe mensajes de retroalimentación.
 * 
 * @author Ángel Redondo
 */
public class PSP03_Ejer1_Cliente {

    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 2000;

    public static void main(String[] args) {
        new PSP03_Ejer1_Cliente().iniciar();
    }

    public void iniciar() {
        try {
            Socket socket = new Socket(SERVIDOR, PUERTO);
            System.out.println("Conectado al servidor");

            DataInputStream flujoEntrada = new DataInputStream(socket.getInputStream());
            DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String respuesta;

            do {
                System.out.println(flujoEntrada.readUTF());

                while (true) {
                    System.out.println("Introduce un numero:");
                    String numCliente = reader.readLine();
                    flujoSalida.writeUTF(numCliente);
                    flujoSalida.flush();

                    String mensaje = flujoEntrada.readUTF();
                    System.out.println(mensaje);

                    if (mensaje.equals("Correcto")) {
                        break;
                    }
                }

                System.out.println("Quieres jugar de nuevo? (S/N)");
                respuesta = reader.readLine();
            } while (respuesta.equalsIgnoreCase("S"));

            flujoSalida.writeUTF("FIN");
            socket.close();
            System.out.println("Desconectado del servidor");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
