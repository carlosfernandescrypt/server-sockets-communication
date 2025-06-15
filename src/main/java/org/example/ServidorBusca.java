package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.*;

public abstract class ServidorBusca {

    public ServidorBusca(int porta, String nomeServidor, String arquivoDados) {
    }

    public void iniciar() {

    }

    private void carregarDados() throws IOException {

    }

    private void processarRequisicao(Socket socket) {

    }

    private List<JSONObject> buscar(String query) {

    }

    public void parar() {

    }
}