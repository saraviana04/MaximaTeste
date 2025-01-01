package com.testemaxima.maxima.dto;

public class TransferenciaRequest {
    private String contaOrigem;
    private String contaDestino;
    private double valor;

    public String getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(String contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    @Override
    public String toString() {
        return "TransferenciaRequest{" +
                "contaOrigem='" + contaOrigem + '\'' +
                ", contaDestino='" + contaDestino + '\'' +
                ", valor=" + valor +
                '}';
    }
}
