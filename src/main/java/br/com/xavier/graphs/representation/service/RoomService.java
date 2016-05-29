package br.com.xavier.graphs.representation.service;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import br.com.xavier.graphs.representation.model.Room;

@Service
public class RoomService implements Serializable {

	private static final long serialVersionUID = 8819444601669899734L;
	
	public Room createRoom(String roomAlias, int xPosition, int yPosition, int width, int height){
		Room room = new Room(roomAlias, xPosition, yPosition, width, height);
		return room;
	}
	
	public Room createRoom(String roomAlias, String xPosition, String yPosition, String width, String height){
		Integer xPos = Integer.valueOf(xPosition);
		Integer yPos = Integer.valueOf(yPosition);
		Integer rWidth = Integer.valueOf(width);
		Integer rHeight = Integer.valueOf(height);
		
		Room room = new Room(roomAlias, xPos, yPos, rWidth, rHeight);
		return room;
	}
	
	
}
