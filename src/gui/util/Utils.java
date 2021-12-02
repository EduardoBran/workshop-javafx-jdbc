package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils { //Esta classe tem a fun��o de retornar o stage(palco) atual.
	
	public static Stage palcoAtual(ActionEvent event){
	
		//implementa��o padr�o para pegar o Stage a partir do objeto de evento (event)
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}	
	
	//m�todo para converter o valor da caixinha pra inteiro
	public static Integer tryParseToInt(String str) { 
		
		try { //tentar fazer a convers�o
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}	
}

// Est� m�todo palcoAtual() � para eu acessar o Palco (Stage) daonde o meu
// controle que recebeu o evento (ActionEvent event) est�.
// Se eu clico no bot�o eu vou pegar o Palco(Stage daquele bot�o)