package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable{
	
	//declaração dos componentes da tela (dois TextField, dois botoes e um label de erro
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
	}
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();		
	}
	
	private void initializeNodes() {
		
		Constraints.setTextFieldInteger(txtId); //setando para que a TextField só aceite numeros inteiros
		Constraints.setTextFieldMaxLength(txtName, 30); //setando para que a TextField só aceite no máximo 30 caracteres
	}

}
