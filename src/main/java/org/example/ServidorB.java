package org.example;

/**
 * Servidor B - responsável por buscar na primeira metade dos dados.
 * Estende a classe ServidorBusca com as configurações específicas.
 */
public class ServidorB extends ServidorBusca {

    private static final int PORTA = 8081;
    private static final String NOME = "Servidor B";
    private static final String ARQUIVO_DADOS = "dados_servidor_b.json";

    public ServidorB() {
        super(PORTA, NOME, ARQUIVO_DADOS);
    }

    public static void main(String[] args) {
        ServidorB servidor = new ServidorB();

        // Adiciona shutdown hook para parar o servidor graciosamente
        Runtime.getRuntime().addShutdownHook(new Thread(servidor::parar));

        servidor.iniciar();
    }
}