package com.codex.codexplt.cmp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;

import com.codex.codexplt.exc.PltException;
import com.codex.codexplt.utl.CampoEvento;
import com.codex.codexplt.utl.CampoTabla;
import com.codex.codexplt.utl.ConstantesAdmin;
import com.codex.codexplt.vo.Formulario;

public class TablaDatos extends Listbox implements ListitemRenderer<Object> {
	private static final Logger logger = Logger.getLogger(TablaDatos.class);
	
	private Class clase;
	
	private ListModelList<Object> modelo;
	
	private List<CampoTabla> listaCampos;
	
	protected Map<String,Object> argumentos;
	
	
	public TablaDatos(Class clase) throws NoSuchFieldException, SecurityException{
		this(clase,null,null);
	}
	
	public TablaDatos(Class clase, List<CampoTabla> listaCampos,Map<String,Object> argumentos) throws NoSuchFieldException, SecurityException{
		super();
		
		this.argumentos = argumentos;
		modelo = new ListModelList<Object>();
		this.setModel(modelo);
		this.setItemRenderer(this);
		this.clase = clase;
		
		Listhead cabeceraTabla = new Listhead();
		cabeceraTabla.setSizable(true);
		
		if(listaCampos != null){
			this.listaCampos = listaCampos;
			
			//mapaCampos = new HashMap<String, Boolean>();
			for(CampoTabla campo : listaCampos){
				//mapaCampos.put(nombreCampo, Boolean.TRUE);
				String nombre = campo.getNombre();
				if(! (campo instanceof CampoEvento)){
					Field atributo = campo.getAtributo();
					if(atributo != null){
						pintarColumnaTabla(cabeceraTabla, atributo);
						
					}
				} else {
					pintarColumnaTabla(cabeceraTabla, "", "columna-boton");
				}
			}
		} else {
			this.listaCampos = new ArrayList<CampoTabla>();
			for(Field atributo : clase.getDeclaredFields()){
				CampoTabla campo = new CampoTabla(atributo.getName());
				this.listaCampos.add(campo);
				pintarColumnaTabla(cabeceraTabla, atributo);
				campo.setAtributo(atributo);
			}
		}
		
		this.appendChild(cabeceraTabla);
	}
	
	private void pintarColumnaTabla(Listhead cabeceraTabla, Field campo){
		pintarColumnaTabla(cabeceraTabla, campo.getName());
	}
	
	private void pintarColumnaTabla(Listhead cabeceraTabla, String nombreCampo){
		pintarColumnaTabla(cabeceraTabla, nombreCampo,null);
		
	}
	private void pintarColumnaTabla(Listhead cabeceraTabla, String nombreCampo, String clase){
			Listheader columna = new Listheader();
			String leyenda = Labels.getLabel(nombreCampo);
			if(StringUtils.isBlank(leyenda)){
				leyenda = nombreCampo;
			}
			columna.setLabel(leyenda);
			cabeceraTabla.appendChild(columna);
			if(clase != null){
				columna.setSclass(clase);
			}
	}
	
	
	public void setDatos(List<Object> datos){
		modelo.clear();
		modelo.addAll(datos);		
	}
	
	private boolean siAnulado(Object obj){
		try{
			Method metodoSiAnulado = obj.getClass().getDeclaredMethod("isAudiSiAnul");
			
			return (boolean) metodoSiAnulado.invoke(obj);

		} catch(Exception ex){
			return false;
		}
	}
	
	private String getMotivoAnulacion(Object obj){
		try{
			Method metodoSiAnulado = obj.getClass().getDeclaredMethod("getAudiMotiAnul");
			
			return (String) metodoSiAnulado.invoke(obj);

		} catch(Exception ex){
			return null;
		}
	}

	@Override
	public void render(final Listitem item, Object data, int index) throws Exception {
		item.setValue(data);
		
		if(siAnulado(data)){
			item.setSclass("registro-anulado");
			
			String motivo = getMotivoAnulacion(data);
			if(motivo != null){
			
				item.setTooltiptext(Labels.getLabel("app.tooltipAnulado", new Object[]{motivo}));
			}
		}
		
		
		
		
		for(CampoTabla campo : listaCampos){
			if(! (campo instanceof CampoEvento)){
				//es un campo del objeto
				
				Field atributo = campo.getAtributo();
				String nombreCampo = atributo.getName();
				Class tipo = atributo.getType();
				String nombreMetodo = new StringBuilder(tipo.equals(Boolean.class) || tipo.equals(boolean.class) ? "is" : "get").append(nombreCampo.substring(0, 1).toUpperCase()).append(nombreCampo.substring(1)).toString();
				
				try{
					Method metodo = clase.getMethod(nombreMetodo);				
					Object objeto = metodo.invoke(data);
					
					Listcell celda = new Listcell(objeto != null ? objeto.toString() : "");
					item.appendChild(celda);
				} catch(NoSuchMethodError ex){
					logger.error(new StringBuilder(ex.getClass().getName()).append(": ").append(ex.getMessage()),ex);
				}
			} else {
				
				Listcell celda = new Listcell();
				
				celda.setSclass("columna-boton");
				
				final CampoEvento campoEvento = (CampoEvento) campo;				
				Formulario form = campoEvento.getFormulario();				
				String icono = form.getUrlIcono();
				
				Toolbarbutton boton = new Toolbarbutton();
				boton.setImage(icono);
				
				String tooltip = Labels.getLabel(form.getTooltip());
				if(tooltip == null){
					tooltip = form.getTooltip();
				}
				
				boton.setTooltiptext(tooltip);
				
				boton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						try{
							
							Object valor = item.getValue();
							
							Map<String,Object> argumentosHijo = new HashMap<String, Object>();
							argumentosHijo.putAll(argumentos);							
							argumentosHijo.put(ConstantesAdmin.ARG_FORMULARIO, campoEvento.getFormulario());
							argumentosHijo.put(ConstantesAdmin.ARG_SELECCION, valor);
							
							campoEvento.setArgumentos(argumentosHijo);							
							campoEvento.ejecutar();
							
						} catch(PltException ex){
							Messagebox.show(Labels.getLabel(ex.getCodigo()), "Error", Messagebox.OK, Messagebox.ERROR);
							logger.error("Al ejecutar Evento",ex);
						}
						
					}
					
				});
				
				celda.appendChild(boton);
				item.appendChild(celda);
								
			}
			
			
		} 
		
		
	}
	

}
