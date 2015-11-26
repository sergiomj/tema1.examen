package tld.examen.tema1.ad.infraestructura.dominio;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaldoCuenta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8770937705345515168L;
	private String ccc;
	private String balanceCuenta;

	public SaldoCuenta(String ccc, BigDecimal balance) {
		super();
		this.ccc = ccc;
		this.balanceCuenta = balance.toPlainString();
	}

	public String getCcc() {
		return ccc;
	}

	public void setCcc(String ccc) {
		this.ccc = ccc;
	}

	public BigDecimal getBalance() {
		return new BigDecimal(balanceCuenta);
	}

	public void setBalance(BigDecimal balance) {
		this.balanceCuenta = balance.toPlainString();
	}

	@Override
	public String toString() {
		return "SaldoCuenta [ccc=" + ccc + ", balance=" + balanceCuenta + "]";
	}

	public void inputarMovimiento(BigDecimal movimiento) {
		setBalance(getBalance().add(movimiento));
	}

}
