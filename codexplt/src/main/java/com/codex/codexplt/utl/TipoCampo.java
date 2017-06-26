package com.codex.codexplt.utl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Textbox;

public enum TipoCampo {

	CADENA(1, "tipo.cadena"), FECHA(3, "tipo.fecha"), NUMERO(2, "tipo.numero");

	private int id;
	private String nombre;
	
	private static final DecimalFormat df = new DecimalFormat(ConstantesAdmin.FORMATO_NUMERO);
	private static final SimpleDateFormat sdf = new SimpleDateFormat(ConstantesAdmin.FORMATO_FECHA);

	private TipoCampo(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	private String toString(Object valor) {

		if (valor == null) {
			return null;
		}

		switch (this) {
		case CADENA:
			return valor.toString();
		case NUMERO:
			
			Number numero = (Number) valor;
			return df.format(numero);
		case FECHA:			
			Date fecha = (Date) valor;
			return sdf.format(fecha);
		default:
			return null;

		}

	}

	public String getValor(Component cmp) {
		switch (this) {
		case CADENA:
			Textbox txt = (Textbox) cmp;
			return txt.getValue();
		case NUMERO:
			Decimalbox dcb = (Decimalbox) cmp;
			
			BigDecimal num = dcb.getValue();
			if(num == null){
				return null;
			} else {								
				return df.format(num);
			}
			
		case FECHA:
			Datebox dtb = (Datebox) cmp;
			
			Date fec = dtb.getValue();
			if(fec == null){
				return null;
			} else {								
				return sdf.format(fec);
			}
			
			
		default:
			return null;
		}
	}

	public Component getComponente(boolean siRequerido) {
		switch (this) {
		case CADENA:
			Textbox txt = new Textbox();
			txt.setWidth("100%");
			if (siRequerido) {
				txt.setConstraint("no empty");
			}
			return txt;
		case NUMERO:
			Decimalbox dcb = new Decimalbox();
			dcb.setFormat(ConstantesAdmin.FORMATO_NUMERO);
			dcb.setWidth("100%");
			if (siRequerido) {
				dcb.setConstraint("no empty");
			}
			return dcb;
		case FECHA:
			Datebox dtb = new Datebox();
			dtb.setFormat(ConstantesAdmin.FORMATO_FECHA);
			dtb.setWidth("100%");
			if (siRequerido) {
				dtb.setConstraint("no empty");
			}
			return dtb;
		default:
			return null;
		}

	}

	public static TipoCampo getTipoPorID(int id) {

		switch (id) {
		case 1:
			return TipoCampo.CADENA;
		case 2:
			return TipoCampo.NUMERO;
		case 3:
			return TipoCampo.FECHA;
		default:
			return null;
		}
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

}
