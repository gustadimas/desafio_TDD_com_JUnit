package com.exemplo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculadoraDescontoTest {

    static final double DELTA = 0.0001;

    CalculadoraDesconto calculadora = new CalculadoraDesconto();

    @ParameterizedTest(name = "valor={0}, desconto=0 -> final={1}")
    @CsvSource({
        "100.0, 100.0",
        "250.0, 250.0"
    })
    void deveRetornarMesmoValor_quandoCompraSemDesconto(double valorCompra, double valorEsperado) {
        double valorFinal = calculadora.calcularValorFinal(valorCompra, 0.0);

        assertEquals(valorEsperado, valorFinal, DELTA);
    }

    @ParameterizedTest(name = "valor={0}, desconto={1}% -> final={2}")
    @CsvSource({
        "200.0, 10.0, 180.0",
        "200.0, 25.0, 150.0",
        "200.0, 50.0, 100.0"
    })
    void deveAplicarDescontoCorretamente_quandoPercentualDescontoForValido(
            double valorCompra,
            double percentualDesconto,
            double valorEsperado) {
        double valorFinal = calculadora.calcularValorFinal(valorCompra, percentualDesconto);

        assertEquals(valorEsperado, valorFinal, DELTA);
    }

    @Test
    void deveRetornarMetadeDoValor_quandoPercentualDescontoForCinquenta() {
        double valorFinal = calculadora.calcularValorFinal(100.0, 50.0);

        assertEquals(50.0, valorFinal, DELTA);
    }

    @ParameterizedTest(name = "deve falhar para desconto invalido > 50: {1}")
    @CsvSource({
        "100.0, 50.1",
        "100.0, 51.0",
        "100.0, 80.0"
    })
    void deveLancarErro_quandoDescontoForMaiorQueCinquenta(double valorCompra, double percentualDesconto) {
        assertArgumentoInvalido(valorCompra, percentualDesconto);
    }

    @Test
    void deveLancarErro_quandoPercentualDescontoForNegativoLimiteInferior() {
        assertArgumentoInvalido(100.0, -0.01);
    }

    @ParameterizedTest(name = "deve falhar para valorCompra invalido: {0}")
    @CsvSource({
        "0.0",
        "-1.0",
        "-100.0"
    })
    void deveLancarErro_quandoValorCompraForMenorOuIgualAZero(double valorCompra) {
        assertArgumentoInvalido(valorCompra, 10.0);
    }

    @ParameterizedTest(name = "deve falhar para desconto invalido < 0: {1}")
    @CsvSource({
        "100.0, -0.1",
        "100.0, -10.0",
        "100.0, -50.0"
    })
    void deveLancarErro_quandoPercentualDescontoForNegativo(double valorCompra, double percentualDesconto) {
        assertArgumentoInvalido(valorCompra, percentualDesconto);
    }

    @Test
    void deveCalcularValorFinalCorretamente_quandoValorCompraForMuitoGrande() {
        double valorFinal = calculadora.calcularValorFinal(1_000_000_000.0, 10.0);

        assertEquals(900_000_000.0, valorFinal, DELTA);
    }

    void assertArgumentoInvalido(double valorCompra, double percentualDesconto) {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularValorFinal(valorCompra, percentualDesconto));
    }
}