package com.exemplo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculadoraDescontoTest {

    static final String CLASS_NAME = "com.exemplo.CalculadoraDesconto";
    static final double DELTA = 0.0001;

    @ParameterizedTest(name = "valor={0}, desconto=0 -> final={1}")
    @CsvSource({
        "100.0, 100.0",
        "250.0, 250.0"
    })
    void deveRetornarMesmoValor_quandoCompraSemDesconto(double valorCompra, double valorEsperado) {
        double valorFinal = calcularValorFinal(valorCompra, 0.0);

        assertEquals(valorEsperado, valorFinal, DELTA);
    }

    @ParameterizedTest(name = "valor={0}, desconto={1}% -> final={2}")
    @CsvSource({
        "200.0, 10.0, 180.0",
        "200.0, 25.0, 150.0",
        "200.0, 50.0, 100.0"
    })
    void deveAplicarDescontoCorretamente_quandoDescontoValido(
            double valorCompra,
            double percentualDesconto,
            double valorEsperado) {
        double valorFinal = calcularValorFinal(valorCompra, percentualDesconto);

        assertEquals(valorEsperado, valorFinal, DELTA);
    }

    @ParameterizedTest(name = "deve falhar para desconto invalido > 50: {1}")
    @CsvSource({
        "100.0, 50.1",
        "100.0, 51.0",
        "100.0, 80.0"
    })
    void deveLancarErro_quandoDescontoMaiorQueCinquenta(double valorCompra, double percentualDesconto) {
        assertArgumentoInvalido(valorCompra, percentualDesconto);
    }

    @ParameterizedTest(name = "deve falhar para valorCompra invalido: {0}")
    @CsvSource({
        "0.0",
        "-1.0",
        "-100.0"
    })
    void deveLancarErro_quandoValorCompraInvalido(double valorCompra) {
        assertArgumentoInvalido(valorCompra, 10.0);
    }

    @ParameterizedTest(name = "deve falhar para desconto invalido < 0: {1}")
    @CsvSource({
        "100.0, -0.1",
        "100.0, -10.0",
        "100.0, -50.0"
    })
    void deveLancarErro_quandoDescontoNegativo(double valorCompra, double percentualDesconto) {
        assertArgumentoInvalido(valorCompra, percentualDesconto);
    }

    void assertArgumentoInvalido(double valorCompra, double percentualDesconto) {
        assertThrows(IllegalArgumentException.class,
                () -> calcularValorFinal(valorCompra, percentualDesconto));
    }

    double calcularValorFinal(double valorCompra, double percentualDesconto) {
        try {
            Class<?> clazz = Class.forName(CLASS_NAME);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Method method = obterMetodoCalcularValorFinal(clazz);
            Object result = method.invoke(instance, valorCompra, percentualDesconto);
            return (double) result;
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new RuntimeException("Erro ao invocar calcularValorFinal", cause);
        } catch (ClassNotFoundException ex) {
            throw new AssertionError("Classe de producao nao encontrada: " + CLASS_NAME, ex);
        } catch (NoSuchMethodException ex) {
            throw new AssertionError("Metodo calcularValorFinal(double,double) nao encontrado", ex);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("Falha de reflexao ao instanciar a calculadora", ex);
        }
    }

    Method obterMetodoCalcularValorFinal(Class<?> clazz) throws NoSuchMethodException {
        return clazz.getDeclaredMethod("calcularValorFinal", double.class, double.class);
    }
}