package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	//dependencia para o Seller
	private Seller entity; //nome entity pq ? a entidade relacionada a esse formul?rio
	
	//dependencia para o SellerService
	private SellerService sellerService;
	
	//dependencia para DepartmentService
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); //permite que objetos se inscrevam nesta List para receber o evento
		
	//declara??o dos componentes da tela (dois TextField, dois botoes e um label de erro
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
	private ComboBox<Department> comboBoxDepartment;
	
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
	
	private ObservableList<Department> obsList;
	
	public void setSeller(Seller entity) {
		this.entity = entity; //agora o SellerFormController possui uma instancia de Seller
	}
	
	public void setServices(SellerService sellerService, DepartmentService departmentService) {
		this.sellerService = sellerService;
		this.departmentService = departmentService;
	}
	
	
	//m?todo para inscrever o ' listener ' na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		
		if(entity == null) {//programa??o defensiva
			throw new IllegalStateException("Entity was null.");
		}
		if(sellerService == null) {//programa??o defensiva
			throw new IllegalStateException("Service was null.");
		}		
		
		//c?digo para salvar nosso departamento no Banco de Dados. A partir do dado preenchido na tela (no caso s? o nome do Departamento) eu vou ter que instanciar um Seller e salvar no BD
		try {			
			
			entity = getFormData(); //respons?vel por pegar os dados das caixinhas do formulario e instanciar um Departamento
			sellerService.saveOrUpdate(entity); //salvou no BD
			notifyDataChangeListeners(); //m?todo para notificar os listeners
			
			//fechando a janela dps que salvou
			Utils.palcoAtual(event).close(); //pegando a refer?ncia da janela atual (que ? a jaanela do formul?rio) por isso foi acrescentando o (ActionEvent event) no m?todo
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		
		catch (DbException e){
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	//m?todo para notificar a emiss?o do evento
	private void notifyDataChangeListeners() {
		
		for (DataChangeListener listener : dataChangeListeners) { //para cada DataChangeListener listener pertencente a minha lista dataChangeListeners
			listener.onDataChanged();
		}
	}
	
	//m?todo respons?vel por instanciar um novo Seller, pegar os dados do formulario e retorna um novo objeto Seller com esses dados.
	private Seller getFormData() { //chamado em onBtSaveAction()
		
		Seller obj = new Seller(); //criou(instanciou) um objeto vazio		
		ValidationException exception = new ValidationException("Validation error."); //exce??o instanciada
		
		obj.setId(Utils.tryParseToInt(txtId.getText())); //id do objeto Seller vai ser o id que estvier preenchido na caixinha. Chamada a fun??o da classe Utils para converter para inteiro
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) { //trim() ? pra eliminar qualquer espa?o em branco no inicio ou no final
			exception.addError("name", "  Field can't be empty.");
		}		
		obj.setName(txtName.getText());
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) { //trim() ? pra eliminar qualquer espa?o em branco no inicio ou no final
			exception.addError("email", "  Field can't be empty.");
		}
		obj.setEmail(txtEmail.getText());
		
		if (dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "  Field can't be empty.");
		}
		else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault())); //recebe o conteudo do DatePicker , atStartOfDay converte o horario da maquina do usuario para o horario padr?o de Instant
			obj.setBirthDate(Date.from(instant));  //converter Instant para Date
		}	
		
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) { //trim() ? pra eliminar qualquer espa?o em branco no inicio ou no final
			exception.addError("baseSalary", "  Field can't be empty.");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		obj.setDepartment(comboBoxDepartment.getValue()); //pegando o valor do Departamento via comboBox
		
		if(exception.getErros().size() > 0) { //teste para ver se a cole??o de erros tem pelo menos 1 erro
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
		
		Constraints.setTextFieldInteger(txtId); //setando para que a TextField s? aceite numeros inteiros
		Constraints.setTextFieldMaxLength(txtName, 70); //setando para que a TextField s? aceite no m?ximo 30 caracteres
		Constraints.setTextFieldDouble(txtBaseSalary); //definindo que o campo(textfield) do baseSalary ? do tipo Double
		Constraints.setTextFieldMaxLength(txtEmail, 60); //definindo tamanho m?ximo do email
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy"); //setando a Data para o formato DatePicker
		
		initializeComboBoxDepartment();
	}
	
	//m?todo respons?vel por pegar os dados de Seller entity e popular as caixas de texto do formul?rio
	public void updateFormData() { //chamado no m?todo createDialogForm() da clase SellerListController
		
		if (entity == null) { //programa??o defensiva
			throw new IllegalStateException("Entity was null."); 
		}		
		txtId.setText(String.valueOf(entity.getId())); //valueOf pq tem que convertar o valor da entidade (id) que ? inteiro para String
		txtName.setText(entity.getName());
		
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); //ZoneId pega o fuso do pc da pessoa que usao sistema
		}
		
		if (entity.getDepartment() == null) { //se o departamento for igual a nulo, significa que ? um vendedor novo que estou cadastrando
			
			comboBoxDepartment.getSelectionModel().selectFirst(); //definir para que o comboBox esteja selecionado no primeiro elemento
		}
		else {
			comboBoxDepartment.setValue(entity.getDepartment());//preencher o ComboBox
		}	
	}
	
	//m?todo respons?vel por carregar os departamentos do banco de dados e preenchendo a lista com estes departamentos
	public void loadAssociatedObjects() { //chamado no createDialogForm (SellerListController)
		
		if (departmentService == null) {
			throw new IllegalStateException("Department was null.");
		}
		
		List<Department> list = departmentService.findAll(); //carregou os departamentos do banco de dados
		obsList = FXCollections.observableArrayList(list); //jogando os departamentos para a ObservableList
		comboBoxDepartment.setItems(obsList); //setando a lista como a lista associada ao ComboBox		
	} 	
	
	//m?todo respons?vel por pegar os erros da exce??o e escrever no Label sem nome
	private void setErrorMessages(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet(); //conjunto de errors
		
//		if(fields.contains("name")) { //se no conjunto de errors exciste a chave 'name' (que foi criada no m?todo getFormData() atrav?s do exception.addError			
//			labelErrorName.setText(errors.get("name"));			
//		}
//		else {
//			labelErrorName.setText(""); //Caso o label tenha apontado algum erro, a msg ficar? escrita. Quando ajeitarmos a msg sera apagada
//		}
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : "")); //operador tern?rio se fields.contains("name") for verdadeira ( ? ) entao errror.get("name") SE NAO ( : ) ""
		
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		
		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
	}
	
	private void initializeComboBoxDepartment() { //chamado no initizalizeNods()
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
