package it.polito.tdp.porto;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	this.model.setGrafo();
    	if(boxPrimo.getValue()!=null) {
    		Author a = boxPrimo.getValue();
    		
    		if(!model.getCoautori(a).isEmpty()) {
    		
    		for(Author aut: model.getCoautori(a)) {
    			
    			txtResult.appendText(aut.toString()+"\n");
    		}
    		boxSecondo.getItems().addAll(model.nonVicini(boxPrimo.getValue()));
    	}
    		else {
    			txtResult.appendText("L'autore non ha coautori");
    		}
    	}
    	else {
    		txtResult.setText("Seleziona un primo autore");
    	}

    	
    }

    @FXML
    void handleSequenza(ActionEvent event) {

    	txtResult.clear();
    	if(boxSecondo.getValue()!=null && boxPrimo.getValue()!=null) {
    		for(Paper p: model.getPapers(boxPrimo.getValue(), boxSecondo.getValue())) {
    			txtResult.appendText(p.toString()+"\n");
    		}
    		
    	}else txtResult.setText("Seleziona un secondo autore");
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
		this.model= model;
		this.boxPrimo.getItems().addAll(model.getAutoriMap().values());
		
		
	}
}
