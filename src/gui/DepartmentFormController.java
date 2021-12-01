package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
	
	//dependencia para o Department
	private Department entity; //nome entity pq � a entidade relacionada a esse formul�rio
	
	//declara��o dos componentes da tela (dois TextField, dois botoes e um label de erro
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
	
	public void setDepartment(Department entity) {
		this.entity = entity; //agora o DepartmentFormController possui uma instancia de Department
	}
	
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
		
		Constraints.setTextFieldInteger(txtId); //setando para que a TextField s� aceite numeros inteiros
		Constraints.setTextFieldMaxLength(txtName, 30); //setando para que a TextField s� aceite no m�ximo 30 caracteres
	}
	
	//m�todo respons�vel por pegar os dados de Department entity e popular as caixas de texto do formul�rio
	public void updateFormData() {
		
		if (entity == null) { //programa��o defensiva
			throw new IllegalStateException("Entity was null."); 
		}
		
		txtId.setText(String.valueOf(entity.getId())); //valueOf pq tem que convertar o valor da entidade (id) que � inteiro para String
		txtName.setText(entity.getName());
	}
}