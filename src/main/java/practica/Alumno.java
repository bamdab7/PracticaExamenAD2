package practica;

public class Alumno {
	
	protected int codigo;
	protected String nombre;
	protected String curso;
	
	public Alumno(int codigo, String nombre, String curso) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.curso = curso;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	
	
	
}
