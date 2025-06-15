package org.example;


public interface AlgoritmoBusca {

    int buscar(String texto, String padrao);

    List<Integer> buscarTodos(String texto, String padrao);
}