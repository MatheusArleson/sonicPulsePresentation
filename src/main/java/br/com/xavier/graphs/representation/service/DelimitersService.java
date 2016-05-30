package br.com.xavier.graphs.representation.service;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import br.com.xavier.graphs.representation.model.Delimiters;
import br.com.xavier.graphs.representation.model.Room;
import br.com.xavier.graphs.representation.util.StringUtil;

@Service
public class DelimitersService implements Serializable {

	private static final long serialVersionUID = -4015726141509199402L;
	
	
	public void validateDelimiters(Delimiters delimiters) throws IllegalArgumentException {
		if(delimiters == null){
			throw new IllegalArgumentException("Null delimiters obj.");
		}
		
		if(delimiters.getRoomAliasDelimiter() == null){
			throw new IllegalArgumentException("Empty Room alias delimiter");
		}
		
		if(delimiters.getRoomParametersDelimiter() == null){
			throw new IllegalArgumentException("Empty Room parameter delimiter");
		}
		
		if(delimiters.getRoomsDelimiter() == null){
			throw new IllegalArgumentException("Empty Rooms delimiter");
		}
	}
	
	public boolean stringHasAllDelimiters(Delimiters delimiters, String str){
		if(str == null || str.isEmpty()){
			throw new IllegalArgumentException("Empty input");
		}
		
		int indexOfRoomAliasDel = str.indexOf(delimiters.getRoomAliasDelimiter());
		int indexOfRoomParametersDel = str.indexOf(delimiters.getRoomParametersDelimiter());
		int indexOfRoomsDel = str.indexOf(delimiters.getRoomsDelimiter());
		
		boolean hasAllDelimiters = (indexOfRoomAliasDel != -1 && indexOfRoomParametersDel != -1 && indexOfRoomsDel != -1);
		boolean isDelimitersInPosition = (indexOfRoomAliasDel < indexOfRoomParametersDel && indexOfRoomParametersDel < indexOfRoomsDel);
		
		return	hasAllDelimiters && isDelimitersInPosition;
	}
	
	public String[] splitInRooms(Delimiters delimiter, String str){
		return StringUtil.split(delimiter.getRoomsDelimiter(), str);
	}
	
	public String[] splitInRoomAlias(Delimiters delimiter, String str){
		return StringUtil.split(delimiter.getRoomAliasDelimiter(), str);
	}
	
	public String[] splitInRoomParameters(Delimiters delimiter, String str){
		return StringUtil.split(delimiter.getRoomParametersDelimiter(), str);
	}
	
	public static void main(String[] args) {
		RoomService rSvc = new RoomService();
		DelimitersService svc = new DelimitersService();
		
		String str = "Sala1:1,2,3,4\nSala2:5,6,7,8";
		Delimiters del = new Delimiters(":", ",", "\n");
		
		String[] splitInRooms = svc.splitInRooms(del, str);
		for (String roomStr : splitInRooms) {
			String[] roomAlias = svc.splitInRoomAlias(del, roomStr);
			String[] roomParam = svc.splitInRoomParameters(del, roomAlias[1]);
			Room r = rSvc.createRoom(roomAlias[0], roomParam[0], roomParam[1], roomParam[2], roomParam[3]);
			System.out.println(r);
		}
	}
	
}
