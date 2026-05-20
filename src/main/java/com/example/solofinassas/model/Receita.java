package com.example.solofinassas.model;

import java.time.LocalDate;

public class Receita extends Movimentacao {
    private String origem;

    public Receita(int id, String descricao, double valor, LocalDate data, String tag, String bancoMetodo, String origem) {
        super(id, descricao, valor, data, tag, bancoMetodo);
        this.origem = origem;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    @Override
    public String getTipo() {
        return "Entrada";
    }
}