package com.exemplo;

public class CalculadoraDesconto {

    public double calcularValorFinal(double valorCompra, double percentualDesconto) {
        if (valorCompra <= 0) {
            throw new IllegalArgumentException("valorCompra deve ser maior que zero");
        }
        if (percentualDesconto < 0 || percentualDesconto > 50) {
            throw new IllegalArgumentException("percentualDesconto deve estar entre 0 e 50");
        }

        double valorFinal = valorCompra - (valorCompra * (percentualDesconto / 100.0));
        return Math.max(0.0, valorFinal);
    }
}