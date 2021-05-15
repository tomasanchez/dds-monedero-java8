package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double limite = 1000;
  private int maximosDepositos = 3;
  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  /**
   * Verfica si se supera el monto de extaccion diaria.
   * 
   * @param cuanto el monto
   * @param limite el limite de extaccion
   * @throws MaximoExtraccionDiarioException si supera el límite
   */
  private void verificarExtraccionDiaria(double cuanto) {
    double limite = getLimiteActual();

    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException(
          "No puede extraer mas de $ " + this.limite + " diarios, límite: " + limite);
    }
  }

  /**
   * Valida si puede realizarse la extaccion.
   * 
   * @param cuanto el monto
   * @throws SaldoMenorException cuando se desea sacar mas que el el saldo.
   */
  private void verificarSaldoMenor(double cuanto) {
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  /**
   * Valia si se encuentra dentro de la Maxima cantidad de depósitos.
   * 
   * @throws MaximaCantidadDepositosException cuando excede cierto numeros.
   */
  private void verificarMaximaCantidadDepositos() {
    if (contarDepositos() >= this.maximosDepositos) {
      throw new MaximaCantidadDepositosException(
          "Ya excedio los " + this.maximosDepositos + " depositos diarios");
    }
  }

  /**
   * Verifica si el monto es negativo.
   * 
   * @param cuanto el monto a verificar
   * @throws MontoNegativoException si el monto es menor a 0.
   */
  private void verificarMontoNegativo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public Cuenta() {
    this(0);
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  /**
   * Cuenta la cantidad de movimientos que son depositos.
   * 
   * @return la cantidad de depositos.
   */
  public long contarDepositos() {
    return getMovimientos().stream().filter(Movimiento::isDeposito).count();
  }

  private void realizarOperacion(double cuanto, boolean isDeposito, Runnable verificacion) {
    verificarMontoNegativo(cuanto);
    verificacion.run();
    agregarMovimiento(LocalDate.now(), cuanto, isDeposito);
  }

  public void poner(double cuanto) {

    realizarOperacion(cuanto, true, () -> verificarMaximaCantidadDepositos());
  }

  public void sacar(double cuanto) {

    realizarOperacion(cuanto, false, () -> {
      verificarSaldoMenor(cuanto);
      verificarExtraccionDiaria(cuanto);
    });

  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
    this.saldo += movimiento.isDeposito() ? movimiento.getMonto() : -movimiento.getMonto();
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto).sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  /**
   * Obtiene el límite de extaccion restante
   * 
   * @return el limite actual
   */
  public double getLimiteActual() {
    return this.limite - getMontoExtraidoA(LocalDate.now());
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
