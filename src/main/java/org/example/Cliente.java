package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

/**
 * Cliente do sistema de busca distribuído.
 * Conecta-se ao Servidor A para realizar buscas no dataset arXiv.
 */
public class Cliente {

    private static final String HOST_SERVIDOR_A = "localhost";
    private static final int PORTA_SERVIDOR_A = 8080;
    private static final int TIMEOUT = 60000; // 60 segundos de timeout

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Cliente de Busca Distribuída arXiv ===");
        System.out.println("Digite 'sair' para encerrar o programa\n");

        while (true) {
            System.out.print("Digite o termo de busca: ");
            String termoBusca = scanner.nextLine().trim();

            if ("sair".equalsIgnoreCase(termoBusca)) {
                System.out.println("Encerrando cliente...");
                break;
            }

            if (termoBusca.isEmpty()) {
                System.out.println("Por favor, digite um termo de busca válido.\n");
                continue;
            }

            // Realiza a busca
            realizarBusca(termoBusca);
            System.out.println(); // Linha em branco entre buscas
        }

        scanner.close();
    }

    private void realizarBusca(String termoBusca) {
        try (Socket socket = new Socket()) {
            // Configura timeout
            socket.setSoTimeout(TIMEOUT);

            // Conecta ao servidor A
            System.out.println("Conectando ao servidor...");
            socket.connect(new InetSocketAddress(HOST_SERVIDOR_A, PORTA_SERVIDOR_A), 5000);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Envia o termo de busca
                System.out.println("Enviando busca: \"" + termoBusca + "\"");
                out.println(termoBusca);

                // Recebe a resposta
                System.out.println("Aguardando resultados...");
                String respostaStr = in.readLine();

                if (respostaStr == null) {
                    System.err.println("Nenhuma resposta recebida do servidor.");
                    return;
                }

                // Processa e exibe os resultados
                processarResposta(respostaStr);

            }
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout: A busca demorou muito tempo. Tente novamente.");
        } catch (ConnectException e) {
            System.err.println("Erro: Não foi possível conectar ao servidor. Verifique se o servidor está rodando.");
        } catch (IOException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        }
    }

    private void processarResposta(String respostaStr) {
        try {
            JSONObject resposta = new JSONObject(respostaStr);
            int total = resposta.getInt("total");

            System.out.println("\n========== RESULTADOS DA BUSCA ==========");
            System.out.println("Total de resultados encontrados: " + total);

            if (total == 0) {
                System.out.println("Nenhum artigo encontrado com o termo buscado.");
                return;
            }

            JSONArray resultados = resposta.getJSONArray("resultados");

            // Exibe cada resultado
            for (int i = 0; i < resultados.length(); i++) {
                JSONObject artigo = resultados.getJSONObject(i);

                System.out.println("\n--- Resultado " + (i + 1) + " ---");
                System.out.println("Título: " + artigo.getString("title"));
                System.out.println("Categoria: " + artigo.getString("label"));
                System.out.println("Resumo: " + artigo.getString("abstract"));
                System.out.println("Servidor: " + artigo.getString("servidor"));

                if (i < resultados.length() - 1) {
                    System.out.println(); // Linha em branco entre resultados
                }
            }

            System.out.println("\n========================================");

        } catch (JSONException e) {
            System.err.println("Erro ao processar resposta do servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.iniciar();
    }
}