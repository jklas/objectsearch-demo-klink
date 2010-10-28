package com.klink.domain;

public class Language {

	private long id;
	
	/**
	 * Nombre en el idioma inglés
	 */
	private String baseName;
	
	/**
	 * Nombre del idioma escrito en ese idioma
	 */
	private String localName;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getLocalName() {
		return localName;
	}
	
}
