package com.codex.codexplt.utl;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;

public class CampoTabla implements Serializable{	
	private String nombre;	
	private Field atributo;
	private int orden;
	
	
	
	public CampoTabla(){
		super();
		nombre = null;
	}
	
	public CampoTabla(String nombre) {
		super();
		this.nombre = nombre;		
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Field getAtributo() {
		return atributo;
	}

	public void setAtributo(Field atributo) {
		this.atributo = atributo;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	
	

}
