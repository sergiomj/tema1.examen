package tld.examen.tema1.ad.dao.impl;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tld.examen.tema1.ad.infraestructura.dominio.MovimientoAgrupadosPorCuenta;
import tld.examen.tema1.ad.infraestructura.dominio.MovimientosPorAño;
import tld.examen.tema1.ad.infraestructura.dominio.SaldoCuenta;
import tld.examen.tema1.ad.infraestructura.excepciones.DaoException;

/**
 * 
 * Clase encargarda de realiza la exportación de los movimientos en las cuentas a un formato como XML 
 * @author sergio5
 *
 */
public class ExportarResumenBalanceDao {

	/**
	 * Dado un año y Objeto movimientoPorAño nos obtiene una cadena formateada en XML con el formato siguiente:
	 * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
	 *	<resultado periodo="2011">
  	 *		<usuario>
     *			<ccc>112312312</ccc>
     * 			<balance>4,02</balance>
  	 *		</usuario>
  	 *		<usuario>
     *			<ccc>112312311</ccc>
     *			<balance>2,00</balance>
  	 *		</usuario>
	 *	</resultado>
	 *
	 *con el resumen de los  movimientos en cada cuenta en ese año 
	 *
	 * @param año 
	 * @param movimientosPorAño El objeto MovimientosPorAño en donde se encuentra los datos resumenes de las cuentas.
	 * @return El XML en el formato anterior con el resumenes de las cuentas
	 * @throws DaoException 
	 */
	public String createXML(String año,MovimientosPorAño movimientosPorAño  ) throws DaoException {
		
		MovimientoAgrupadosPorCuenta movimientosPorcuentas=movimientosPorAño.getMovimientosPorAño().get(año);
		
		StringWriter xmlSalida = new StringWriter();
		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			// define root elements
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("resultado");
			document.appendChild(rootElement);
			Attr attribute = document.createAttribute("periodo");
			attribute.setValue(año);

			rootElement.setAttributeNode(attribute);
			
			// Comprobamos que exista movimientos para ese año
			if (movimientosPorcuentas!=null)
				generarClienteXML(document, rootElement, movimientosPorcuentas);

			// Vamos a crear y escribir el xml en un StringWriter ( una cadena)
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(xmlSalida);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(domSource, streamResult);

		} catch (ParserConfigurationException e) {
			throw new DaoException("No se ha podido  generar correntamente  el XML con la configuación dada", e);
		} catch (TransformerException e) {
			throw new DaoException("No se ha podido  generar correntamente  el XML", e);
		}
		return xmlSalida.toString();
	}

	private void generarClienteXML(Document document, Element rootElement,
			MovimientoAgrupadosPorCuenta movimientoAgrupadosPorCuenta) {
		
		HashMap<String, SaldoCuenta> movimientosEnElAño = movimientoAgrupadosPorCuenta.getCuentas();
		if (movimientosEnElAño != null) {
			Set<Entry<String, SaldoCuenta>> desgloseDeMovimientosPorCuenta = movimientosEnElAño.entrySet();

			for (Iterator<Entry<String, SaldoCuenta>> iterator = desgloseDeMovimientosPorCuenta.iterator(); iterator.hasNext();) {
				Entry<String, SaldoCuenta> entry = (Entry<String, SaldoCuenta>) iterator.next();
				
				Element usuario = document.createElement("usuario");

				rootElement.appendChild(usuario);

				Element ccc = document.createElement("ccc");
				ccc.appendChild(document.createTextNode(entry.getKey()));
				usuario.appendChild(ccc);

				Element balance = document.createElement("balance");
				Formatter formateador=new Formatter(new Locale("es", "es"));
				// Generamos el balance con dos decimales en español
				String balanceEnEspañol=formateador.format("%.2f",entry.getValue().getBalance()).toString();
				formateador.close();
				balance.appendChild(document.createTextNode(balanceEnEspañol));
				usuario.appendChild(balance);

			}

		}
	}

	public static void main(String[] args) {
		MovimientosPorAño movimientosPorAño = new MovimientosPorAño();
		movimientosPorAño.añadirMovimientoAlAño("2011", "112312312", new BigDecimal("1.001"));
		movimientosPorAño.añadirMovimientoAlAño("2010", "112312311", new BigDecimal("2.015"));
		movimientosPorAño.añadirMovimientoAlAño("2011", "112312311", new BigDecimal("3.00"));
		movimientosPorAño.añadirMovimientoAlAño("2011", "112312311", new BigDecimal("-1.00"));
		movimientosPorAño.añadirMovimientoAlAño("2011", "112312312", new BigDecimal("3.019"));

		String xml="";
		try {
			xml = new ExportarResumenBalanceDao().createXML("2011",movimientosPorAño	);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(xml);

	}

}
