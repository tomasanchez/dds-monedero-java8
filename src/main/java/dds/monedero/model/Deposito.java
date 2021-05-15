package dds.monedero.model;

import java.time.LocalDate;

public class Deposito extends Movimiento {

    public Deposito(LocalDate fecha, double monto) {
        super(fecha, monto);
    }

    @Override
    public boolean fueExtraido(LocalDate fecha) {
        return false;
    }

    @Override
    public boolean fueDepositado(LocalDate fecha) {
        return esDeLaFecha(fecha);
    }

    @Override
    public double getSaldo() {
        return this.getMonto();
    }
}
