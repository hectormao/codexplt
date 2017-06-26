package com.codex.codexplt.cnt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.From;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.East;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.North;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.codex.codexplt.ant.Columna;
import com.codex.codexplt.ant.Tabla;
import com.codex.codexplt.cmp.TablaDatos;
import com.codex.codexplt.ngc.PlataformaNgc;
import com.codex.codexplt.utl.CampoEvento;
import com.codex.codexplt.utl.CampoTabla;
import com.codex.codexplt.utl.ConstantesAdmin;
import com.codex.codexplt.utl.WindowComposer;
import com.codex.codexplt.vo.Formulario;
import com.codex.codexplt.vo.FormularioHijo;
import com.codex.codexplt.vo.RegistroTabla;

public class TablaDatosCnt extends WindowComposer {

	private Toolbar herramientas;
	private Borderlayout border;
	private Div dvTabla;
	private North norte;
	private Checkbox chkSiAnulado;

	private TablaDatos tablaDatos;

	

	private Class clase;
	
	private East eastDetalle;
	private Tabs tabsDetalle;
	private Tabpanels panelsDetalle;
	
	
	private List<Formulario> listaDetalles;
	
	private List<FormularioHijo> listaCeldas;
	
	private List<Window> listaWinDetalle;
	
	private Object padre;
	private String nombreAtributo;
	private boolean filtrarSiPadreNull;
	
	private boolean crearHijos;
	
	private boolean mostrarAnulados = false;
	
	private String argumentoWhere;
	
	@Override
	public void doAfterCompose(Window win) throws Exception {
		super.doAfterCompose(win);
		
		crearHijos = true;
		
		padre = argumentos.get(ConstantesAdmin.OBJETO_PADRE);
		nombreAtributo = (String) argumentos.get(ConstantesAdmin.NOMBRE_ATRIBUTO_PADRE);
		Boolean aux = (Boolean) argumentos.get(ConstantesAdmin.FILTRAR_PADRE_NULL);
		if(aux != null){
			filtrarSiPadreNull = aux.booleanValue();
		} else {
			filtrarSiPadreNull = false;
		}

		listaDetalles = new ArrayList<Formulario>();
		listaCeldas = new ArrayList<FormularioHijo>();
		
		List<FormularioHijo> hijos = formulario.getHijos();
		if(hijos != null){
			for (FormularioHijo hijo : hijos) {
				switch (hijo.getTipo()) {
				case ConstantesAdmin.HIJO_BOTON:
					//crearBoton
					crearBotonHerramienta(hijo.getHijo());
					break;
				case ConstantesAdmin.HIJO_CELDA:
					//agregar a la lista de celdas
					listaCeldas.add(hijo);
					break;
	
				case ConstantesAdmin.HIJO_DETALLE:
					//agregar a la lista 
					listaDetalles.add(hijo.getHijo());
					break;
	
				default:
					break;
				}
			}
		}
		
		if(listaDetalles.isEmpty()){
			eastDetalle.setVisible(false);
		}
		
		List<CampoTabla> listaCampos = null;
		
		boolean cargarAlInicio = false;
		
		String sClase = formulario.getAtributo(ConstantesAdmin.ARG_CLASE);
		if(sClase != null){
			clase = Class.forName(sClase);
			
			cargarAlInicio = true;
		} else {
			clase = (Class) argumentos.get(ConstantesAdmin.ARG_CLASE);
			
		}
		
		String sArgumento = formulario.getAtributo(ConstantesAdmin.ARG_WHERE);
		if(sArgumento != null){
			argumentoWhere = sArgumento;
			
			
		} else {
			argumentoWhere = (String) argumentos.get(ConstantesAdmin.ARG_WHERE);
			
		}
		
		if(clase.isAnnotationPresent(Tabla.class)){
			
			listaCampos = new ArrayList<>();
			
			for(Field campo : clase.getDeclaredFields()){
				if(campo.isAnnotationPresent(Columna.class)){
					Columna columna = (Columna) campo.getAnnotation(Columna.class);
					boolean mostrar = false; 
					
					String[] aplica = columna.aplica();
					if(aplica.length == 0 ){
						mostrar = true;
					} else {
						String nombreFormulario = formulario.getNombre();
						for(String aplicaFormulario : aplica){
							if(aplicaFormulario.equals(nombreFormulario)){
								mostrar = true;
								break;
							}
						}
					}
					
					if(mostrar){
						CampoTabla campoTabla = new CampoTabla(campo.getName());
						campoTabla.setAtributo(campo);
						campoTabla.setOrden(columna.orden());
							
						int idx = 0;
						while(idx < listaCampos.size()){
							if(columna.orden() < listaCampos.get(idx).getOrden()){
								listaCampos.add(idx, campoTabla);
								break;
							}
							
							idx ++;
						}
						
						if(idx >= listaCampos.size()){
							listaCampos.add(campoTabla);
						}
					}	
					
					
				}
			}
			
		}
		
		if(! listaCeldas.isEmpty()){			
			for(FormularioHijo formHijo : listaCeldas){
				
				Formulario form = formHijo.getHijo();
				String url = form.getUrl();
				CampoEvento campoEvento;
				if(url.trim().toLowerCase().endsWith(".zul")){
					campoEvento = crearCampoEventoZul(form);
				} else {
					campoEvento = crearCampoEvento(form);
				}
				
				int orden = formHijo.getOrden();
				
				if(orden < 0){
					orden = 1;
				}
				
				orden --;
				if(orden >= listaCampos.size()){
					listaCampos.add(campoEvento);
				} else {
					listaCampos.add(orden,campoEvento);
				}
				
				
			}
			
		}
		
		

		tablaDatos = new TablaDatos(clase, listaCampos, argumentos);
		dvTabla.appendChild(tablaDatos);
		tablaDatos.setHflex("1");
		tablaDatos.setVflex("1");
		tablaDatos.setMold("paging");
		tablaDatos.setPagingPosition("bottom");
		tablaDatos.setPageSize(10);

		if (herramientas.getChildren().size() <= 0) {
			norte.setVisible(false);
		}

		win.addEventListener(Events.ON_USER, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {

				Map<String, Object> datos = (Map<String, Object>) arg0.getData();
				argumentos.putAll(datos);
				
				String accion = datos.get(ConstantesAdmin.ACCION).toString();
				
				
				if (accion.equals(ConstantesAdmin.EVENTO_REFRESCAR)) {

					padre = datos.get(ConstantesAdmin.OBJETO_PADRE);
					nombreAtributo = (String) datos.get(ConstantesAdmin.NOMBRE_ATRIBUTO_PADRE);
					
					Boolean aux = (Boolean) argumentos.get(ConstantesAdmin.FILTRAR_PADRE_NULL);
					if(aux != null){
						filtrarSiPadreNull = aux.booleanValue();
					} else {
						filtrarSiPadreNull = false;
					}
					
					cerrarDetalle();
					
					
					String where = (String) datos.get(ConstantesAdmin.ARG_WHERE);
					
					refrescarTabla(padre, nombreAtributo, filtrarSiPadreNull, where);
				}

			}
		});
		
		win.addEventListener(Events.ON_CREATE, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				;
			}
		});
		
		if(listaDetalles != null && ! listaDetalles.isEmpty()){
			tablaDatos.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					
					Listitem li = tablaDatos.getSelectedItem();
					Object seleccion = li != null ? li.getValue() : null;
					
					if(crearHijos){
						crearHijosDetalle(seleccion);
						crearHijos = false;
					} else {
						actualizarHijosDetalle(seleccion);
					}
					
					if(! eastDetalle.isOpen()){
						//eastDetalle.setVisible(true);
						eastDetalle.setOpen(true);						
						
						
					}
					
				}
			});
		}
		if(cargarAlInicio){
			refrescarTabla(padre,nombreAtributo, filtrarSiPadreNull);
		}
	}
	
	private CampoEvento crearCampoEvento(Formulario form) throws Exception{
		
		Class clase = Class.forName(form.getUrl());
		
		CampoEvento campoEvento = (CampoEvento) clase.newInstance();
		campoEvento.setFormulario(form);
		campoEvento.setNombre(form.getNombre());
		campoEvento.setUsuario(usuario);
		
		
		return campoEvento;
	}

	private CampoEvento crearCampoEventoZul(final Formulario form) {
		
		final Window estaVentana = this.self;
		
		CampoEvento campoEvento = new CampoEvento() {
			
			@Override
			public void ejecutar() throws Exception {
				
				
				Map<String,Object> argumentosHijo = new HashMap<String, Object>();
				argumentosHijo.putAll(argumentos);
				Listitem li = tablaDatos.getSelectedItem();
				Object valor = null;
				if(li != null){
					valor = li.getValue();
					argumentosHijo.put(ConstantesAdmin.ARG_SELECCION, valor);
				}
				argumentosHijo.put(ConstantesAdmin.ARG_FORMULARIO, form);
				
				
				
				java.io.InputStream zulInput = this.getClass().getClassLoader().getResourceAsStream(form.getUrl()) ;
				java.io.Reader zulReader = new java.io.InputStreamReader(zulInput) ;
				
				
				Window winCargar = (Window) Executions.createComponentsDirectly(zulReader,"zul",estaVentana,argumentosHijo) ;
				
				String nombre = form.getNombre();
				
				String titulo = Labels.getLabel(nombre);
				if(titulo == null){
					titulo = nombre;
				}
				
				winCargar.setTitle(titulo);
				
				winCargar.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						String res = (String) arg0.getData();
						if(res != null && res.equals(ConstantesAdmin.EXITO)){
							refrescarTabla(padre,nombreAtributo, filtrarSiPadreNull);
						}
					}
				});
				
				winCargar.doModal(); 
				
			}
		};
		
		campoEvento.setFormulario(form);
		campoEvento.setNombre(form.getNombre());
		campoEvento.setUsuario(usuario);
		
		return campoEvento;
	}
	
	private void cerrarDetalle() throws Exception{
		if(listaWinDetalle == null){
			return;
		}
		
		if(listaWinDetalle.isEmpty()){
			return;
		}
		
		for(Window winDetalle : listaWinDetalle){
			Events.sendEvent(Events.ON_CLOSE, winDetalle, null);
		}
		crearHijos = true;
		eastDetalle.setOpen(false);
		
	}
	
	
	private void actualizarHijosDetalle(Object seleccion) throws Exception{
		if(listaWinDetalle == null){
			return;
		}
		
		if(listaWinDetalle.isEmpty()){
			return;
		}
		
		Map<String, Object> datos = new HashMap<>();
		datos.put(ConstantesAdmin.ACCION,ConstantesAdmin.EVENTO_REFRESCAR);
		datos.put(ConstantesAdmin.OBJETO_PADRE,seleccion);
		
		for(Window winDetalle : listaWinDetalle){
			Events.sendEvent(Events.ON_USER, winDetalle, datos);
		}
		
	}
	
	private void crearHijosDetalle(Object seleccion) throws Exception{
		listaWinDetalle = new ArrayList<Window>();
		
		for(Formulario detalle : listaDetalles){
			final Tabpanel panel = new Tabpanel();
			final Tab tab = new Tab();
			
			String nombre = detalle.getNombre();
			String leyenda = Labels.getLabel(nombre);
			if(leyenda == null){
				leyenda = nombre;
			}
			
			tab.setLabel(leyenda);
			
						
			panelsDetalle.appendChild(panel);
			
			String url = detalle.getUrl();
			
			Map<String,Object> marg = new HashMap<String, Object>();
			marg.put(ConstantesAdmin.ARG_FORMULARIO,detalle);
			marg.put(ConstantesAdmin.ARG_USUARIO,usuario);
			marg.put(ConstantesAdmin.OBJETO_PADRE,seleccion);
			
			
			Window winDetalle = null;
			
			try{			
				winDetalle = (Window) Executions.createComponents(url, panel, marg);
				
			} catch(Exception ex){
				java.io.InputStream zulInput = this.getClass().getClassLoader().getResourceAsStream(url) ;
				java.io.Reader zulReader = new java.io.InputStreamReader(zulInput) ;
				winDetalle = (Window) Executions.createComponentsDirectly(zulReader,"zul",panel,marg) ;
			}
			
			winDetalle.setBorder("none");
			winDetalle.doEmbedded();
			
			winDetalle.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

				@Override
				public void onEvent(Event arg0) throws Exception {
					Events.sendEvent(Events.ON_CLOSE,panel,null);
					Events.sendEvent(Events.ON_CLOSE,tab,null);
					
				}
				
			});
			
			listaWinDetalle.add(winDetalle);
			
			tabsDetalle.appendChild(tab);
			
		}
		
		
	}
	

	private void crearBotonHerramienta(final Formulario hijo) {
		Toolbarbutton boton = new Toolbarbutton();
		boton.setImage(hijo.getUrlIcono());
		
		String tooltip = Labels.getLabel(hijo.getTooltip());
		if(tooltip == null){
			tooltip = hijo.getTooltip();
		}
		
		boton.setTooltiptext(tooltip);
		
		final Window estaVentana = this.self;
		
		boton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Map<String,Object> argumentosHijo = new HashMap<String, Object>();
				argumentosHijo.putAll(argumentos);
				Listitem li = tablaDatos.getSelectedItem();
				Object valor = null;
				if(li != null){
					valor = li.getValue();
				}
				argumentosHijo.put(ConstantesAdmin.ARG_FORMULARIO, hijo);
				argumentosHijo.put(ConstantesAdmin.ARG_SELECCION, valor);
				
				
				java.io.InputStream zulInput = this.getClass().getClassLoader().getResourceAsStream(hijo.getUrl()) ;
				java.io.Reader zulReader = new java.io.InputStreamReader(zulInput) ;
				
				
				Window winCargar = (Window) Executions.createComponentsDirectly(zulReader,"zul",estaVentana,argumentosHijo) ;
				
				String nombre = hijo.getNombre();
				
				String titulo = Labels.getLabel(nombre);
				if(titulo == null){
					titulo = nombre;
				}
				
				winCargar.setTitle(titulo);
				
				winCargar.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						Object res = arg0.getData();
						if(res instanceof String){
							if(((String)res).equals(ConstantesAdmin.EXITO)){
								refrescarTabla(padre,nombreAtributo, filtrarSiPadreNull);
							}
						} else if(res != null) {
							refrescarTabla(padre,nombreAtributo, filtrarSiPadreNull);
						}
					}
				});
				
				winCargar.doModal();
				
				
				
				
				
			}
		});
		
		herramientas.appendChild(boton);
		
	}

	private void refrescarTabla(Object padre, String nombreAtributo, boolean filtrarSiPadreNull) throws Exception {
		refrescarTabla(padre, nombreAtributo, filtrarSiPadreNull, null);
	}
	
	private void refrescarTabla(Object padre, String nombreAtributo, boolean filtrarSiPadreNull, String whereRefrescar)
			throws Exception {

		
		
		
		List<Object> datos = null;
		
		StringBuilder where = new StringBuilder();
		
		
		
		if(argumentoWhere != null){
			where.append("(");
			where.append(argumentoWhere);
			where.append(")");
		}
		
		if(whereRefrescar != null){
			
			if(where.length() > 0){
				where.append(" and ");
			}
			
			where.append("(");
			where.append(whereRefrescar);
			where.append(")");
		}

		if(padre == null && filtrarSiPadreNull){
			if(where.length() > 0){
				where.append(" and ");
			}
			where.append("(");
			where.append(nombreAtributo);
			where.append(" is null)");
			
		} else if(padre != null) {
			if (nombreAtributo == null) {
				for (Field atributo : clase.getDeclaredFields()) {					
					
					if (atributo.getType().equals(padre.getClass().getGenericSuperclass()) || atributo.getType().equals(padre.getClass())) {
						nombreAtributo = atributo.getName();
						break;
					}
				}
			}
			
			
			if(where.length() > 0){
				where.append(" and ");
			}
			
			where.append("(");
			where.append(nombreAtributo);
			where.append(".");
			String condicion = plataformaNgc.getCondicionPadre(padre);
			where.append(condicion);
			where.append(")");
			
			
		}
		
		String orderBy = null;
		
		if(clase.isAnnotationPresent(Tabla.class)){
			Tabla tabla = (Tabla) clase.getAnnotation(Tabla.class);
			orderBy = tabla.orderBy();
		}
		
		datos = plataformaNgc.getDatos(clase, where.toString(), orderBy);
		List<Object> datosAMostrar = new ArrayList<>();
		for(Object dato : datos){
			
			boolean siMostrar = true; 
			
			try{
				
				if(! mostrarAnulados){
					Method metodo = dato.getClass().getMethod(ConstantesAdmin.METODO_ANULADO);
					siMostrar = ! (boolean) metodo.invoke(dato);					
				}
				
			} catch(Exception ex){
				;
			}
			
			if(siMostrar){
				if(dato instanceof RegistroTabla){
					RegistroTabla registroTabla = (RegistroTabla) dato;
					if(registroTabla.siMostrar() && registroTabla.siMostrarSegunUsuario(usuario)){
						datosAMostrar.add(dato);
					}
				} else {
					datosAMostrar.add(dato);
				}
			}
		}

		tablaDatos.setDatos(datosAMostrar);
	}

	

	public boolean isMostrarAnulados() {
		return mostrarAnulados;
	}

	public void setMostrarAnulados(boolean mostrarAnulados) {
		this.mostrarAnulados = mostrarAnulados;
	}
	
	public void onCheck$chkSiAnulado(Event evt) throws Exception{
		
		
		setMostrarAnulados(chkSiAnulado.isChecked());
		refrescarTabla(padre, nombreAtributo,filtrarSiPadreNull);
		
	}

}
