# Sistema de Busca Distribuída arXiv

Sistema distribuído para busca de artigos científicos do arXiv utilizando sockets em Java 17.

## Arquitetura do Sistema

O sistema é composto por três servidores que trabalham de forma distribuída:

- **Servidor A (Coordenador)**: Recebe requisições dos clientes, distribui para os servidores B e C, e consolida os resultados
- **Servidor B**: Responsável pela busca na primeira metade do dataset
- **Servidor C**: Responsável pela busca na segunda metade do dataset

### Diagrama de Comunicação

```
┌─────────┐
│ Cliente │
└────┬────┘
     │ Busca: "machine learning"
     ▼
┌─────────────┐
│ Servidor A  │ (Porta 8080)
│(Coordenador)│
└──┬──────┬───┘
   │      │ Distribui busca
   ▼      ▼
┌─────┐ ┌─────┐
│Serv.│ │Serv.│
│  B  │ │  C  │
└──┬──┘ └──┬──┘
   │       │ Resultados parciais
   └───┬───┘
       ▼
  Consolidação
       │
       ▼
   Resposta ao Cliente
```

## Algoritmo de Busca

O sistema utiliza o algoritmo **Boyer-Moore** para busca de substrings. Este algoritmo foi escolhido por:

- **Eficiência**: Especialmente eficiente para padrões longos
- **Complexidade**: O(n + m) no melhor caso
- **Heurísticas**: Usa Bad Character Rule e Good Suffix Rule para pular comparações desnecessárias

## Formato de Dados

### Comunicação Cliente → Servidor A
```
"termo de busca"
```

### Comunicação Servidor A → Servidores B/C
```json
{
  "tipo": "BUSCA",
  "query": "termo de busca"
}
```

### Resposta dos Servidores B/C → Servidor A
```json
{
  "servidor": "Servidor B",
  "total": 2,
  "resultados": [
    {
      "title": "Título do artigo",
      "abstract": "Resumo...",
      "label": "categoria",
      "servidor": "Servidor B"
    }
  ]
}
```

### Resposta do Servidor A → Cliente
```json
{
  "total": 5,
  "resultados": [...]
}
```

## Requisitos

- Java 17
- Maven 3.6+
- Biblioteca org.json

## Estrutura do Projeto

```
.
├── src/main/java/
│   ├── ServidorA.java       # Servidor coordenador
│   ├── ServidorBusca.java   # Classe base para servidores B e C
│   ├── ServidorB.java       # Servidor de busca B
│   ├── ServidorC.java       # Servidor de busca C
│   ├── Cliente.java         # Cliente do sistema
│   ├── AlgoritmoBusca.java  # Interface para algoritmos
│   └── BoyerMoore.java      # Implementação Boyer-Moore
├── dados_servidor_b.json    # Dataset do servidor B
├── dados_servidor_c.json    # Dataset do servidor C
├── pom.xml                  # Configuração Maven
└── README.md
```

## Como Executar

### 1. Compilar o Projeto

```bash
mvn clean package
```

Isso criará 4 JARs executáveis na pasta `target/`:
- `servidor-a.jar`
- `servidor-b.jar`
- `servidor-c.jar`
- `cliente.jar`

### 2. Iniciar os Servidores

**Importante**: Inicie os servidores B e C antes do servidor A.

Em terminais separados:

```bash
# Terminal 1 - Servidor B
java -jar target/servidor-b.jar

# Terminal 2 - Servidor C
java -jar target/servidor-c.jar

# Terminal 3 - Servidor A
java -jar target/servidor-a.jar
```

### 3. Executar o Cliente

```bash
java -jar target/cliente.jar
```

## Exemplos de Uso

```
=== Cliente de Busca Distribuída arXiv ===
Digite 'sair' para encerrar o programa

Digite o termo de busca: machine learning
Conectando ao servidor...
Enviando busca: "machine learning"
Aguardando resultados...

========== RESULTADOS DA BUSCA ==========
Total de resultados encontrados: 3

--- Resultado 1 ---
Título: Deep Learning for Computer Vision
Categoria: cs.CV
Resumo: This paper presents a new approach to...
Servidor: Servidor B

--- Resultado 2 ---
Título: Machine Learning in Healthcare
Categoria: cs.LG
Resumo: We propose a novel method for...
Servidor: Servidor C
========================================
```

## Vantagens da Arquitetura Distribuída

1. **Escalabilidade**: Possibilidade de adicionar mais servidores de busca
2. **Tolerância a Falhas**: Se um servidor de busca falhar, o sistema ainda retorna resultados parciais
3. **Paralelismo**: Buscas executadas simultaneamente nos servidores B e C
4. **Balanceamento de Carga**: Dataset dividido entre múltiplos servidores

## Desvantagens

1. **Complexidade**: Maior complexidade de implementação e manutenção
2. **Latência de Rede**: Comunicação entre servidores adiciona overhead
3. **Ponto Único de Falha**: Servidor A é crítico para o funcionamento
4. **Sincronização**: Necessidade de garantir consistência entre servidores

## Melhorias Futuras

- Implementar cache de resultados
- Adicionar balanceamento de carga dinâmico
- Implementar replicação para alta disponibilidade
- Adicionar compressão na comunicação
- Implementar busca com expressões regulares
- Adicionar ranking de relevância nos resultados

## Referências

- Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.
- Boyer, R. S., & Moore, J. S. (1977). A fast string searching algorithm. *Communications of the ACM*, 20(10), 762-772.
- Tanenbaum, A. S., & Van Steen, M. (2017). *Distributed Systems: Principles and Paradigms* (3rd ed.). Pearson.