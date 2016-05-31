package br.com.xavier.graphs.representation.view.bean;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.xavier.graphs.abstractions.AbstractGraph;
import br.com.xavier.graphs.impl.edges.DefaultUnweightedEdge;
import br.com.xavier.graphs.representation.model.Delimiters;
import br.com.xavier.graphs.representation.model.Room;
import br.com.xavier.graphs.representation.service.MainService;
import br.com.xavier.graphs.representation.util.StringUtil;
import br.com.xavier.jsf.JsfUtil;
import br.com.xavier.jsf.PrimefacesUtil;

@Scope("view")
@Controller
public class MainPageBean {
	
	private static final String WV_FILE_UPLOAD_DIALOG = "fileUploadDialog";
	
	@Autowired
	private MainService mainService;
	
	//XXX DELIMITERS PROPERTIES
	private Delimiters inputDelimiters;
	
	//XXX INPUT DATA PROPERTIES 
	private String inputDataStr;
	private UploadedFile uploadedFile;
	
	//XXX GRAPH PROPERTIES
	AbstractGraph<Room, DefaultUnweightedEdge<Room>> graph;
	
	//XXX OUTPUT DATA PROPERTIES
	private String scanCyclesStr;
	
	//XXX CONSTRUCTOR
	public MainPageBean() {}
	
	@PostConstruct
	private void initialize(){
		resetInternalData();
	}

	private void resetInternalData() {
		this.inputDelimiters = new Delimiters(":", ",", "\n");
		this.inputDataStr = new String();
		this.scanCyclesStr = new String();
		this.graph = null;
		clearUploadedFile();
	}
	
	public void clearUploadedFile(){
		this.uploadedFile = null;
	}
	
	//XXX FILE UPLOAD METHODS
	public void fileUploadListener(FileUploadEvent event){
		this.uploadedFile = event.getFile();
		String fileText = mainService.readFileToString(uploadedFile, Charset.defaultCharset());
		
		if(StringUtil.isNullOrEmpty(fileText)){
			JsfUtil.addErrorMessage("Empty file content.");
			PrimefacesUtil.hideByWidgetVar(WV_FILE_UPLOAD_DIALOG);
			return;
		}
		
		this.inputDataStr = fileText;
		JsfUtil.addSucessMessage("File content loaded.");
		PrimefacesUtil.hideByWidgetVar(WV_FILE_UPLOAD_DIALOG);
	}
	
	//XXX PROCESS METHODS
	public void drawnInput(){
		try{
			scanCyclesStr = null;
			
			String roomsDelimiter = inputDelimiters.getRoomsDelimiter();
			if(roomsDelimiter == null || roomsDelimiter.isEmpty()){
				inputDelimiters.setRoomsDelimiter("\n");
			}
			
			graph = mainService.drawnElements(inputDataStr, inputDelimiters);
			
		} catch (Exception e){
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	public void applyColor(){
		if(graph == null){
			JsfUtil.addErrorMessage("Process some input first.");
		}
		
		try{
			
			mainService.colorElements(graph);
			scanCyclesStr = mainService.getScanCycles(graph);
			
		} catch (Exception e){
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	public boolean disableColor(){
		return (graph == null);
	}
	
	//XXX GETTERS/SETTERS
	public Delimiters getInputDelimiters() {
		return inputDelimiters;
	}
	
	public void setInputDelimiters(Delimiters inputDelimiters) {
		this.inputDelimiters = inputDelimiters;
	}
	
	public String getInputDataStr() {
		return inputDataStr;
	}
	
	public void setInputDataStr(String inputDataStr) {
		this.inputDataStr = inputDataStr;
	}
	
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public String getScanCyclesStr() {
		return scanCyclesStr;
	}
	
	public void setScanCyclesStr(String scanCyclesStr) {
		this.scanCyclesStr = scanCyclesStr;
	}
	
}