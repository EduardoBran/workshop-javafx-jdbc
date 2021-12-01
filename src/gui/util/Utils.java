package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils { //Esta classe tem a fun��o de retornar o stage(palco) atual.
	
	public static Stage palcoAtual(ActionEvent event){
	
		//implementa��o padr�o para pegar o Stage a partir do objeto de evento (event)
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}	
}

// Est� m�todo � para eu acessar o Palco (Stage) daonde o meu
// controle que recebeu o evento (ActionEvent event) est�.
// Se eu clico no bot�o eu vou pegar o Palco(Stage daquele bot�o)