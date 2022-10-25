package practica;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Principal {
	//		DECLARAMOS EL ARRAY DONDE SE VAN A ALMACENAR LOS VALORES
	private static ArrayList<Alumno> listaAlumno = new ArrayList<Alumno>();
	
	public static void main(String[] args) throws SQLException, ParserConfigurationException, TransformerException {
		// TODO Borrar datos de mysql, y leer los datos de la tabla de postrgrade, y volcarlos en la tabla de mysql y despues generar un doc xml con la estructura de la tabla
		//		borrarDatos();
		leerDatosPostGrade();
		// pasarDatosMSQL();
		generarXML();
	}

	public  static void generarXML() throws ParserConfigurationException, TransformerException {
		for(Alumno alumno: listaAlumno) {
			System.out.println(alumno.getCodigo() + " "+  alumno.getNombre() + " "+ alumno.getCurso());
		}		
		//	GENERAMOS UN ARCHIVO XML
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factoria.newDocumentBuilder();
		Document documento = db.newDocument();
		
		// 	GENERAMOS LOS ELEMENTOS
		Element alumnos = documento.createElement("alumnos");
		documento.appendChild(alumnos);
		//		ITERACION PARA LISTAR LOS VALORES DEL ARRAY
		for( int i= 0; i<listaAlumno.size(); i++) {
			Element alumno= documento.createElement("alumno");;
			alumnos.appendChild(alumno);
			
			Element codigo = documento.createElement("codigo");
			codigo.setTextContent(String.valueOf(listaAlumno.get(i).getCodigo())) ; //me lo transforma a int
			alumno.appendChild(codigo);
			
			Element nombre = documento.createElement("nombre");
			nombre.setTextContent(listaAlumno.get(i).getNombre());
			alumno.appendChild(nombre);
			
			Element curso = documento.createElement("curso");
			curso.setTextContent(listaAlumno.get(i).getCurso());
			alumno.appendChild(curso);
		}
		//TRANSFORMAMOS EL XML
		TransformerFactory ft = TransformerFactory.newInstance();
		Transformer transformer = ft.newTransformer();		
		DOMSource dom = new DOMSource(documento);		
		StreamResult sr = new StreamResult(new File("C:\\PRUEBAS\\AD\\practica2Alumno.xml"));
		transformer.transform(dom,sr);		
	}

	public static void pasarDatosMSQL()  throws SQLException {
		//		ESTABLECEREMOS LA CONEXION
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/dam22?serverTimezone=Europe/Madrid","root","");
		//		HACEMOS LA CONSULTA DE INSERT Y TIENE QUE ESTAR DENTRO DEL ARRAY BLABLA
		for(int i = 0; i<listaAlumno.size(); i++) {
			PreparedStatement ps = conexion.prepareStatement("INSERT INTO alumno2 (codigo,nombre,ciclo) VALUES (?,?,?)");
		//	 ESTABLECEREMOS LOS VALORES EN FUNCION DEL ARRAY
			ps.setInt(1, listaAlumno.get(i).getCodigo());
			ps.setString(2, listaAlumno.get(i).getNombre());
			ps.setString(3, listaAlumno.get(i).getCurso());
			
			ps.executeUpdate();
		}
	}

	public static void leerDatosPostGrade() throws SQLException {
		//		ESTABLECEMOS CONEXION
		Connection conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dam22?serverTimezone=Europe/Madrid","postgres","1234");
		//		HACEMOS LA CONSULTA DE MOSTRAR
		PreparedStatement ps= conexion.prepareStatement("SELECT * FROM alumno");
		//		RECOGEMOS LOS VALORESY LOS METEMOS EN UN ARRAY
		ResultSet resultado = ps.executeQuery();
		//		COMPROBAMOS QUE HEMOS ALMACENADO LSO VALORES
		while(resultado.next()) {
			listaAlumno.add(new Alumno(resultado.getInt(1),resultado.getString(2),resultado.getString(3)));
		}
		
		/* for(Alumno alumno: listaAlumno) {
			System.out.println(alumno.getCodigo() + " "+  alumno.getNombre() + " "+ alumno.getCurso());
		} */
	}

	public static void borrarDatos() throws SQLException {
		//		ESTABLECEMOS CONEXION
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/dam22?serverTimezone=Europe/Madrid","root","");
		//		HAGO LA CONSULTA
		PreparedStatement ps= conexion.prepareStatement("DELETE  FROM alumno2");
		ps.executeUpdate();
	}
}
