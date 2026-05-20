package com.example.solofinassas.model;

import java.time.LocalDate;

public class Despesa extends Movimentacao {
    private String categoria;

    public Despesa(int id, String descricao, double valor, LocalDate data, String tag, String bancoMetodo, String categoria) {
        super(id, descricao, valor, data, tag, bancoMetodo);
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String getTipo() {
        return "Saída";
    }
}