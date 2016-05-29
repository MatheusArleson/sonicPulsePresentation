package br.com.xavier.graphs.representation.view.bean;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.xavier.graphs.representation.model.Delimiters;
import br.com.xavier.graphs.representation.service.MainService;
import br.com.xavier.graphs.representation.util.StringUtil;
import br.com.xavier.jsf.JsfUtil;
import br.com.xavier.jsf.PrimefacesUtil;

@Scope("view")
@Controller
public class MainPageBean {
	
	private static final String WV_FILE_UPLOAD_DIALOG = "fileUploadDialog";
	
	@Autowired
	private MainService mainServioe;
	
	//XXX DELIMITERS PROPERTIES
	private Delimiters inputDelimiters;
	
	//XXX INPUT DATA PROPERTIES 
	private String inputDataStr;
	private UploadedFile uploadedFile;
	
	//XXX CONSTRUCTOR
	public MainPageBean() {}
	
	@PostConstruct
	private void initialize(){
		resetInternalData();
	}

	private void resetInternalData() {
		this.inputDelimiters = new Delimiters(":", ",", "\n");
		this.inputDataStr = new String();
		clearUploadedFile();
	}
	
	public void clearUploadedFile(){
		this.uploadedFile = null;
	}
	
	//XXX FILE UPLOAD METHODS
	public void fileUploadListener(FileUploadEvent event){
		this.uploadedFile = event.getFile();
		String fileText = mainServioe.readFileToString(uploadedFile, Charset.defaultCharset());
		
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
	public void process(){
		try{
			mainServioe.process(inputDataStr, inputDelimiters);
		} catch (Exception e){
			JsfUtil.addErrorMessage(e.getMessage());
		}
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
	
}