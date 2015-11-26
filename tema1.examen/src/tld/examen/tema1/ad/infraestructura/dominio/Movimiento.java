package tld.examen.tema1.ad.infraestructura.dominio;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movimiento {

	public Movimiento() {
		super();
		timestamp = null;
		ccc = null;
		variacion = null;
		concepto = null;
	}

	public static void main(String args[]) {

		Movimiento mov1 = new Movimiento("20091132T131805", "ES7620770024003102575761", "+15000,23", "Nomina");
		Movimiento mov2 = new Movimiento("20091103T131805", "ES7620770024003102575761", "-15000,23", "Nomina");

		System.out.println(mov1);
		System.out.println(mov2);

	}

	private Date timestamp;
	private String ccc;
	private BigDecimal variacion;
	private String concepto;

	public Movimiento(String timestamp, String ccc, String variacion, String concepto) {

		setTimestamp(timestamp);
		setCcc(ccc);
		setVariacion(variacion);
		setConcepto(concepto);

	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getCcc() {
		return ccc;
	}

	public BigDecimal getVariacion() {
		return variacion;
	}

	
	public String getConcepto() {
		return concepto;
	}

	@Override
	public String toString() {
		return "Movimiento [timestamp=" + timestamp + ", ccc=" + ccc + ", variacion=" + variacion + ", concepto="
				+ concepto + "]";
	}

	public void setTimestamp(String timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		try {
			this.timestamp = sdf.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setCcc(String ccc) {
		this.ccc = ccc;
	}

	public void setVariacion(String variacion) {
		NumberFormat nf = NumberFormat.getInstance(new Locale("es", "es"));
		try {
			Number nb = nf.parse(variacion.replace("+", ""));
			this.variacion = new BigDecimal(nb.toString());

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getAño() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String año = format.format(timestamp);
		return año;
	}

}
