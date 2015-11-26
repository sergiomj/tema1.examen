package tld.examen.tema1.ad.infraestructura.dominio;

import java.math.BigDecimal;
import java.util.HashMap;

public class MovimientosPorAño {

	
	private HashMap<String, MovimientoAgrupadosPorCuenta> movimientosPorAño;
	
	public MovimientosPorAño() {
		super();
		this.movimientosPorAño = new HashMap<String, MovimientoAgrupadosPorCuenta>();
	}

	public HashMap<String, MovimientoAgrupadosPorCuenta> getMovimientosPorAño() {
		return movimientosPorAño;
	}

	public void setMovimientosPorAño(HashMap<String, MovimientoAgrupadosPorCuenta> movimientosPorAño) {
		this.movimientosPorAño = movimientosPorAño;
	}
	
	public void añadirMovimientoAlAño(String año,String ccc, BigDecimal imputacion){
		
		if (movimientosPorAño.containsKey(año)){
			movimientosPorAño.get(año).añadirSaldoACuenta(ccc, imputacion);
		}
		else {
			movimientosPorAño.put(año, new MovimientoAgrupadosPorCuenta());
			añadirMovimientoAlAño(año, ccc, imputacion);
		}
	}

	
}
