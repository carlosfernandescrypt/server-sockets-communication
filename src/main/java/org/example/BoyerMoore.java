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
