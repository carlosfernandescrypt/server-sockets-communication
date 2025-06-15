package org.example;

import java.util.*;

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
