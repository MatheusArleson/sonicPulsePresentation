package br.com.xavier.graphs.representation.model;

import java.awt.Color;
import java.io.Serializable;

import br.com.xavier.graphs.interfaces.nodes.ColoredNode;

public class Room implements ColoredNode, Serializable {
	
	private static final long serialVersionUID = 8282034103902232581L;
	
	//XXX PROPERTTIES
	private int xPosition;
	private int yPosition;
	private int width;
	private int height;
	private String alias;
	private Color color;
	
	//XXX CONSTRUCTORS
	public Room(String alias, int xPosition, int yPosition, int width, int height) {
		this(alias, xPosition, yPosition, width, height, Color.WHITE);
	}
	
	public Room(String alias, int xPosition, int yPosition, int width, int height, Color color) {
		super();
		this.alias = alias;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	//XXX OVERRIDE METHODS
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + xPosition;
		result = prime * result + yPosition;
		result = prime * result + width;
		result = prime * result + height;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		
		Room other = (Room) obj;
		if (xPosition != other.xPosition){ return false; }
		if (yPosition != other.yPosition) { return false; }
		if (width != other.width) { return false; }
		if (height != other.height) { return false; }
		
		if (alias == null) {
			if (other.alias != null) { 
				return false; 
			}
		} else if (!alias.equals(other.alias)) {
			return false;
		}
		
		if (color == null) {
			if (other.color != null) { 
				return false; 
			}
		} else if (!color.equals(other.color)) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return "Room [" 
			+ "alias=" + alias	
			+ ", xPosition=" + xPosition 
			+ ", yPosition=" + yPosition 
			+ ", width=" + width 
			+ ", height=" + height
			+ ", color=" + color 
		+ "]";
	}

	//XXX GETTERS/SETTERS
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
