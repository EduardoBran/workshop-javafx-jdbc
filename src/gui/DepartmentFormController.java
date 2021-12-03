package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	//dependencia para o DepartmentService
	private Department entity; //nome entity pq � a entidade relacionada a esse formul�rio
	
	//dependencia para o DepartmentService
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); //permite que objetos se inscrevam nesta List para receber o evento
		
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
	
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	//m�todo para inscrever o ' listener ' na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		
		if(entity == null) {//programa��o defensiva
			throw new IllegalStateException("Entity was null.");
		}
		if(departmentService == null) {//programa��o defensiva
			throw new IllegalStateException("Service was null.");
		}		
		
		//c�digo para salvar nosso departamento no Banco de Dados. A partir do dado preenchido na tela (no caso s� o nome do Departamento) eu vou ter que instanciar um Department e salvar no BD
		try {
			
			if (txtName.getText() == null) { //se o nome estiver vazio
				Utils.palcoAtual(event).close();
			}else {
				entity = getFormData(); //respons�vel por pegar os dados das caixinhas do formulario e instanciar um Departamento
				departmentService.saveOrUpdate(entity); //salvou no BD
				notifyDataChangeListeners(); //m�todo para notificar os listeners
			}
			//fechando a janela dps que salvou
			Utils.palcoAtual(event).close(); //pegando a refer�ncia da janela atual (que � a jaanela do formul�rio) por isso foi acrescentando o (ActionEvent event) no m�todo
		}
		catch (DbException e){
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	//m�todo para notificar a emiss�o do evento
	private void notifyDataChangeListeners() {
		
		for (DataChangeListener listener : dataChangeListeners) { //para cada DataChangeListener listener pertencente a minha lista dataChangeListeners
			listener.onDataChanged();
		}
	}
	
	//m�todo respons�vel por instanciar um novo Department, pegar os dados do formulario e retorna um novo objeto Department com esses dados.
	private Department getFormData() { 
		
		Department obj = new Department(); //criou(instanciou) um objeto vazio
		obj.setId(Utils.tryParseToInt(txtId.getText())); //id do objeto Department vai ser o id que estvier preenchido na caixinha. Chamada a fun��o da classe Utils para converter para inteiro
		obj.setName(txtName.getText());
		
		return obj;		
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		
		Utils.palcoAtual(event).close();
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
	public void updateFormData() { //chamado no m�todo createDialogForm() da clase DepartmentListController
		
		if (entity == null) { //programa��o defensiva
			throw new IllegalStateException("Entity was null."); 
		}
		
		txtId.setText(String.valueOf(entity.getId())); //valueOf pq tem que convertar o valor da entidade (id) que � inteiro para String
		txtName.setText(entity.getName());
	}
}