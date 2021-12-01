package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {  //Classes controller s�o respons�veis por tratar os eventos/intera��es com os usu�rios
	
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	//m�todos para tratar cada uma das a�oes do Menu
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() { //na hora de passar a chamanda de loadView, foi incluso um segundo par�mentro com uma fun��o para inicializar o controlador
		loadView("/gui/DepartmentList.fxml" , (DepartmentListController controller) -> { //fun��o do tipo DepartmentListController chamada de controller, setinha da expressao lambida, dps no corpo da fun��o � chamada a inicizaliza��o do controle  
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView(); //m�todo updateTableVIew da classe DepartmentViewController
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {		
		loadView("/gui/About.fxml" , x -> {}); //a��o de inicializa��o vazia pq nao tem nada
	}	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	//fun��o para abrir outra tela VBOX //synchrinized garante que o proceso nao sera interrompido
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) { 
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load(); //carregamos a view enviada (absuluteName)
			
			Scene mainScene = Main.getMainScene(); //pegando a referencia de Scene da classe Main
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0); 
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController(); // comando para ativar a fun��o passada (initializingAction)
			initializingAction.accept(controller); // executando a fun��o
		}
		catch(IOException e){
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
