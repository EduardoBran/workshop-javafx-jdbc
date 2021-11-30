package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{  //Classes controller são responsáveis por tratar os eventos/interações com os usuários
	
	//dependência do DepartmentService
	private DepartmentService service;
	
	//referencias para os nosso componentes da tela DepartmentList (botão, tabela e colunas)
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId; //integer pq é Id
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {   //método do tratamento de eventos do clico do botão btNew
		Stage parentStage = Utils.palcoAtual(event);
		createDialogForm("/gui/DepartmentForm.fxml", parentStage);
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
		List<Department> list = service.findAll(); //recupera os departamentos do serviço
		obsList = FXCollections.observableList(list);  //carregando a lista dentro do ObservableList
		tableViewDepartment.setItems(obsList);		   //atualiza conteudo da tabela
	}
	
	//funçao para carregar a janela do formulario e preencher um novo departamento.
	private void createDialogForm(String absoluteName, Stage parentStage) {
		
		//instanciando a janeia de dialogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); //para poder deixar a janela ser redimensionada
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL); //deixa a janela modal (travada), enquanto nao fechar esta janela, nao pode acessar as outras.
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
