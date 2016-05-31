package br.com.xavier.graphs.representation.service;

import java.awt.Color;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.xavier.graphs.abstractions.AbstractGraph;
import br.com.xavier.graphs.impl.algorithms.color.ColorAlgorithm;
import br.com.xavier.graphs.impl.edges.DefaultUnweightedEdge;
import br.com.xavier.graphs.impl.parser.CytoscapeUnweightedParser;
import br.com.xavier.graphs.representation.model.Delimiters;
import br.com.xavier.graphs.representation.model.Room;
import br.com.xavier.graphs.representation.util.StringUtil;
import br.com.xavier.jsf.PrimefacesUtil;

@Service
public class MainService implements Serializable {
	
	private static final long serialVersionUID = 6474794517971094775L;
	
	private static final String CYTOSCAPE_WIDGET_VAR = "cy";
	private static final String COLOR_GRAPH_ROOM_NODE_SIGNATURE = CYTOSCAPE_WIDGET_VAR + ".$(\"#ID\").css({\"background-color\" : \"#COLOR\"});";
	private static final String COLOR_ROOM_JS_SIGNATURE = "colorirSala(\"idSala\", \"#color\");";
	private static final String SCAN_CYCLE_BASE_STR = "CYCLE #CYCLE_NUMBER# => #ROOMS_ALIASES# \n";
	
	//XXX INJECTED DEPENDENCIES
	@Autowired
	private DelimitersService delimitersSvc;
	
	@Autowired
	private RoomService roomsSvc;
	
	//XXX METHODS
	public String readFileToString(UploadedFile uploadedFile, Charset charset) {
		byte[] fileContents = uploadedFile.getContents();
		return StringUtil.toString(fileContents, charset);
	}
	
	//XXX DRAWN METHODS
	public AbstractGraph<Room, DefaultUnweightedEdge<Room>> drawnElements(String inputDataStr, Delimiters inputDelimiters) throws IllegalArgumentException {
		validateDelimiters(inputDelimiters);
		validateDelimitersInInput(inputDelimiters, inputDataStr);
		
		Set<Room> roomsSet = createRoomsSet(inputDelimiters, inputDataStr);
		checkRoomsAliasIntersection(roomsSet);
		checkRoomsRectangleIntersection(roomsSet);
		
		Map<Room, Set<Room>> adjacencyMap = createAdjacencyMap(roomsSet);
		
		AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph = createGraphInstance(adjacencyMap);
		applyColoring(graph);
		drawGraph(graph);
		
		drawRooms(roomsSet);
		
		return graph;
	}

	private void validateDelimiters(Delimiters delimiters) {
		delimitersSvc.validateDelimiters(delimiters);
	}
	
	private void validateDelimitersInInput(Delimiters delimiters, String inputDataStr) {
		boolean stringHasAllDelimiters = delimitersSvc.stringHasAllDelimiters(delimiters, inputDataStr);
		if(!stringHasAllDelimiters){
			throw new IllegalArgumentException("Malformed Input");
		}
	}
	
	private Set<Room> createRoomsSet(Delimiters inputDelimiters, String inputDataStr) {
		Set<Room> roomsSet = new LinkedHashSet<>();
		String[] roomsStrs = delimitersSvc.splitInRooms(inputDelimiters, inputDataStr);
		for (String roomStr : roomsStrs) {
			String[] roomAlias = delimitersSvc.splitInRoomAlias(inputDelimiters, roomStr);
			String[] roomParam = delimitersSvc.splitInRoomParameters(inputDelimiters, roomAlias[1]);
			Room room = roomsSvc.createRoom(roomAlias[0], roomParam[0], roomParam[1], roomParam[2], roomParam[3]);
			roomsSet.add(room);
		}
		
		return roomsSet;
	}
	
	private void checkRoomsAliasIntersection(Set<Room> roomsSet) {
		roomsSvc.checkRoomsAliasIntersection(roomsSet);
	}
	
	private void checkRoomsRectangleIntersection(Set<Room> roomsSet) {
		roomsSvc.checkRoomsRectangleIntersection(roomsSet);
	}
	
	private Map<Room, Set<Room>> createAdjacencyMap(Set<Room> roomsSet) {
		return roomsSvc.createAdjacencyMap(roomsSet);
	}

	private AbstractGraph<Room, DefaultUnweightedEdge<Room>> createGraphInstance(Map<Room, Set<Room>> adjacencyMap) {
		return roomsSvc.createRoomsGraph(adjacencyMap);
	}

	private void applyColoring(AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph) {
		ColorAlgorithm ca = new ColorAlgorithm();
		ca.applyColor(graph);
	}
	
	private void drawGraph(AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph) {
		CytoscapeUnweightedParser<Room, DefaultUnweightedEdge<Room>> parser = new CytoscapeUnweightedParser<>();
		String script = parser.parse(graph, "cy", "cy");
		System.out.println(script);
		PrimefacesUtil.executeJavascript(script);
	}
	
	private void drawRooms(Set<Room> roomsSet) {
		String script = roomsSvc.generateRoomDrawScript(roomsSet);
		System.out.println(script);
		PrimefacesUtil.executeJavascript(script);
	}
	
	//XXX COLOR METHODS
	public void colorElements(AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph){
		colorGraph(graph);
		colorRooms(graph);
	}

	private void colorGraph(AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph) {
		StringBuffer sb = new StringBuffer();
		
		for (Room room : graph.getAllNodes()) {
			String colorHexStr = StringUtil.getAsString(room.getColor());
			String command = COLOR_GRAPH_ROOM_NODE_SIGNATURE.replace("ID", room.getAlias()).replace("#COLOR", colorHexStr);
			sb.append(command + "\n");
		}
		
		String script = sb.toString();
		System.out.println(script);
		PrimefacesUtil.executeJavascript(script);
	}
	
	private void colorRooms(AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph) {
		StringBuffer sb = new StringBuffer();
		
		for (Room room : graph.getAllNodes()) {
			String colorHexStr = StringUtil.getAsString(room.getColor());
			String command = COLOR_ROOM_JS_SIGNATURE.replace("idSala", room.getAlias()).replace("#color", colorHexStr);
			sb.append(command + "\n");
		}
		
		String script = sb.toString();
		System.out.println(script);
		PrimefacesUtil.executeJavascript(script);
	}
	
	//XXX SCAN CYCLES METHODS
	public String getScanCycles(AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph) {
		Map<Color, Set<Room>> colorDegreeMap = getGraphColorDegreeMap(graph);
		Map<Integer, String> cycleStringsMap = getCycleStringsMap(colorDegreeMap);
		
		StringBuffer sb = new StringBuffer();
		for (Integer cycleMapKey : cycleStringsMap.keySet()) {
			sb.append(cycleStringsMap.get(cycleMapKey));
		}
		
		return sb.toString();
	}

	private Map<Color, Set<Room>> getGraphColorDegreeMap(AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph) {
		Map<Color, Set<Room>> roomColorMap = new LinkedHashMap<>();
		for (Room room : graph.getAllNodes()) {
			Color roomColor = room.getColor();
			
			if(!roomColorMap.containsKey(roomColor)){
				roomColorMap.put(roomColor, new LinkedHashSet<>());
			}
			
			roomColorMap.get(roomColor).add(room);
		}
		
		return roomColorMap;
	}

	private Map<Integer, String> getCycleStringsMap(Map<Color, Set<Room>> colorDegreeMap) {
		Map<Integer, String> cycleMap = new TreeMap<>();
		
		for (Color color : colorDegreeMap.keySet()) {
			StringBuffer roomsSb = new StringBuffer();
			Set<Room> roomsSet = colorDegreeMap.get(color);
			
			Iterator<Room> roomsIt = roomsSet.iterator();
			while(roomsIt.hasNext()){
				Room room = roomsIt.next();
				roomsSb.append(room.getAlias());
				
				if(roomsIt.hasNext()){
					roomsSb.append(", ");
				}
			}
			
			String cycleStr = SCAN_CYCLE_BASE_STR.replace("#ROOMS_ALIASES#", roomsSb.toString());
			
			int mapKey = roomsSet.size();
			while(cycleMap.containsKey(mapKey)){
				mapKey++;
			}
					
			cycleStr = cycleStr.replace("#CYCLE_NUMBER#", String.valueOf(mapKey));
			cycleMap.put(mapKey, cycleStr);
		}
		
		return cycleMap;
	}

}
