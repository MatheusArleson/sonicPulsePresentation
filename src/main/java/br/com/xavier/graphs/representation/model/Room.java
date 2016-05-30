package br.com.xavier.graphs.representation.model;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;

import br.com.xavier.graphs.abstractions.nodes.AbstractColoredNode;
import br.com.xavier.graphs.interfaces.nodes.ColoredNode;

public class Room extends AbstractColoredNode implements ColoredNode, Serializable {
	
	private static final long serialVersionUID = 8282034103902232581L;
	
	//XXX PROPERTTIES
	private final Rectangle roomRectangle;
	private String alias;
	private Color color;
	
	//XXX CONSTRUCTORS
	public Room(String alias, int xPosition, int yPosition, int width, int height) {
		this(alias, xPosition, yPosition, width, height, Color.WHITE);
	}
	
	public Room(String alias, int xPosition, int yPosition, int width, int height, Color color) {
		super();
		this.alias = alias;
		this.roomRectangle = new Rectangle(xPosition, yPosition, width, height);
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
		result = prime * result + ((roomRectangle == null) ? 0 : roomRectangle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		
		Room other = (Room) obj;
		if (alias == null) {
			if (other.alias != null) { 
				return false; 
			}
		} else if (!alias.equals(other.alias)) {
			return false;
		}
		
		if (roomRectangle == null) {
			if (other.roomRectangle != null) { 
				return false; 
			}
		} else if (!roomRectangle.equals(other.roomRectangle)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String getLabel() {
		return getAlias();
	}
	
	@Override
	public String toString() {
		return "Room [" 
			+ "alias=" + alias
			+ ", roomRectangle= " + roomRectangle
			+ ", color=" + color 
		+ "]";
	}

	//XXX METHODS
	public boolean intersects(Room otherRoom){
		return this.roomRectangle.intersects(otherRoom.roomRectangle);
	}
	
	public Room getExpandedTop(){
		return new Room(alias, getX(), getY(), getWidth(), getHeight()+1);
	}
	
	public Room getExpandedBottom(){
		return new Room(alias, getX(), getY()-1, getWidth(), getHeight());
	}
	
	public Room getExpandedLeft(){
		return new Room(alias, getX()-1, getY(), getWidth(), getHeight());
	}
	
	public Room getExpandedRight(){
		return new Room(alias, getX(), getY(), getWidth()+1, getHeight());
	}
	
	//XXX GETTERS/SETTERS
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public int getX() {
		return new Integer(roomRectangle.x);
	}

	public int getY() {
		return new Integer(roomRectangle.y);
	}

	public int getWidth() {
		return new Integer(roomRectangle.width);
	}

	public int getHeight() {
		return new Integer(roomRectangle.height);
	}

}
