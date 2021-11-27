package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DeparmentListController implements Initializable{
	
	//referencias para os nosso componentes da tela DepartmentList (botão, tabela e colunas)
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId; //integer pq é Id
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	//método do tratamento de eventos do clico do botão btNew
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes(); //método auxiliar para iniciar componentes da minha tela		
	}


	private void initializeNodes() {
		
		//comando para iniciar apropriadamente o comportamento das minhas colunas da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); //id da classe Department
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//código para que a tablea acompanhe o tamanho da tela do projeto
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
