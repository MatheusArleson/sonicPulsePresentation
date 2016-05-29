package br.com.xavier.graphs.representation.model;

public class Delimiters {

	//XXX PROPERTIES
	private String roomAliasDelimiter;
	private String roomParametersDelimiter;
	private String roomsDelimiter;
	
	//XXX CONSTRUCTOR
	public Delimiters(String roomAliasDelimiter, String roomParametersDelimiter, String roomsDelimiter) {
		super();
		this.roomAliasDelimiter = roomAliasDelimiter;
		this.roomParametersDelimiter = roomParametersDelimiter;
		this.roomsDelimiter = roomsDelimiter;
	}
	
	//XXX GETTERS/SETTERS
	public String getRoomAliasDelimiter() {
		return roomAliasDelimiter;
	}

	public void setRoomAliasDelimiter(String roomAliasDelimiter) {
		this.roomAliasDelimiter = roomAliasDelimiter;
	}

	public String getRoomParametersDelimiter() {
		return roomParametersDelimiter;
	}

	public void setRoomParametersDelimiter(String roomParametersDelimiter) {
		this.roomParametersDelimiter = roomParametersDelimiter;
	}

	public String getRoomsDelimiter() {
		return roomsDelimiter;
	}

	public void setRoomsDelimiter(String roomsDelimiter) {
		this.roomsDelimiter = roomsDelimiter;
	}
	
}
