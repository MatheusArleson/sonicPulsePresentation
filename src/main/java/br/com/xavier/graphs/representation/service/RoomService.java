package br.com.xavier.graphs.representation.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.xavier.graphs.abstractions.AbstractGraph;
import br.com.xavier.graphs.impl.edges.DefaultUnweightedEdge;
import br.com.xavier.graphs.impl.simple.directed.DefaultSDUGraph;
import br.com.xavier.graphs.representation.model.Room;

@Service
public class RoomService implements Serializable {
	
	private static final long serialVersionUID = 8819444601669899734L;
	
	private static final String HTML_ELEMENT_ROOMS = "roomsCanvas";
	private static final String DRAW_ROOM_JS_METHOD_SIGNATURE = "criarSala(\"idElementoCanvas\", \"idSala\", xPos, yPos, height, width);";
	
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
	
	public void checkRoomsRectangleIntersection(Set<Room> roomsSet) {
		if(roomsSet == null || roomsSet.isEmpty()){
			throw new IllegalArgumentException("Empty rooms set");
		}
		
		for (Room room : roomsSet) {
			for (Room otherRoom : roomsSet) {
				if(room.equals(otherRoom)){
					continue;
				}
				
				if(room.intersects(otherRoom)){
					throw new IllegalArgumentException("Room intersection detectected between rooms :  " + room.getAlias() + " and " + otherRoom.getAlias());
				}
			}
		}
	}
	
	public void checkRoomsAliasIntersection(Set<Room> roomsSet) {
		for (Room room : roomsSet) {
			for (Room otherRoom : roomsSet) {
				if(room.equals(otherRoom)){
					continue;
				}
				
				if(room.getAlias().equals(otherRoom.getAlias())){
					throw new IllegalArgumentException("Room Alias intersection detectected :  " + room.getAlias());
				}
			}
		}
	}
	
	public Map<Room, Set<Room>> createAdjacencyMap(Set<Room> roomsSet) {
		Map<Room, Set<Room>> adjacencyMap = new LinkedHashMap<>(roomsSet.size());
		if(roomsSet == null || roomsSet.isEmpty()){
			return adjacencyMap;
		}
		
		for (Room room : roomsSet) {
			Set<Room> adjacentRoomsSet = new LinkedHashSet<>();
			
			for (Room toCheckRoom : roomsSet) {
				boolean intersects = isAdjacentRoom(room, toCheckRoom);
				if(intersects){
					adjacentRoomsSet.add(toCheckRoom);
				}
			}
			
			adjacencyMap.put(room, adjacentRoomsSet);
		}
		
		return adjacencyMap;
	}

	private boolean isAdjacentRoom(Room room, Room toCheckRoom) {
		if(room.equals(toCheckRoom)){
			return false;
		}
		
		boolean intersects = (
			room.getExpandedTop().intersects(toCheckRoom) ||
			room.getExpandedBottom().intersects(toCheckRoom) ||
			room.getExpandedLeft().intersects(toCheckRoom) ||
			room.getExpandedRight().intersects(toCheckRoom)
		);
		return intersects;
	}
	
	public AbstractGraph<Room, DefaultUnweightedEdge<Room>> createRoomsGraph(Map<Room, Set<Room>> adjacencyMap){
		DefaultSDUGraph<Room, DefaultUnweightedEdge<Room>> graph = new DefaultSDUGraph<>();
		
		if(adjacencyMap == null || adjacencyMap.isEmpty()){
			return graph;
		}
		
		Set<Room> mapKeysSet = adjacencyMap.keySet();
		for (Room room : mapKeysSet) {
			graph.addNode(room);
		}
		
		
		for (Room room : mapKeysSet) {
			Set<Room> adjacencySet = adjacencyMap.get(room);
			for (Room adjacentRoom : adjacencySet) {
				graph.addEdge(new DefaultUnweightedEdge<Room>(room, adjacentRoom));
			}
		}
		
		return graph;
	}

	public String generateRoomDrawScript(Set<Room> roomsSet){
		StringBuffer sb = new StringBuffer();
		
		for (Room room : roomsSet) {
			String command = getCreateRoomCommand(HTML_ELEMENT_ROOMS, room);
			sb.append(command + "\n");
		}
		
		String script = sb.toString();
		return script;
	}

	private String getCreateRoomCommand(String htmlElementId, Room room) {
		String command = DRAW_ROOM_JS_METHOD_SIGNATURE.replace("idElementoCanvas", htmlElementId)
			.replace("idSala", room.getAlias()).replace("xPos", String.valueOf(room.getX()))
			.replace("yPos", String.valueOf(room.getY())).replace("height", String.valueOf(room.getHeight()))
			.replace("width", String.valueOf(room.getWidth()));
		return command;
	}

	
}
