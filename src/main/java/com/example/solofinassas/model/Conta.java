package com.example.solofinassas.model;

public class Conta {
    private int id;
    private String nome;
    private double saldo;
    private String banco;

    public Conta(int id, String nome, double saldo, String banco) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;
        this.banco = banco;
    }

    // Getters e Setters para cumprir o Encapsulamento
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }

    // Regras de negócio pedidas no teu diagrama do draw.io
    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    public void sacar(double valor) {
        if (valor > 0) {
            this.saldo -= valor;
        }
    }
}