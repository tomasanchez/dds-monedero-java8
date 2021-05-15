package dds.monedero.model;

import java.time.LocalDate;

public abstract class Movimiento {
  private LocalDate fecha;
  // En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  // siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
  private double monto;

  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }

  public abstract double getSaldo();

  public LocalDate getFecha() {
    return fecha;
  }

  public Movimiento setFecha(LocalDate fecha) {
    this.fecha = fecha;
    return this;
  }

  public Movimiento setMonto(double monto) {
    this.monto = monto;
    return this;
  }

  public abstract boolean fueDepositado(LocalDate fecha);

  public abstract boolean fueExtraido(LocalDate fecha);

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

}
