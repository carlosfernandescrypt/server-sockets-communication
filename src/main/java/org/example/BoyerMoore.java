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
