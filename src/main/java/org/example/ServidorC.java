package org.example;

/**
 * Servidor C - responsável por buscar na segunda metade dos dados.
 * Estende a classe ServidorBusca com as configurações específicas.
 */
public class ServidorC extends ServidorBusca {

    private static final int PORTA = 8082;
    private static final String NOME = "Servidor C";
    private static final String ARQUIVO_DADOS = "dados_servidor_c.json";

    public ServidorC() {
        super(PORTA, NOME, ARQUIVO_DADOS);
    }

    public static void main(String[] args) {
        ServidorC servidor = new ServidorC();

        // Adiciona shutdown hook para parar o servidor graciosamente
        Runtime.getRuntime().addShutdownHook(new Thread(servidor::parar));

        servidor.iniciar();
    }
}