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