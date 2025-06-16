package org.example;


public class ServidorB extends ServidorBusca {
    public ServidorB() {

    }

    public static void main(String[] args) {

    }
}

package org.example;


public class ServidorB extends ServidorBusca {

    private static final int PORTA = 8081;
    private static final String NOME = "Servidor B";
    private static final String ARQUIVO_DADOS = "dados_servidor_b.json";

    public ServidorB() {
        super(PORTA, NOME, ARQUIVO_DADOS);
    }

    public static void main(String[] args) {
        ServidorB servidor = new ServidorB();

        Runtime.getRuntime().addShutdownHook(new Thread(servidor::parar));

        servidor.iniciar();
    }
}
```
