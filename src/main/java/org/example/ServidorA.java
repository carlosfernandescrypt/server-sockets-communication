package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.*;

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

                // Processa cada cliente em uma thread separada
                executorService.execute(() -> processarCliente(clienteSocket));
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor A: " + e.getMessage());
        }
    }

    private void processarCliente(Socket clienteSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true)) {

            // Recebe a query de busca do cliente
            String queryBusca = in.readLine();
            System.out.println("Query recebida: " + queryBusca);

            // Cria requisição para os servidores B e C
            JSONObject requisicao = new JSONObject();
            requisicao.put("tipo", "BUSCA");
            requisicao.put("query", queryBusca);

            // Executa buscas em paralelo nos servidores B e C
            CompletableFuture<List<JSONObject>> futureB = CompletableFuture.supplyAsync(() ->
                    buscarEmServidor(HOST_SERVIDOR_B, PORTA_SERVIDOR_B, requisicao)
            );

            CompletableFuture<List<JSONObject>> futureC = CompletableFuture.supplyAsync(() ->
                    buscarEmServidor(HOST_SERVIDOR_C, PORTA_SERVIDOR_C, requisicao)
            );

            // Aguarda os resultados de ambos os servidores
            List<JSONObject> resultadosB = futureB.get(30, TimeUnit.SECONDS);
            List<JSONObject> resultadosC = futureC.get(30, TimeUnit.SECONDS);

            // Combina os resultados
            List<JSONObject> resultadosCombinados = new ArrayList<>();
            resultadosCombinados.addAll(resultadosB);
            resultadosCombinados.addAll(resultadosC);

            // Prepara resposta para o cliente
            JSONObject resposta = new JSONObject();
            resposta.put("total", resultadosCombinados.size());
            resposta.put("resultados", new JSONArray(resultadosCombinados));

            // Envia resposta ao cliente
            out.println(resposta.toString());
            System.out.println("Resposta enviada ao cliente: " + resultadosCombinados.size() + " resultados encontrados");

        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Erro ao processar cliente: " + e.getMessage());
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar conexão com cliente: " + e.getMessage());
            }
        }
    }

    private List<JSONObject> buscarEmServidor(String host, int porta, JSONObject requisicao) {
        List<JSONObject> resultados = new ArrayList<>();

        try (Socket socket = new Socket(host, porta);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Envia requisição
            out.println(requisicao.toString());

            // Recebe resposta
            String respostaStr = in.readLine();
            JSONObject resposta = new JSONObject(respostaStr);

            // Extrai resultados
            if (resposta.has("resultados")) {
                JSONArray resultadosArray = resposta.getJSONArray("resultados");
                for (int i = 0; i < resultadosArray.length(); i++) {
                    resultados.add(resultadosArray.getJSONObject(i));
                }
            }

            System.out.println("Recebidos " + resultados.size() + " resultados do servidor " + host + ":" + porta);

        } catch (IOException e) {
            System.err.println("Erro ao conectar com servidor " + host + ":" + porta + " - " + e.getMessage());
        }

        return resultados;
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

        // Adiciona shutdown hook para parar o servidor graciosamente
        Runtime.getRuntime().addShutdownHook(new Thread(servidor::parar));

        servidor.iniciar();
    }
}