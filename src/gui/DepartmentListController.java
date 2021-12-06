package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{  //Classes controller são responsáveis por tratar os eventos/interações com os usuários
	
	//dependência do DepartmentService
	private DepartmentService service;
	
	private ObservableList<Department> obsList;
	
	//referencias para os nosso componentes da tela DepartmentList (botão, tabela e colunas)
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId; //integer pq é Id
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;
	
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	//método do tratamento de eventos do clico do botão btNew
	@FXML
	public void onBtNewAction(ActionEvent event) {   
		Stage parentStage = Utils.palcoAtual(event); //método palcoAtual da classe Utils
		Department obj = new Department(); //instanciando um departamento vazio
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	//injetando a dependência
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes(); //método auxiliar para iniciar componentes da minha tela		
	}

	private void initializeNodes() {
		
		//comando para iniciar apropriadamente o comportamento das minhas colunas da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); //id da classe Department (nome da tabela)
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//código para que a tablea acompanhe o tamanho da tela do projeto
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	//método responsável por acessar o serviço, carregar os departamentos e jogar na ObservableList
	public void updateTableView() { //chamado em MainViewController
		
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); //recupera os departamentos do serviço - método findAll() da classe DepartmentService
		obsList = FXCollections.observableList(list);  //carregando a lista dentro do ObservableList
		tableViewDepartment.setItems(obsList);		   //atualiza conteudo da tabela
		
		initEditButtons(); //acrescenta um novo botão Edit em cada linha da tabela
		initRemoveButtons();
	}
	
	//funçao para carregar a janela do formulario de preenchimento de um novo departamento.
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) { //chamanda na função do botão de novo departamento
		
		//Lógica pra abrir janela de formulário PALCO
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			//injetando o obj na tela de DepartmentForm
			DepartmentFormController controller =  loader.getController();//pegando a referencia do Controlador
			controller.setDepartment(obj); //injetando o obj no controlador
			controller.setDepartmentService(new DepartmentService()); //injeção de dependência
			controller.subscribeDataChangeListener(this); //estou me inscrevendo para receber o evento , método da classe DepartmentFormController
			controller.updateFormData();   //carregando os dados de ' setDepartment(obj) ' no formulario
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane)); //setScene - escolhendo quem vai ser a cena. (New Scene(pane) - criando a nova cena pq como é um noto Stage tba vai ser uma nova cena(pane)
			dialogStage.setResizable(false); //para poder deixar a janela ser redimensionada
			dialogStage.initOwner(parentStage); //parenteStage é o pai dessa janela por isso é passada essa informação no método
			dialogStage.initModality(Modality.WINDOW_MODAL); //deixa a janela modal (travada), enquanto nao fechar esta janela, nao pode acessar as outras.
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() { //na hora que disparar que os eventos foram alterados, eu chamo a função updateTableView
		
		updateTableView();		
	}
	
	private void initEditButtons() { //chamado em updateTableView()
		
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			
			private final Button button = new Button("edit");

			@Override //opicional (?)
			protected void updateItem(Department obj, boolean empty) {
				
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.palcoAtual(event)));
			}
		});
	}
	
	private void initRemoveButtons() { //chamado em updateTableView()
		
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) {
		
		//lógica para remoção de departamento
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) { //para acessar o objeto dentro do Option precisa do .get
			
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();				
			}
			catch (DbIntegrityException e){
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
		
	}
}
