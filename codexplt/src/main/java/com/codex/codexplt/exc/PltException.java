package com.codex.codexplt.exc;

public class PltException extends Exception {
	
	private String codigo;
	private String mensaje;
	
	public PltException(String codigo, String mensaje){
		super(mensaje);
		this.codigo = codigo;
		this.mensaje = mensaje;		
	}
	
	public PltException(String codigo){
		this(codigo,null);
	}
	
	public String getCodigo(){
		return codigo;
	}
	
	public String getMensaje(){
		return mensaje;
	}

}
