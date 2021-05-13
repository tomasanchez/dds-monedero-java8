package dds.monedero.model;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MovimientoTest {
    private Movimiento movimiento;


    @BeforeEach
    void init() {
        this.movimiento = new Movimiento(LocalDate.now(), 100, true);
    }

    @Test
    void getFecha() {
        assertEquals(LocalDate.now(), movimiento.getFecha());
    }

    @Test
    void esDeposito() {
        assertTrue(movimiento.isDeposito());
        movimiento.setEsDeposito(false);
        assertFalse(movimiento.isDeposito());
    }

    @Test
    void fueDepositadoHoyNoAyer() {
        assertTrue(movimiento.fueDepositado(LocalDate.now()));
        assertFalse(movimiento.fueDepositado(LocalDate.now().minusDays(1)));
    }

    @Test
    void noEsDepositoEsRetiro() {
        movimiento.setEsDeposito(false);
        assertTrue(movimiento.fueExtraido(LocalDate.now()));
        assertFalse(movimiento.fueDepositado(LocalDate.now()));
    }
}
