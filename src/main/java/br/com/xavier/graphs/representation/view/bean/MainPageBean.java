package br.com.xavier.graphs.representation.view.bean;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Scope("view")
@Controller
public class MainPageBean {
	
	private UploadedFile uploadedFile;
	
	public void fileUploadListener(FileUploadEvent event){
		
	}
	
	
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
}