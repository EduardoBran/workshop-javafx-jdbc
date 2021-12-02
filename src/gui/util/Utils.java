package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils { //Esta classe tem a função de retornar o stage(palco) atual.
	
	public static Stage palcoAtual(ActionEvent event){
	
		//implementação padrão para pegar o Stage a partir do objeto de evento (event)
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}	
	
	//método para converter o valor da caixinha pra inteiro
	public static Integer tryParseToInt(String str) { 
		
		try { //tentar fazer a conversão
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}	
}

// Esté método palcoAtual() é para eu acessar o Palco (Stage) daonde o meu
// controle que recebeu o evento (ActionEvent event) está.
// Se eu clico no botão eu vou pegar o Palco(Stage daquele botão)