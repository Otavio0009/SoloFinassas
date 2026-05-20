package com.example.solofinassas.model;

import java.time.LocalDate;

public abstract class Movimentacao {
    private int id;
    private String descricao;
    private double valor;
    private LocalDate data;
    private String tag;
    private String bancoMetodo;

    public Movimentacao(int id, String descricao, double valor, LocalDate data, String tag, String bancoMetodo) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.tag = tag;
        this.bancoMetodo = bancoMetodo;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public LocalDate getData() { return data; }
    public String getTag() { return tag; }
    public String getBancoMetodo() { return bancoMetodo; }

    public abstract String getTipo();
}