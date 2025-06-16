package org.example;

public class ServidorC extends ServidorBusca {


    public ServidorC() {
    }

    public static void main(String[] args) {

    }
}

package org.example;


public class ServidorC extends ServidorBusca {

    private static final int PORTA = 8082;
    private static final String NOME = "Servidor C";
    private static final String ARQUIVO_DADOS = "dados_servidor_c.json";

    public ServidorC() {
        super(PORTA, NOME, ARQUIVO_DADOS);
    }

    public static void main(String[] args) {
        ServidorC servidor = new ServidorC();

        Runtime.getRuntime().addShutdownHook(new Thread(servidor::parar));

        servidor.iniciar();
    }
}
```
