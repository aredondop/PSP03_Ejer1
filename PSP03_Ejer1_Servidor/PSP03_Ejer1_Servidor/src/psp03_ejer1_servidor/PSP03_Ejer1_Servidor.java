package psp03_ejer1_servidor;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase que implementa un servidor para un juego de adivinanza de números.
 * El servidor genera un número aleatorio y el cliente intenta adivinarlo.
 * Proporciona mensajes de retroalimentación al cliente.
 * 
 * @author Ángel Redondo
 */

public class PSP03_Ejer1_Servidor {

    private static final int PUERTO = 2000;

    public static void main(String[] args) {
        new PSP03_Ejer1_Servidor().iniciar();
    }

    public void iniciar() {
        ExecutorService pool = Executors.newFixedThreadPool(5); // Número máximo de clientes concurrentes

        try {
            ServerSocket skServidor = new ServerSocket(PUERTO);
            System.out.println("Escuchando en el puerto " + PUERTO);

            while (true) {
                Socket skCliente = skServidor.accept();
                System.out.println("Cliente conectado");

                // Crea un nuevo hilo para manejar al cliente
                ClienteHandler clienteHandler = new ClienteHandler(skCliente);
                pool.execute(clienteHandler);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static class ClienteHandler implements Runnable {
        private final Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream flujoEntrada = new DataInputStream(socket.getInputStream());
                DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());

                flujoSalida.writeUTF("Tienes que adivinar un numero del 1 al 100");

                Random randomGenerator = new Random();
                int numSecreto = randomGenerator.nextInt(100);

                while (true) {
                    String numCliente = flujoEntrada.readUTF();
                    System.out.println("\tEl cliente ha dicho " + numCliente);

                    if (numSecreto == Integer.parseInt(numCliente)) {
                        flujoSalida.writeUTF("Correcto");
                        break;
                    } else {
                        flujoSalida.writeUTF(numSecreto > Integer.parseInt(numCliente) ?
                                "El numero secreto es mayor" :
                                "El numero secreto es menor");
                    }
                }

                flujoSalida.writeUTF("FIN");
                socket.close();
                System.out.println("Cliente desconectado");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
