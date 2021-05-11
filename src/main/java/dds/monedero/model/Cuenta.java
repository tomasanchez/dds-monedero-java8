package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  /**
   * Verfica si se supera el monto de extaccion diaria.
   * 
   * @param cuanto el monto
   * @param limite el limite de extaccion
   * @throws MaximoExtraccionDiarioException si supera el límite
   */
  private void verificarExtraccionDiaria(double cuanto, double limite) {
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException(
          "No puede extraer mas de $ " + 1000 + " diarios, límite: " + limite);
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
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
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

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {

    verificarMontoNegativo(cuanto);

    verificarMaximaCantidadDepositos();

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
  }

  public void sacar(double cuanto) {

    verificarMontoNegativo(cuanto);

    verificarSaldoMenor(cuanto);

    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;

    verificarExtraccionDiaria(cuanto, limite);

    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto).sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
