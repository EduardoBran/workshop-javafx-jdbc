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
	private Department entity; //nome entity pq é a entidade relacionada a esse formulário
	
	//dependencia para o DepartmentService
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); //permite que objetos se inscrevam nesta List para receber o evento
		
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
	
	public void setDepartment(Department entity) {
		this.entity = entity; //agora o DepartmentFormController possui uma instancia de Department
	}
	
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	//método para inscrever o ' listener ' na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		
		if(entity == null) {//programação defensiva
			throw new IllegalStateException("Entity was null.");
		}
		if(departmentService == null) {//programação defensiva
			throw new IllegalStateException("Service was null.");
		}		
		
		//código para salvar nosso departamento no Banco de Dados. A partir do dado preenchido na tela (no caso só o nome do Departamento) eu vou ter que instanciar um Department e salvar no BD
		try {
			
			if (txtName.getText() == null) { //se o nome estiver vazio
				Utils.palcoAtual(event).close();
			}else {
				entity = getFormData(); //responsável por pegar os dados das caixinhas do formulario e instanciar um Departamento
				departmentService.saveOrUpdate(entity); //salvou no BD
				notifyDataChangeListeners(); //método para notificar os listeners
			}
			//fechando a janela dps que salvou
			Utils.palcoAtual(event).close(); //pegando a referência da janela atual (que é a jaanela do formulário) por isso foi acrescentando o (ActionEvent event) no método
		}
		catch (DbException e){
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	//método para notificar a emissão do evento
	private void notifyDataChangeListeners() {
		
		for (DataChangeListener listener : dataChangeListeners) { //para cada DataChangeListener listener pertencente a minha lista dataChangeListeners
			listener.onDataChanged();
		}
	}
	
	//método responsável por instanciar um novo Department, pegar os dados do formulario e retorna um novo objeto Department com esses dados.
	private Department getFormData() { 
		
		Department obj = new Department(); //criou(instanciou) um objeto vazio
		obj.setId(Utils.tryParseToInt(txtId.getText())); //id do objeto Department vai ser o id que estvier preenchido na caixinha. Chamada a função da classe Utils para converter para inteiro
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
		
		Constraints.setTextFieldInteger(txtId); //setando para que a TextField só aceite numeros inteiros
		Constraints.setTextFieldMaxLength(txtName, 30); //setando para que a TextField só aceite no máximo 30 caracteres
	}
	
	//método responsável por pegar os dados de Department entity e popular as caixas de texto do formulário
	public void updateFormData() { //chamado no método createDialogForm() da clase DepartmentListController
		
		if (entity == null) { //programação defensiva
			throw new IllegalStateException("Entity was null."); 
		}
		
		txtId.setText(String.valueOf(entity.getId())); //valueOf pq tem que convertar o valor da entidade (id) que é inteiro para String
		txtName.setText(entity.getName());
	}
}