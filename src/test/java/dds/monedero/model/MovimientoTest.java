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
        this.movimiento = new Deposito(LocalDate.now(), 100);
    }

    @Test
    void getFecha() {
        assertEquals(LocalDate.now(), movimiento.getFecha());
    }
}
