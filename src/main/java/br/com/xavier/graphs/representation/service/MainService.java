package br.com.xavier.graphs.representation.service;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.Set;

import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.xavier.graphs.representation.model.Delimiters;
import br.com.xavier.graphs.representation.model.Room;
import br.com.xavier.graphs.representation.util.StringUtil;

@Service
public class MainService implements Serializable {
	
	private static final long serialVersionUID = 6474794517971094775L;

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
	
	
	public void process(String inputDataStr, Delimiters inputDelimiters) throws IllegalArgumentException {
		validateDelimiters(inputDelimiters);
		validateDelimitersInInput(inputDelimiters, inputDataStr);
		
		processInputData(inputDelimiters, inputDataStr);
		
		drawGraph();
		drawRooms();
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
	
	private Set<Room> processInputData(Delimiters inputDelimiters, String inputDataStr) {
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
	
	private void drawRooms() {
		// TODO Auto-generated method stub
		
	}

	private void drawGraph() {
		// TODO Auto-generated method stub
		
	}

}
