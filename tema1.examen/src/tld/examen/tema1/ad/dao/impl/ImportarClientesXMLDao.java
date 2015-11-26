package tld.examen.tema1.ad.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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

import tld.examen.tema1.ad.infraestructura.dominio.Cliente;
import tld.examen.tema1.ad.infraestructura.excepciones.DaoException;

public class ImportarClientesXMLDao {

	public static void main(String[] argumentos) {

		File fichero = new File("examen/ficheros/clientes.xml");
		System.out.println("El fichero existe? " + fichero.exists());
		try {
			List<Cliente> listaClientes = ImportarClientesXMLDao.leerClientesXML(fichero);
			for (Iterator<Cliente> iterator = listaClientes.iterator(); iterator.hasNext();) {
				Cliente cliente = (Cliente) iterator.next();
				System.out.println(cliente);

			}
		} catch (DaoException e) {

			e.printStackTrace();
		}
		return;
	}

	/**
	 * Obtenemos una lista de Cliente a partir de un XML en formato:
	 * <?xml version="1.0" encoding="UTF-8" standalone="no" ?> 
	 * <clientes>
 	 *<cliente id="1">
	 *	<nif> 11111111A </nif>
	 *	<nombre> Pepe Rapel Flor1 </nombre>
	 *	<domicilio> Mi casita es la mejor1 </domicilio>
	 *	<ccc>ES7620770024003102575761 </ccc>
 	 * </cliente>
 	 * <cliente id="2">
	 *	<nif> 22222222B </nif>
	 *	<nombre> Pepe Rapel Martín </nombre>
	 *	<domicilio> Debajo de un puente </domicilio>
	 *	<ccc>ES7620770024003102575762 </ccc>
 	 * </cliente>
 	 * <cliente id="3">
	 *	<nif> 33333333B </nif>
	 *	<nombre> Juan Rapel Ramírez </nombre>
	 *	<domicilio> Debajo de un puente1 </domicilio>
	 *	<ccc>ES7620770024003102575763 </ccc>
 	 * </cliente>
 	 * <cliente id="4">
	 * 	<nif> 44444444P </nif>
	 * 	<nombre> Rafael Rapel Flores </nombre>
	 * 	<domicilio> Debajo de un puente2 </domicilio>
	 *	<ccc>ES7620770024003102575764 </ccc>
 	 * </cliente>
 	 * <cliente id="5">
	 *	<nif> 55555555P </nif>
	 *	<nombre> Luis Rapel Flores </nombre>
	 *	<domicilio> Debajo de un puente </domicilio>
	 *	<ccc>ES7620770024003102575765 </ccc>
 	 * </cliente>

     * </clientes>
	 * 
	 * @param fichero La ruta del  fichero donde está el fichero  
	 * @return Una lista de Cliente 
	 * @throws DaoException Si se produce algún error leyendo el XML
	 */
	
	public static List<Cliente> leerClientesXML(File fichero) throws DaoException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		List<Cliente> clientes = null;

		try (InputStream xmlInput = new FileInputStream(fichero)) {
			SAXParser saxParser;
			saxParser = factory.newSAXParser();
			SaxHandler handler = new SaxHandler();
			saxParser.parse(xmlInput, handler);
			clientes = handler.getClientes();
		} catch (FileNotFoundException e) {
			throw new DaoException("El fichero clientes XML no existe", e);
		} catch (SAXException e) {
			throw new DaoException("Error en el parseado de fichero XML de clientes", e);
		} catch (IOException e) {
			throw new DaoException("Error en el acceso al fichero", e);
		} catch (ParserConfigurationException e) {
			throw new DaoException("Error en la configuración del parser", e);
		}

		return clientes;
	}

	/**
	 * Clase interna que trata el manejo con SAX del fichero XML
	 * @author sergio5
	 *
	 */
	private static class SaxHandler extends DefaultHandler {

		private List<Cliente> clientes = new ArrayList<Cliente>();

		private Stack<String> elementStack = new Stack<String>();
		private Stack<Object> objectStack = new Stack<Object>();

		public List<Cliente> getClientes() {
			return clientes;
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {

			this.elementStack.push(qName);

			if ("cliente".equals(qName)) {
				Cliente cliente = new Cliente("", "", "", "", new BigDecimal("0.00"));
				this.objectStack.push(cliente);
			}

		}

		public void endElement(String uri, String localName, String qName) throws SAXException {

			this.elementStack.pop();
			if ("cliente".equals(qName)) {
				clientes.add((Cliente) objectStack.pop());
			}

		}

		private String currentElement() {
			return this.elementStack.peek();
		}

		public void characters(char ch[], int start, int length) throws SAXException {

			String value = new String(ch, start, length).trim();
			if (value.length() == 0)
				return; // Ignora los espacios en blanco

			if ("nif".equals(currentElement())) {

				((Cliente) objectStack.peek()).setNif(value);
			}

			if ("nombre".equals(currentElement())) {

				((Cliente) objectStack.peek()).setNombre(value);

			}
			if ("domicilio".equals(currentElement())) {

				((Cliente) objectStack.peek()).setDomicilio(value);

			}
			if ("ccc".equals(currentElement())) {

				((Cliente) objectStack.peek()).setCcc(value);

			}

		}
	}
}
