package org.example;


public class ServidorA {

    public ServidorA() {

    }

    public void iniciar() {

        }

    private void processarCliente(Socket clienteSocket) {

    }

    private List<JSONObject> buscarEmServidor(String host, int porta, JSONObject requisicao) {

    }

    public void parar() {

    }

    public static void main(String[] args) {

    }
}

package org.example;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ServidorA {
    private static final int PORTA_SERVIDOR_A = 8080;
    private static final int PORTA_SERVIDOR_B = 8081;
    private static final int PORTA_SERVIDOR_C = 8082;
    private static final String HOST_SERVIDOR_B = "localhost";
    private static final String HOST_SERVIDOR_C = "localhost";

    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public ServidorA() {
        this.executorService = Executors.newCachedThreadPool();
    }

    public void iniciar() {
        try {
            serverSocket = new ServerSocket(PORTA_SERVIDOR_A);
            System.out.println("Servidor A iniciado na porta " + PORTA_SERVIDOR_A);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());

                executorService.execute(() -> processarCliente(clienteSocket));
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor A: " + e.getMessage());
        }
    }

    private void processarCliente(Socket clienteSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true)) {

            String queryBusca = in.readLine();
            System.out.println("Query recebida: " + queryBusca);

            out.println("Query processada: " + queryBusca);

        } catch (IOException e) {
            System.err.println("Erro ao processar cliente: " + e.getMessage());
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar conexão com cliente: " + e.getMessage());
            }
        }
    }

    public void parar() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            executorService.shutdown();
        } catch (IOException e) {
            System.err.println("Erro ao parar servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ServidorA servidor = new ServidorA();

        Runtime.getRuntime().addShutdownHook(new Thread(servidor::parar));

        servidor.iniciar();
    }
}
```
