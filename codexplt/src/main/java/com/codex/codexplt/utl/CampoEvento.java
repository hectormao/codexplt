package com.codex.codexplt.utl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.sql.ForUpdateFragment;

import com.codex.codexplt.vo.Formulario;
import com.codex.codexplt.vo.Usuario;

public abstract class CampoEvento extends CampoTabla {
	protected Formulario formulario;
	
	protected Usuario usuario;
	
	protected Map<String,Object> argumentos;
	
	private Logger logger;
	
	
	public CampoEvento(){
		super();
		formulario = null;
		usuario = null;
		argumentos = null;
		logger = Logger.getLogger(this.getClass());
	}
	
	public CampoEvento(Formulario formulario, Usuario usuario, Map<String,Object> argumentos){		
		super();
		
		setFormulario(formulario);		
		this.usuario = usuario;
		this.argumentos = argumentos;
		logger = Logger.getLogger(this.getClass());
	}

		
	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
		super.setNombre(formulario.getNombre());
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Map<String, Object> getArgumentos() {
		return argumentos;
	}

	public void setArgumentos(Map<String, Object> argumentos) {
		this.argumentos = argumentos;
	}

	public abstract void ejecutar()  throws Exception;
	
	
}
