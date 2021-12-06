package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{ //exce��o personalizada que vai carregar uma cole��o (Map) conteando todos os erros poss�veis

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> erros = new HashMap<>(); //1� String indica o nome do campo ( nome, email ) e o 2� indica a msg de erro
	
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
// � uma exce��o para validar um formul�rio. 
// Em cada campo no formul�rio eu posso ter um tipo de erro especifico
// Para carregar esses erros na exce��o eu declaro um Map