package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{ //exceção personalizada que vai carregar uma coleção (Map) conteando todos os erros possíveis

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> erros = new HashMap<>(); //1º String indica o nome do campo ( nome, email ) e o 2º indica a msg de erro
	
	public ValidationException(String msg) {
		super(msg);
	}

	public Map<String, String> getErros() {
		return erros;
	}

	public void addError(String nomeDoCampo, String errorMessage) {		
		erros.put(nomeDoCampo, errorMessage);
	}
	
}
// É uma exceção para validar um formulário. 
// Em cada campo no formulário eu posso ter um tipo de erro especifico
// Para carregar esses erros na exceção eu declaro um Map