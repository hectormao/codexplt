package com.codex.codexplt.utl;



import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import com.codex.codexplt.ngc.PlataformaNgc;
import com.codex.codexplt.vo.Dominio;
import com.codex.codexplt.vo.Formulario;
import com.codex.codexplt.vo.Usuario;

public class WindowComposer extends GenericForwardComposer<Window>{

	protected Formulario formulario;
	
	protected Usuario usuario;
	
	protected Map<String,Object> argumentos;
	
	private Logger logger;
	
	protected PlataformaNgc plataformaNgc;
	
	@Override
	public void doAfterCompose(Window win) throws Exception{
		
		super.doAfterCompose(win);
		
		llenarCombosDominio(win);
		
		logger = Logger.getLogger(this.getClass());
		
		argumentos = (Map<String, Object>) Executions.getCurrent().getArg();
		
		usuario = (Usuario) argumentos.get(ConstantesAdmin.ARG_USUARIO);
		
		if(usuario == null){
			throw new Exception("No hay usuario registrado");
		}
		
		formulario = (Formulario) argumentos.get(ConstantesAdmin.ARG_FORMULARIO);
		
		if(formulario == null){
			throw new Exception("No hay formulario registrado");
		}
		
		final Window estaVentana = this.self;
		
		this.self.addEventListener(Events.ON_CREATE, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				
				Object objetoParametro = argumentos.get(ConstantesAdmin.ARG_SELECCION);
				
				try{
					switch(formulario.getTipo()){
						case ConstantesAdmin.FORMULARIO_TIPO_BORRAR:
						case ConstantesAdmin.FORMULARIO_TIPO_EDITAR:{
							if(objetoParametro == null){
								throw new Exception(Labels.getLabel("error.0007"));
							} else {
								
								Boolean siAnulado = null;
								
								try{
									Method metodo = objetoParametro.getClass().getMethod("isAudiSiAnul");
									siAnulado = (Boolean) metodo.invoke(objetoParametro);
								} catch(Exception ex){
									logger.error(new StringBuilder("Error al obtener metodo de anulado para: ").append(objetoParametro.getClass().getName()).append(" - ").append(ex.getClass().getName()).append(": ").append(ex.getMessage()).toString(), ex);
								}
								
								if(siAnulado != null && siAnulado.booleanValue()){
									throw new Exception(Labels.getLabel("error.0008"));
								}
								
							}
						}break;
					
					}
					
				} catch(Exception ex){
					logger.error(new StringBuilder("Error al crear ventana: ").append(formulario.getNombre()).append(" - ").append(ex.getClass().getName()).append(": ").append(ex.getMessage()).toString(), ex);
					Messagebox.show(ex.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					Events.sendEvent(Events.ON_CLOSE,estaVentana,null);
				}
				
				
				
			}
		});
		
		
		if(logger.isDebugEnabled()) logger.debug(new StringBuilder("Usuario: ").append(usuario.getLogin()).append(" Abriendo formulario: ").append(formulario.getNombre()).toString());
	}
	
	private void llenarCombosDominio(Component comp){
		Collection<Component> hijos = comp.getFellows();
		for(Component hijo : hijos){
			if(hijo instanceof Combobox){
				String nombreDominio = (String) hijo.getAttribute(ConstantesAdmin.DOMINIO);
				if(nombreDominio != null){
					llenarComboDominio((Combobox) hijo, nombreDominio);
				}
			}
		}
	}
	
	protected void llenarComboDominio(Combobox combo, String nombreDominio){
		List<Dominio> dominio = plataformaNgc.getDominio(nombreDominio);
		if(dominio != null){
			for(Dominio item : dominio){
				Comboitem ci = new Comboitem(item.getLeyenda());
				ci.setValue(item);
				
				combo.appendChild(ci);
			}
		}
	}
	
	protected void seleccionarComboDominio(Combobox combo, String valor){
		
		if(valor != null){
			for(Comboitem ci : combo.getItems()){
				Dominio dominio = ci.getValue();
				if(dominio.getValor().equals(valor)){
					combo.setSelectedItem(ci);
					break;
				}
			}
		}
		
	}
	
	protected String getSeleccionComboDominio(Combobox combo, boolean requerido){
		
		Comboitem ci = combo.getSelectedItem();
		if(ci != null){
			Dominio dominio = ci.getValue();
			if(dominio != null){
				return dominio.getValor();
			} else {
				if(requerido){
					throw new WrongValueException(combo, Labels.getLabel("error.1007"));
				}
				return null;
			}
			
			
		} else {
			if(requerido){
				throw new WrongValueException(combo, Labels.getLabel("error.1007"));
			}
			
			return null;
		}
		
	}
	
	public Formulario getFormulario(String nombre){
		Map<String,Formulario> formularios = (Map<String, Formulario>) session.getAttribute(ConstantesAdmin.SESION_MAPA_FORM);
		if(formularios == null){
			return null;
		}
		
		Formulario formulario = formularios.get(nombre);
		
		return formulario;
	}
	
	public void soloConsulta(){
		
		for(Component componente : this.self.getFellows()){
			deshabilitarComponente(componente);
		}
		
		
	}
	
	private void deshabilitarComponente(Component componente){
		if(componente instanceof InputElement){
			((InputElement)componente).setDisabled(true);
		} else if(componente instanceof Button){
			if(! (componente.getId().equals("btnAceptar") || componente.getId().equals("btnCancelar"))){
				((Button)componente).setDisabled(true);
			}
		}else if(componente instanceof Checkbox){			
			((Checkbox)componente).setDisabled(true);
			
		} else if(componente instanceof Listbox){
			Listbox lista = (Listbox)componente;
			lista.setCheckmark(false);
			lista.setMultiple(false);
			for(Listitem li : lista.getItems()){
				for(Object obj : li.getChildren()){
					if(obj instanceof Listcell){
						Listcell celda = (Listcell) obj;
						Component componenteCelda = celda.getFirstChild();
						deshabilitarComponente(componenteCelda);
					}
				}
			}
		} else if(componente instanceof Tree){
			Tree arbol = (Tree) componente;
			for(Treeitem ti : arbol.getItems()){
				Treerow tr = (Treerow) ti.getFirstChild();
				
				for(Object obj : tr.getChildren()){
					if(obj instanceof Treecell){
						Treecell celda = (Treecell) obj;
						Component componenteCelda = celda.getFirstChild();
						deshabilitarComponente(componenteCelda);
					}
				}
				
			}
		}
	}
	
	
	public String solicitarNota(){
		
		final Window win = new Window();
		this.self.appendChild(win);
		
		Borderlayout borderLayout = new Borderlayout();
		win.appendChild(borderLayout);
		
		North norte = new North();
		norte.setBorder("none");
		borderLayout.appendChild(norte);
		Label lblNota = new Label(Labels.getLabel("app.nota"));
		norte.appendChild(lblNota);
		
		Center centro = new Center();
		centro.setBorder("none");
		borderLayout.appendChild(centro);
		Textbox txtNota = new Textbox();
		txtNota.setMaxlength(1024);
		txtNota.setWidth("250px");
		txtNota.setRows(3);
		centro.appendChild(txtNota);
		
		
		South sur = new South();
		sur.setBorder("none");
		borderLayout.appendChild(sur);
		Button boton = new Button(Labels.getLabel("app.aceptar"));
		boton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Events.sendEvent(Events.ON_CLOSE,win,null);
				
			}
		});
		Div divBoton = new Div();
		divBoton.setWidth("100%");
		divBoton.appendChild(boton);
		divBoton.setAlign("center");
		
		sur.appendChild(divBoton);
		
		win.setTitle(Labels.getLabel("app.nota"));
		win.setWidth("270px");
		win.setHeight("160px");
		win.setBorder("normal");
		win.setClosable(false);
		win.setSizable(false);
		
		
		
		win.setVisible(true);
		win.doModal();
		
		
		
		return txtNota.getText();
		
	}
	
	public void abrirFormulario(Formulario formulario, Object seleccion, EventListener<Event> eventoCerrar) throws Exception{
		
		Map<String,Object> argumentosHijo = new HashMap<String, Object>();
		argumentosHijo.putAll(argumentos);
		
		argumentosHijo.put(ConstantesAdmin.ARG_FORMULARIO, formulario);
		argumentosHijo.put(ConstantesAdmin.ARG_SELECCION, seleccion);
		
		
		java.io.InputStream zulInput = this.getClass().getClassLoader().getResourceAsStream(formulario.getUrl()) ;
		java.io.Reader zulReader = new java.io.InputStreamReader(zulInput) ;
		
		
		Window winFormulario = (Window) Executions.createComponentsDirectly(zulReader,"zul",this.self,argumentosHijo) ;
		
		String nombre = formulario.getNombre();
		
		String titulo = Labels.getLabel(nombre);
		if(titulo == null){
			titulo = nombre;
		}
		
		winFormulario.setTitle(titulo);
		
		
		
		if(eventoCerrar != null){
			winFormulario.addEventListener(Events.ON_CLOSE, eventoCerrar);
		}
		
		winFormulario.doModal();
		
		
	}

	public PlataformaNgc getPlataformaNgc() {
		return plataformaNgc;
	}

	public void setPlataformaNgc(PlataformaNgc plataformaNgc) {
		this.plataformaNgc = plataformaNgc;
	}
	
	
	
	
}
