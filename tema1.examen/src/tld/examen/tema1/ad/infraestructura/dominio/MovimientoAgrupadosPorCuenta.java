package tld.examen.tema1.ad.infraestructura.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

public class MovimientoAgrupadosPorCuenta implements Serializable {

	private HashMap<String, SaldoCuenta> movimientosPorCuenta;
	private static final long serialVersionUID = 1L;

	public MovimientoAgrupadosPorCuenta() {
		super();
		movimientosPorCuenta = new HashMap<String, SaldoCuenta>();
	}

	public void añadirSaldoACuenta(String ccc, BigDecimal imputacion) {

		if (movimientosPorCuenta.containsKey(ccc))
			movimientosPorCuenta.get(ccc).inputarMovimiento(imputacion);
		else
			movimientosPorCuenta.put(ccc, new SaldoCuenta(ccc, imputacion));
	}
	
	public HashMap<String, SaldoCuenta> getCuentas(){
		return movimientosPorCuenta;
	}
}

