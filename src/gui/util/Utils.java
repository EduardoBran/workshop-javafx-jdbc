package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class Utils { 
	
	//método cujo a função é retornar o stage(palco) atual.
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
	
	//formatar a data numa listagem de objetos
	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			return cell;
		});
	}

	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}
}

// Esté método palcoAtual() é para eu acessar o Palco (Stage) daonde o meu
// controle que recebeu o evento (ActionEvent event) está.
// Se eu clico no botão eu vou pegar o Palco(Stage daquele botão)