package com.codex.codexplt.vo;

import java.util.ArrayList;
import java.util.List;

public class EstructuraMenu {
	
	private Menu menu;
	private List<EstructuraMenu> hijos;
	
	public EstructuraMenu(){
		this.menu = null;
		this.hijos = new ArrayList<EstructuraMenu>();
	}
	
	
	
	public Menu getMenu() {
		return menu;
	}



	public void setMenu(Menu menu) {
		this.menu = menu;
	}



	public void addMenuHijo(EstructuraMenu hijo){
		hijos.add(hijo);
	}
	
	public List<EstructuraMenu> getHijos(){
		return hijos;
	}

}
