package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{  //Classes controller s�o respons�veis por tratar os eventos/intera��es com os usu�rios
	
	//depend�ncia do DepartmentService
	private DepartmentService service;
	
	//referencias para os nosso componentes da tela DepartmentList (bot�o, tabela e colunas)
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId; //integer pq � Id
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction() {           //m�todo do tratamento de eventos do clico do bot�o btNew
		System.out.println("onBtNewAction");
	}
	
	//injetando a depend�ncia
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes(); //m�todo auxiliar para iniciar componentes da minha tela		
	}


	private void initializeNodes() {
		
		//comando para iniciar apropriadamente o comportamento das minhas colunas da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); //id da classe Department
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//c�digo para que a tablea acompanhe o tamanho da tela do projeto
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	//m�todo respons�vel por acessar o servi�o, carregar os departamentos e jogar na ObservableList
	public void updateTableView() { //chamado em MainViewController
		
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); //recupera os departamentos do servi�o
		obsList = FXCollections.observableList(list);  //carregando a lista dentro do ObservableList
		tableViewDepartment.setItems(obsList);		
	}
}
