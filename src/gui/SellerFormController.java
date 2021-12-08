package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	//dependencia para o Seller
	private Seller entity; //nome entity pq é a entidade relacionada a esse formulário
	
	//dependencia para o SellerService
	private SellerService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); //permite que objetos se inscrevam nesta List para receber o evento
		
	//declaração dos componentes da tela (dois TextField, dois botoes e um label de erro
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	public void setSeller(Seller entity) {
		this.entity = entity; //agora o SellerFormController possui uma instancia de Seller
	}
	
	public void setSellerService(SellerService departmentService) {
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
		
		//código para salvar nosso departamento no Banco de Dados. A partir do dado preenchido na tela (no caso só o nome do Departamento) eu vou ter que instanciar um Seller e salvar no BD
		try {			
			
			entity = getFormData(); //responsável por pegar os dados das caixinhas do formulario e instanciar um Departamento
			departmentService.saveOrUpdate(entity); //salvou no BD
			notifyDataChangeListeners(); //método para notificar os listeners
			
			//fechando a janela dps que salvou
			Utils.palcoAtual(event).close(); //pegando a referência da janela atual (que é a jaanela do formulário) por isso foi acrescentando o (ActionEvent event) no método
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
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
	
	//método responsável por instanciar um novo Seller, pegar os dados do formulario e retorna um novo objeto Seller com esses dados.
	private Seller getFormData() { 
		
		Seller obj = new Seller(); //criou(instanciou) um objeto vazio
		
		ValidationException exception = new ValidationException("Validation error."); //exceção instanciada
		
		obj.setId(Utils.tryParseToInt(txtId.getText())); //id do objeto Seller vai ser o id que estvier preenchido na caixinha. Chamada a função da classe Utils para converter para inteiro
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) { //trim() é pra eliminar qualquer espaço em branco no inicio ou no final
			exception.addError("name", "  Field can't be empty.");
		}
		
		obj.setName(txtName.getText());
		
		if(exception.getErros().size() > 0) { //teste para ver se a coleção de erros tem pelo menos 1 erro
			throw exception;
		}	
			
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
		Constraints.setTextFieldMaxLength(txtName, 70); //setando para que a TextField só aceite no máximo 30 caracteres
		Constraints.setTextFieldDouble(txtBaseSalary); //definindo que o campo(textfield) do baseSalary é do tipo Double
		Constraints.setTextFieldMaxLength(txtEmail, 60); //definindo tamanho máximo do email
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy"); //setando a Data para o formato DatePicker
	}
	
	//método responsável por pegar os dados de Seller entity e popular as caixas de texto do formulário
	public void updateFormData() { //chamado no método createDialogForm() da clase SellerListController
		
		if (entity == null) { //programação defensiva
			throw new IllegalStateException("Entity was null."); 
		}		
		txtId.setText(String.valueOf(entity.getId())); //valueOf pq tem que convertar o valor da entidade (id) que é inteiro para String
		txtName.setText(entity.getName());
		
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); //ZoneId pega o fuso do pc da pessoa que usao sistema
		}
	}
	
	//método responsável por pegar os erros da exceção e escrever no Label sem nome
	private void setErrorMessages(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet(); //conjunto de errors
		
		if(fields.contains("name")) { //se no conjunto de errors exciste a chave 'name'
			
			labelErrorName.setText(errors.get("name"));			
		}	
	}
}
