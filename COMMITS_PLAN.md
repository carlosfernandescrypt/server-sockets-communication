# Plano de Commits - Sistema de Busca Distribuída

Este documento detalha como dividir o desenvolvimento do projeto entre 4 pessoas, com 4 commits cada uma, simulando um desenvolvimento colaborativo real.

## 👨‍💻 Pessoa 1 - Vinicius (Algoritmos e Interfaces)

### Commit 1: Criar interface para algoritmos de busca

**Arquivo:** `src/main/java/org/example/AlgoritmoBusca.java`

```java
package org.example;

import java.util.List;

/**
 * Interface para algoritmos de busca de padrões em strings.
 * Permite implementar diferentes algoritmos de busca (Boyer-Moore, KMP, Rabin-Karp, etc.)
 * mantendo a mesma interface para o sistema.
 */
public interface AlgoritmoBusca {

    /**
     * Busca a primeira ocorrência do padrão no texto.
     *
     * @param texto O texto onde será feita a busca
     * @param padrao O padrão a ser buscado
     * @return A posição da primeira ocorrência do padrão no texto, ou -1 se não encontrado
     */
    int buscar(String texto, String padrao);

    /**
     * Busca todas as ocorrências do padrão no texto.
     *
     * @param texto O texto onde será feita a busca
     * @param padrao O padrão a ser buscado
     * @return Lista com as posições de todas as ocorrências do padrão no texto
     */
    List<Integer> buscarTodos(String texto, String padrao);
}
```

**Mensagem do commit:**
```
feat: add search algorithm interface

- Create AlgoritmoBusca interface for pattern matching
- Define buscar() method for single occurrence search
- Define buscarTodos() method for multiple occurrences
- Add comprehensive JavaDoc documentation
```

### Commit 2: Implementar estrutura básica do Boyer-Moore

**Arquivo:** `src/main/java/org/example/BoyerMoore.java`

```java
package org.example;

import java.util.*;

/**
 * Implementação do algoritmo Boyer-Moore para busca de padrões em strings.
 *
 * O algoritmo Boyer-Moore é eficiente para busca de substrings, especialmente
 * quando o padrão é longo. Ele usa duas heurísticas principais:
 * 1. Bad Character Rule - pula caracteres que não correspondem
 * 2. Good Suffix Rule - usa informações sobre sufixos que já correspondem
 *
 * Complexidade de tempo: O(n + m) no melhor caso, O(nm) no pior caso
 * onde n é o tamanho do texto e m é o tamanho do padrão.
 */
public class BoyerMoore implements AlgoritmoBusca {

    private static final int NO_OF_CHARS = 256;

    @Override
    public int buscar(String texto, String padrao) {
        if (padrao.isEmpty() || texto.isEmpty()) {
            return -1;
        }

        if (padrao.length() > texto.length()) {
            return -1;
        }

        // Implementação básica - será expandida nos próximos commits
        int m = padrao.length();
        int n = texto.length();
        
        // Busca simples por enquanto
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m && texto.charAt(i + j) == padrao.charAt(j)) {
                j++;
            }
            if (j == m) {
                return i;
            }
        }
        
        return -1;
    }

    @Override
    public List<Integer> buscarTodos(String texto, String padrao) {
        // Será implementado no commit 4
        List<Integer> resultado = new ArrayList<>();
        int pos = buscar(texto, padrao);
        if (pos != -1) {
            resultado.add(pos);
        }
        return resultado;
    }
}
```

**Mensagem do commit:**
```
feat: implement basic Boyer-Moore algorithm structure

- Create BoyerMoore class implementing AlgoritmoBusca
- Add basic search method with pattern matching logic
- Include class documentation with algorithm complexity
- Set up character set constant for ASCII support
```

### Commit 3: Adicionar heurística Bad Character ao Boyer-Moore

**Arquivo:** `src/main/java/org/example/BoyerMoore.java` (versão atualizada)

```java
package org.example;

import java.util.*;

/**
 * Implementação do algoritmo Boyer-Moore para busca de padrões em strings.
 *
 * O algoritmo Boyer-Moore é eficiente para busca de substrings, especialmente
 * quando o padrão é longo. Ele usa duas heurísticas principais:
 * 1. Bad Character Rule - pula caracteres que não correspondem
 * 2. Good Suffix Rule - usa informações sobre sufixos que já correspondem
 *
 * Complexidade de tempo: O(n + m) no melhor caso, O(nm) no pior caso
 * onde n é o tamanho do texto e m é o tamanho do padrão.
 */
public class BoyerMoore implements AlgoritmoBusca {

    private static final int NO_OF_CHARS = 256;

    @Override
    public int buscar(String texto, String padrao) {
        if (padrao.isEmpty() || texto.isEmpty()) {
            return -1;
        }

        if (padrao.length() > texto.length()) {
            return -1;
        }

        int m = padrao.length();
        int n = texto.length();

        // Cria a tabela de bad character
        int[] badChar = preprocessBadCharacter(padrao);

        int s = 0; // deslocamento do padrão em relação ao texto

        while (s <= (n - m)) {
            int j = m - 1;

            // Reduz j enquanto os caracteres do padrão e texto correspondem
            while (j >= 0 && padrao.charAt(j) == texto.charAt(s + j)) {
                j--;
            }

            // Se o padrão está presente no deslocamento atual
            if (j < 0) {
                return s; // Retorna a posição onde o padrão foi encontrado
            } else {
                // Desloca o padrão usando bad character heuristic
                s += Math.max(1, j - badChar[texto.charAt(s + j)]);
            }
        }

        return -1; // Padrão não encontrado
    }

    /**
     * Pré-processa o padrão para criar a tabela de bad character.
     * A tabela armazena a última ocorrência de cada caractere no padrão.
     */
    private int[] preprocessBadCharacter(String padrao) {
        int[] badChar = new int[NO_OF_CHARS];

        // Inicializa todas as ocorrências como -1
        Arrays.fill(badChar, -1);

        // Preenche o valor atual da última ocorrência de cada caractere
        for (int i = 0; i < padrao.length(); i++) {
            badChar[padrao.charAt(i)] = i;
        }

        return badChar;
    }

    @Override
    public List<Integer> buscarTodos(String texto, String padrao) {
        // Será implementado no commit 4
        List<Integer> resultado = new ArrayList<>();
        int pos = buscar(texto, padrao);
        if (pos != -1) {
            resultado.add(pos);
        }
        return resultado;
    }
}
```

**Mensagem do commit:**
```
feat: add Bad Character heuristic to Boyer-Moore

- Implement preprocessBadCharacter method
- Create bad character lookup table for ASCII characters
- Optimize pattern matching with character skip logic
- Add detailed method documentation
```

### Commit 4: Completar Boyer-Moore com Good Suffix e buscarTodos

**Arquivo:** `src/main/java/org/example/BoyerMoore.java`

```java
package org.example;

import java.util.*;

/**
 * Implementação do algoritmo Boyer-Moore para busca de padrões em strings.
 *
 * O algoritmo Boyer-Moore é eficiente para busca de substrings, especialmente
 * quando o padrão é longo. Ele usa duas heurísticas principais:
 * 1. Bad Character Rule - pula caracteres que não correspondem
 * 2. Good Suffix Rule - usa informações sobre sufixos que já correspondem
 *
 * Complexidade de tempo: O(n + m) no melhor caso, O(nm) no pior caso
 * onde n é o tamanho do texto e m é o tamanho do padrão.
 */
public class BoyerMoore implements AlgoritmoBusca {

    private static final int NO_OF_CHARS = 256;

    @Override
    public int buscar(String texto, String padrao) {
        if (padrao.isEmpty() || texto.isEmpty()) {
            return -1;
        }

        if (padrao.length() > texto.length()) {
            return -1;
        }

        int m = padrao.length();
        int n = texto.length();

        // Cria a tabela de bad character
        int[] badChar = preprocessBadCharacter(padrao);

        // Cria a tabela de good suffix
        int[] shift = new int[m + 1];
        int[] bpos = new int[m + 1];
        preprocessGoodSuffix(padrao, shift, bpos);

        int s = 0; // deslocamento do padrão em relação ao texto

        while (s <= (n - m)) {
            int j = m - 1;

            // Reduz j enquanto os caracteres do padrão e texto correspondem
            while (j >= 0 && padrao.charAt(j) == texto.charAt(s + j)) {
                j--;
            }

            // Se o padrão está presente no deslocamento atual
            if (j < 0) {
                return s; // Retorna a posição onde o padrão foi encontrado
            } else {
                // Desloca o padrão usando o máximo entre bad character e good suffix
                s += Math.max(shift[j + 1], j - badChar[texto.charAt(s + j)]);
            }
        }

        return -1; // Padrão não encontrado
    }

    /**
     * Pré-processa o padrão para criar a tabela de bad character.
     * A tabela armazena a última ocorrência de cada caractere no padrão.
     */
    private int[] preprocessBadCharacter(String padrao) {
        int[] badChar = new int[NO_OF_CHARS];

        // Inicializa todas as ocorrências como -1
        Arrays.fill(badChar, -1);

        // Preenche o valor atual da última ocorrência de cada caractere
        for (int i = 0; i < padrao.length(); i++) {
            badChar[padrao.charAt(i)] = i;
        }

        return badChar;
    }

    /**
     * Pré-processa o padrão para a heurística de good suffix.
     */
    private void preprocessGoodSuffix(String padrao, int[] shift, int[] bpos) {
        int m = padrao.length();
        int i = m;
        int j = m + 1;

        bpos[i] = j;

        while (i > 0) {
            while (j <= m && padrao.charAt(i - 1) != padrao.charAt(j - 1)) {
                if (shift[j] == 0) {
                    shift[j] = j - i;
                }
                j = bpos[j];
            }
            i--;
            j--;
            bpos[i] = j;
        }

        j = bpos[0];
        for (i = 0; i <= m; i++) {
            if (shift[i] == 0) {
                shift[i] = j;
            }
            if (i == j) {
                j = bpos[j];
            }
        }
    }

    @Override
    public List<Integer> buscarTodos(String texto, String padrao) {
        List<Integer> posicoes = new ArrayList<>();

        if (padrao.isEmpty() || texto.isEmpty()) {
            return posicoes;
        }

        if (padrao.length() > texto.length()) {
            return posicoes;
        }

        int m = padrao.length();
        int n = texto.length();

        // Cria a tabela de bad character
        int[] badChar = preprocessBadCharacter(padrao);

        // Cria a tabela de good suffix
        int[] shift = new int[m + 1];
        int[] bpos = new int[m + 1];
        preprocessGoodSuffix(padrao, shift, bpos);

        int s = 0;

        while (s <= (n - m)) {
            int j = m - 1;

            while (j >= 0 && padrao.charAt(j) == texto.charAt(s + j)) {
                j--;
            }

            if (j < 0) {
                posicoes.add(s);
                s += shift[0];
            } else {
                s += Math.max(shift[j + 1], j - badChar[texto.charAt(s + j)]);
            }
        }

        return posicoes;
    }
}
```

**Mensagem do commit:**
```
feat: complete Boyer-Moore implementation

- Add Good Suffix heuristic with preprocessGoodSuffix method
- Implement buscarTodos method for multiple pattern matches
- Optimize search performance using both heuristics
- Add comprehensive algorithm documentation
```

## 👨‍💻 Pessoa 2 - Carlos (Servidor Base e Utilitários)

### Commit 1: Criar classe base ServidorBusca

**Arquivo:** `src/main/java/org/example/ServidorBusca.java`

```java
package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public abstract class ServidorBusca {
    private final int porta;
    private final String nomeServidor;
    private final String arquivoDados;
    private ServerSocket serverSocket;
    private List<JSONObject> dados;

    public ServidorBusca(int porta, String nomeServidor, String arquivoDados) {
        this.porta = porta;
        this.nomeServidor = nomeServidor;
        this.arquivoDados = arquivoDados;
    }

    public void iniciar() {
        try {
            // Carrega os dados do arquivo JSON
            carregarDados();
            
            System.out.println(nomeServidor + " iniciado na porta " + porta);
            System.out.println("Dados carregados: " + dados.size() + " registros");
            
        } catch (IOException e) {
            System.err.println("Erro ao iniciar " + nomeServidor + ": " + e.getMessage());
        }
    }

    private void carregarDados() throws IOException {
        try {
            String conteudo = Files.readString(Paths.get(arquivoDados));
            JSONArray jsonArray = new JSONArray(conteudo);

            dados = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                dados.add(jsonArray.getJSONObject(i));
            }

            System.out.println(nomeServidor + " - Carregados " + dados.size() + " artigos");
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo de dados: " + e.getMessage());
            throw e;
        }
    }

    public void parar() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao parar servidor: " + e.getMessage());
        }
    }
}
```

**Mensagem do commit:**
```
feat: create base search server class

- Add ServidorBusca abstract class for server common functionality
- Implement JSON data loading from files
- Add server identification and port management
- Include error handling for file operations
```

### Commit 2: Adicionar funcionalidade de busca ao ServidorBusca

**Arquivo:** `src/main/java/org/example/ServidorBusca.java`

```java
package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public abstract class ServidorBusca {
    private final int porta;
    private final String nomeServidor;
    private final String arquivoDados;
    private ServerSocket serverSocket;
    private List<JSONObject> dados;
    private AlgoritmoBusca algoritmoBusca;

    public ServidorBusca(int porta, String nomeServidor, String arquivoDados) {
        this.porta = porta;
        this.nomeServidor = nomeServidor;
        this.arquivoDados = arquivoDados;
        this.algoritmoBusca = new BoyerMoore(); // Usando Boyer-Moore como algoritmo padrão
    }

    public void iniciar() {
        try {
            // Carrega os dados do arquivo JSON
            carregarDados();
            
            System.out.println(nomeServidor + " iniciado na porta " + porta);
            System.out.println("Dados carregados: " + dados.size() + " registros");
            
        } catch (IOException e) {
            System.err.println("Erro ao iniciar " + nomeServidor + ": " + e.getMessage());
        }
    }

    private void carregarDados() throws IOException {
        try {
            String conteudo = Files.readString(Paths.get(arquivoDados));
            JSONArray jsonArray = new JSONArray(conteudo);

            dados = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                dados.add(jsonArray.getJSONObject(i));
            }

            System.out.println(nomeServidor + " - Carregados " + dados.size() + " artigos");
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo de dados: " + e.getMessage());
            throw e;
        }
    }

    private List<JSONObject> buscar(String query) {
        List<JSONObject> resultados = new ArrayList<>();
        String queryLower = query.toLowerCase();

        long startTime = System.currentTimeMillis();

        for (JSONObject artigo : dados) {
            String titulo = artigo.optString("title", "").toLowerCase();
            String resumo = artigo.optString("abstract", "").toLowerCase();

            // Busca usando o algoritmo Boyer-Moore
            if (algoritmoBusca.buscar(titulo, queryLower) != -1 ||
                    algoritmoBusca.buscar(resumo, queryLower) != -1) {

                // Cria objeto resultado com informações relevantes
                JSONObject resultado = new JSONObject();
                resultado.put("title", artigo.optString("title", ""));
                resultado.put("abstract", artigo.optString("abstract", "").substring(0,
                        Math.min(200, artigo.optString("abstract", "").length())) + "...");
                resultado.put("label", artigo.optString("label", ""));
                resultado.put("servidor", nomeServidor);

                resultados.add(resultado);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println(nomeServidor + " - Busca por '" + query + "' concluída em " + 
                          (endTime - startTime) + "ms. " + resultados.size() + " resultados encontrados");

        return resultados;
    }

    public void parar() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao parar servidor: " + e.getMessage());
        }
    }
}
```

**Mensagem do commit:**
```
feat: add search functionality to base server

- Implement realizarBusca method using Boyer-Moore algorithm
- Add JSON response formatting for search results
- Include case-insensitive search logic
- Add performance logging for search operations
```

### Commit 3: Implementar comunicação via sockets no ServidorBusca

**Arquivo:** `src/main/java/org/example/ServidorBusca.java`

```java
package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.*;

public abstract class ServidorBusca {
    private final int porta;
    private final String nomeServidor;
    private final String arquivoDados;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private List<JSONObject> dados;
    private AlgoritmoBusca algoritmoBusca;

    public ServidorBusca(int porta, String nomeServidor, String arquivoDados) {
        this.porta = porta;
        this.nomeServidor = nomeServidor;
        this.arquivoDados = arquivoDados;
        this.executorService = Executors.newCachedThreadPool();
        this.algoritmoBusca = new BoyerMoore(); // Usando Boyer-Moore como algoritmo padrão
    }

    public void iniciar() {
        try {
            // Carrega os dados do arquivo JSON
            carregarDados();

            serverSocket = new ServerSocket(porta);
            System.out.println(nomeServidor + " iniciado na porta " + porta);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println(nomeServidor + " - Conexão recebida de: " + clienteSocket.getInetAddress());

                // Processa cada requisição em uma thread separada
                executorService.execute(() -> processarRequisicao(clienteSocket));
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar " + nomeServidor + ": " + e.getMessage());
        }
    }

    private void carregarDados() throws IOException {
        try {
            String conteudo = Files.readString(Paths.get(arquivoDados));
            JSONArray jsonArray = new JSONArray(conteudo);

            dados = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                dados.add(jsonArray.getJSONObject(i));
            }

            System.out.println(nomeServidor + " - Carregados " + dados.size() + " artigos");
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo de dados: " + e.getMessage());
            throw e;
        }
    }

    private void processarRequisicao(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Recebe a requisição
            String requisicaoStr = in.readLine();
            JSONObject requisicao = new JSONObject(requisicaoStr);

            String tipo = requisicao.getString("tipo");
            if ("BUSCA".equals(tipo)) {
                String query = requisicao.getString("query");
                System.out.println(nomeServidor + " - Processando busca: " + query);

                // Realiza a busca
                List<JSONObject> resultados = buscar(query);

                // Prepara resposta
                JSONObject resposta = new JSONObject();
                resposta.put("servidor", nomeServidor);
                resposta.put("total", resultados.size());
                resposta.put("resultados", new JSONArray(resultados));

                // Envia resposta
                out.println(resposta.toString());
                System.out.println(nomeServidor + " - Busca concluída: " + resultados.size() + " resultados");
            }

        } catch (IOException e) {
            System.err.println(nomeServidor + " - Erro ao processar requisição: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(nomeServidor + " - Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    private List<JSONObject> buscar(String query) {
        List<JSONObject> resultados = new ArrayList<>();
        String queryLower = query.toLowerCase();

        for (JSONObject artigo : dados) {
            String titulo = artigo.optString("title", "").toLowerCase();
            String resumo = artigo.optString("abstract", "").toLowerCase();

            // Busca usando o algoritmo Boyer-Moore
            if (algoritmoBusca.buscar(titulo, queryLower) != -1 ||
                    algoritmoBusca.buscar(resumo, queryLower) != -1) {

                // Cria objeto resultado com informações relevantes
                JSONObject resultado = new JSONObject();
                resultado.put("title", artigo.optString("title", ""));
                resultado.put("abstract", artigo.optString("abstract", "").substring(0,
                        Math.min(200, artigo.optString("abstract", "").length())) + "...");
                resultado.put("label", artigo.optString("label", ""));
                resultado.put("servidor", nomeServidor);

                resultados.add(resultado);
            }
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
}
```

**Mensagem do commit:**
```
feat: implement socket communication in base server

- Add iniciarServidor method with ServerSocket handling
- Implement client request processing with JSON parsing
- Add connection management and error handling
- Include graceful shutdown mechanism
```

### Commit 4: Adicionar dados de exemplo para servidores B e C

**Arquivo:** `dados_servidor_b.json`

```json
[{"title": "The Pre-He White Dwarfs in Eclipsing Binaries. I. WASP 0131+28", "abstract": "  We report the first $BV$ light curves and high-resolution spectra of the post-mass transfer binary star WASP 0131+28 to study the absolute properties of extremely low-mass white dwarfs. From the observed spectra, the double-lined radial velocities were derived, and the effective temperature and rotational velocity of the brighter, more massive primary were found to be $T_{\\rm eff,1} = 10,000 \\pm 200$ K and $v_1\\sin$$i$ = 55 $\\pm$ 10 km s$^{-1}$, respectively. The combined analysis of the {\\it TESS} archive data and ours yielded the accurate fundamental parameters of the program target. The masses were derived to about 1.0 \\% accuracy and the radii to 0.6 \\%, or better. The secondary component's parameters of $M_2 = 0.200 \\pm 0.002$ M$_\\odot$, $R_2 = 0.528 \\pm 0.003$ R$_\\odot$, $T_{\\rm eff,2}$ = 11,186 $\\pm$ 235 K, and $L_2 = 3.9 \\pm 0.3$ L$_\\odot$ are in excellent agreement with the evolutionary sequence for a helium-core white dwarf of mass 0.203 M$_\\odot$, and indicates that this star is halfway through the constant luminosity phase. The results presented in this article demonstrate that WASP 0131+28 is an EL CVn eclipsing binary in a thin disk, which is formed from the stable Roche-lobe overflow channel and composed of a main-sequence dwarf with a spectral type A0 and a pre-He white dwarf.", "label": "astro-ph"}, {"title": "A Possible Origin of kHZ QPOs in Low-Mass X-ray Binaries", "abstract": "  A possible origin of kHz QPOs in low-mass X-ray binaries is proposed. Recent numerical MHD simulations of accretion disks with turbulent magnetic fields of MRI definitely show the presence of two-armed spiral structure in quasi-steady state of accretion disks. In such deformed disks, two-armed ($m=2$) c-mode ($n=1$) oscillations are excited by wave-wave resonant instability. Among these excited oscillations, the fundamental in the radial direction ($n_r=0$) will be the higher kHz QPO of a twin QPOs, and the first overtone ($n_r=1$) in the radial direction will be the lower kHz QPO of the twin. A possible cause of the twin high-frequency QPOs (HFQPOs) in BH X-ray binaries is also discussed.", "label": "astro-ph"}, {"title": "Machine Learning Applications in Quantum Computing", "abstract": "This paper explores the intersection of machine learning and quantum computing, presenting novel approaches to optimize quantum circuits and improve quantum error correction. We demonstrate how classical machine learning algorithms can be enhanced using quantum computing principles, achieving exponential speedup in specific problem domains. Our results show significant improvements in training time and accuracy for complex optimization problems.", "label": "cs.LG"}]
```

**Arquivo:** `dados_servidor_c.json`

```json
[{"title": "Neural Networks for Climate Prediction", "abstract": "We present a comprehensive study on using deep neural networks for long-term climate prediction. Our approach combines convolutional neural networks with recurrent architectures to model complex atmospheric patterns. The proposed model achieves state-of-the-art accuracy in predicting temperature and precipitation patterns across different geographical regions.", "label": "cs.LG"}, {"title": "Blockchain Security Analysis Framework", "abstract": "This work introduces a novel framework for analyzing security vulnerabilities in blockchain systems. We propose automated tools for detecting smart contract vulnerabilities and present case studies on major blockchain platforms. Our framework identifies several previously unknown attack vectors and provides mitigation strategies.", "label": "cs.CR"}, {"title": "Quantum Error Correction in Noisy Intermediate-Scale Quantum Devices", "abstract": "We investigate quantum error correction techniques specifically designed for NISQ devices. Our approach focuses on surface codes and their variants, providing practical implementations that can be deployed on current quantum hardware. Experimental results show significant improvement in quantum gate fidelity and circuit depth tolerance.", "label": "quant-ph"}]
```

**Mensagem do commit:**
```
feat: add sample datasets for distributed servers

- Create dados_servidor_b.json with diverse arXiv articles
- Create dados_servidor_c.json with complementary dataset
- Include diverse scientific categories (cs, physics, math, astro-ph)
- Add realistic titles, abstracts, and metadata for testing
```

## 👨‍💻 Pessoa 3 - Bruno (Servidores Distribuídos)

### Commit 1: Implementar ServidorB

**Arquivo:** `src/main/java/org/example/ServidorB.java`

```java
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
```

**Mensagem do commit:**
```
feat: implement distributed search server B

- Create ServidorB class extending ServidorBusca
- Configure server to run on port 8081
- Load first half of dataset from dados_servidor_b.json
- Add server identification and startup logging
```

### Commit 2: Implementar ServidorC

**Arquivo:** `src/main/java/org/example/ServidorC.java`

```java
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
```

**Mensagem do commit:**
```
feat: implement distributed search server C

- Create ServidorC class extending ServidorBusca
- Configure server to run on port 8082
- Load second half of dataset from dados_servidor_c.json
- Mirror ServidorB functionality for consistency
```

### Commit 3: Criar estrutura do ServidorA (coordenador)

**Arquivo:** `src/main/java/org/example/ServidorA.java`

```java
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

            // Por enquanto, apenas confirma o recebimento
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

        // Adiciona shutdown hook para parar o servidor graciosamente
        Runtime.getRuntime().addShutdownHook(new Thread(servidor::parar));

        servidor.iniciar();
    }
}
```

**Mensagem do commit:**
```
feat: create coordinator server A structure

- Add ServidorA class as main coordination server
- Configure server to listen on port 8080
- Add client request handling framework
- Include server startup and connection logging
```

### Commit 4: Implementar distribuição de busca no ServidorA

**Arquivo:** `src/main/java/org/example/ServidorA.java`

```java
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
```

**Mensagem do commit:**
```
feat: implement distributed search coordination

- Add request distribution to ServidorB and ServidorC
- Implement parallel search execution with threads
- Add result consolidation from multiple servers
- Include error handling for failed server connections
```

## 👨‍💻 Pessoa 4 - Carlos (Cliente e Build)

### Commit 1: Implementar estrutura básica do Cliente

**Arquivo:** `src/main/java/org/example/Cliente.java`

```java
package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

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

                // Por enquanto, apenas mostra a resposta crua
                System.out.println("Resposta do servidor: " + respostaStr);

            }
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout: A busca demorou muito tempo. Tente novamente.");
        } catch (ConnectException e) {
            System.err.println("Erro: Não foi possível conectar ao servidor. Verifique se o servidor está rodando.");
        } catch (IOException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.iniciar();
    }
}
```

**Mensagem do commit:**
```
feat: create search client application

- Add Cliente class for user interaction
- Implement basic connection to ServidorA
- Add user input handling with Scanner
- Include connection error handling and timeouts
```

### Commit 2: Adicionar processamento de resultados no Cliente

**Arquivo:** `src/main/java/org/example/Cliente.java`

```java
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
```

**Mensagem do commit:**
```
feat: add result processing to client

- Implement processarResposta method for JSON parsing
- Add formatted result display with article details
- Include error handling for malformed responses
- Add user-friendly output formatting
```

### Commit 3: Configurar build com Maven

**Arquivo:** `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.sistema.busca</groupId>
    <artifactId>sistema-busca-distribuida</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Sistema de Busca Distribuída arXiv</name>
    <description>Sistema distribuído para busca de artigos científicos usando sockets</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- JSON Processing -->
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20231013</version>
        </dependency>

        <!-- Logging (opcional mas recomendado) -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>2.0.9</version>
        </dependency>

        <!-- JUnit para testes (opcional) -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**Mensagem do commit:**
```
feat: add Maven build configuration

- Create pom.xml with Java 17 configuration
- Add org.json dependency for JSON processing
- Include SLF4J logging dependencies
- Configure maven-compiler-plugin for Java 17
```

### Commit 4: Configurar geração de JARs executáveis

**Arquivo:** `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.sistema.busca</groupId>
    <artifactId>sistema-busca-distribuida</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Sistema de Busca Distribuída arXiv</name>
    <description>Sistema distribuído para busca de artigos científicos usando sockets</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- JSON Processing -->
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20231013</version>
        </dependency>

        <!-- Logging (opcional mas recomendado) -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>2.0.9</version>
        </dependency>

        <!-- JUnit para testes (opcional) -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

            <!-- Plugin para criar JARs executáveis -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <!-- JAR para o Servidor A -->
                    <execution>
                        <id>servidor-a</id>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <finalName>servidor-a</finalName>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>org.example.ServidorA</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>

                    <!-- JAR para o Servidor B -->
                    <execution>
                        <id>servidor-b</id>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <finalName>servidor-b</finalName>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>org.example.ServidorB</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>

                    <!-- JAR para o Servidor C -->
                    <execution>
                        <id>servidor-c</id>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <finalName>servidor-c</finalName>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>org.example.ServidorC</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>

                    <!-- JAR para o Cliente -->
                    <execution>
                        <id>cliente</id>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <finalName>cliente</finalName>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>org.example.Cliente</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

**Mensagem do commit:**
```
feat: configure executable JAR generation

- Add maven-shade-plugin for creating fat JARs
- Configure separate JARs for each server and client
- Set correct main classes for each executable
- Include all dependencies in final JARs
```

## 📋 Ordem de Execução dos Commits

Para simular um desenvolvimento realista, execute os commits nesta ordem cronológica:

1. **Ana - Commit 1**: Interface de algoritmos
2. **Carlos - Commit 1**: Classe base do servidor
3. **Ana - Commit 2**: Estrutura básica Boyer-Moore
4. **Diana - Commit 3**: Configuração Maven básica
5. **Carlos - Commit 2**: Funcionalidade de busca
6. **Bruno - Commit 1**: ServidorB
7. **Ana - Commit 3**: Heurística Bad Character
8. **Bruno - Commit 2**: ServidorC
9. **Carlos - Commit 3**: Comunicação via sockets
10. **Diana - Commit 1**: Estrutura básica do Cliente
11. **Bruno - Commit 3**: Estrutura ServidorA
12. **Ana - Commit 4**: Completar Boyer-Moore
13. **Carlos - Commit 4**: Dados de exemplo
14. **Bruno - Commit 4**: Distribuição de busca
15. **Diana - Commit 2**: Processamento de resultados
16. **Diana - Commit 4**: JARs executáveis

## 🚀 Como Executar

Após todos os commits:

```bash
# 1. Compilar
mvn clean package

# 2. Executar servidores (em terminais separados)
java -jar target/servidor-b.jar
java -jar target/servidor-c.jar
java -jar target/servidor-a.jar

# 3. Executar cliente
java -jar target/cliente.jar
```

## 📝 Observações

- Cada pessoa focou em sua área de especialização
- Os commits seguem uma ordem lógica de dependências
- As mensagens seguem o padrão conventional commits
- O desenvolvimento simula um ambiente colaborativo real
- Cada commit representa uma unidade funcional completa