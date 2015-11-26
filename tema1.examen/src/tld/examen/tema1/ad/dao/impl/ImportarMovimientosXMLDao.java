package tld.examen.tema1.ad.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tld.examen.tema1.ad.infraestructura.dominio.Movimiento;
import tld.examen.tema1.ad.infraestructura.excepciones.DaoException;

/**
 * 
 * Clase que va a tratar los movimiento en las cuentas en XML en el formato
 * <?xml version="1.0" encoding="UTF-8" standalone="no" ?> 
 *<movimientos timestamp="20131103T131805">
 *
 *	<movimiento>	
 *		<timestamp> 20091103T131805 </timestamp>
 *		<cccdestino> ES7620770024003102575761</cccdestino>
 *		<variacion> +1,20 </variacion>
 *		<concepto> Nomina </concepto>
 *	</movimiento>	
 *
 *
 *	<movimiento>	
 *		<timestamp> 20091103T131806 </timestamp>
 *		<cccdestino> ES7620770024003102575761</cccdestino>
 *		<variacion> -1,10 </variacion>
 *		<concepto> El cole de los niños </concepto>
 *	</movimiento>	
 *
 *	<movimiento>	
 *		<timestamp> 20101103T131807 </timestamp>
 *		<cccdestino> ES7620770024003102575761</cccdestino>
 *		<variacion> +2,3 </variacion>
 *		<concepto> Dinero no muy legal </concepto>
 *	</movimiento>	
 *				
 *	<movimiento>	
 *		<timestamp> 20091103T131808 </timestamp>
 *		<cccdestino> ES7620770024003102575762</cccdestino>
 *		<variacion> +700 </variacion>
 *		<concepto> Ingreso Inicial </concepto>
 *	</movimiento>	
 *	<movimiento>	
 *		<timestamp> 20091103T131809 </timestamp>
 *		<cccdestino> ES7620770024003102575762</cccdestino>
 *		<variacion> -70 </variacion>
 *		<concepto> Reintegro cajero </concepto>
 *	</movimiento>	
 *
 *	<movimiento>	
 *		<timestamp> 20101103T131810 </timestamp>
 *		<cccdestino> ES7620770024003102575762</cccdestino>
 *		<variacion>  -23</variacion>
 *		<concepto> Comunidad vecinos</concepto>
 *	</movimiento>	
 *
 *</movimientos>
 * 
 * @author sergio5
 *
 */
public class ImportarMovimientosXMLDao {

	public static void main(String[] argumentos) {

		File fichero = new File("examen/ficheros/movimientos.xml");
		System.out.println("El fichero existe? " + fichero.exists());
		try {
			List<Movimiento> listaMovimientos = ImportarMovimientosXMLDao.leerMovimientoXML(fichero);
			for (Iterator<Movimiento> iterator = listaMovimientos.iterator(); iterator.hasNext();) {
				Movimiento movimientos = (Movimiento) iterator.next();
				System.out.println(movimientos);

			}
		} catch (DaoException e) {

			e.printStackTrace();
		}
		return;
	}

	/**
	 * A partir de la ruta del fichero del XML obtiene una lista de Movimiento
	 * @param fichero La ruta del fichero del XML
	 * @return La lista de Movimientos
	 * @throws DaoException Si se produce algún error leyendo el XML
	 */
	public static List<Movimiento> leerMovimientoXML(File fichero) throws DaoException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		List<Movimiento> movimientos = null;

		try (InputStream xmlInput = new FileInputStream(fichero)) {
			SAXParser saxParser;
			saxParser = factory.newSAXParser();
			SaxHandler handler = new SaxHandler();
			saxParser.parse(xmlInput, handler);
			movimientos = handler.getMovimientos();
		} catch (FileNotFoundException e) {
			throw new DaoException("El fichero clientes XML no existe", e);
		} catch (SAXException e) {
			throw new DaoException("Error en el parseado de fichero XML de movimientos", e);
		} catch (IOException e) {
			throw new DaoException("Error en el acceso al fichero", e);
		} catch (ParserConfigurationException e) {
			throw new DaoException("Error en la configuración del parser", e);
		}

		return movimientos;
	}

	/**
	 * Clase interna que trata en SAX el tratamiento del XML
	 * @author sergio5
	 *
	 */
	private static   class SaxHandler extends DefaultHandler {

		private List<Movimiento> movimientos = new ArrayList<Movimiento>();

		private Stack<String> elementStack = new Stack<String>();
		// Almacena el último objeto Movimiento 
		private Stack<Object> objectStack = new Stack<Object>();

		public List<Movimiento> getMovimientos() {
			return movimientos;
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {

			this.elementStack.push(qName);

			if ("movimiento".equals(qName)) {
				Movimiento movimientos = new Movimiento();
				this.objectStack.push(movimientos);
			}

		}

		public void endElement(String uri, String localName, String qName) throws SAXException {

			this.elementStack.pop();
			if ("movimiento".equals(qName)) {
				movimientos.add((Movimiento) objectStack.pop());
			}

		}

		private String currentElement() {
			return this.elementStack.peek();
		}

		public void characters(char ch[], int start, int length) throws SAXException {

			String value = new String(ch, start, length).trim();
			if (value.length() == 0)
				return; // Ignora los espacios en blanco

			if ("timestamp".equals(currentElement())) {

				((Movimiento) objectStack.peek()).setTimestamp(value);
			}

			if ("cccdestino".equals(currentElement())) {

				((Movimiento) objectStack.peek()).setCcc(value);

			}
			if ("variacion".equals(currentElement())) {

				((Movimiento) objectStack.peek()).setVariacion(value);
			}
			if ("concepto".equals(currentElement())) {

				((Movimiento) objectStack.peek()).setConcepto(value);

			}

		}
	}
}
