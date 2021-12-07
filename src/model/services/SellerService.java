package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService { //� uma classe de servi�o respons�vel por nos fornecer servi�os(opera��es) relacionadas a Seller	
	
	private SellerDao dao = DaoFactory.createSellerDao(); //classe DaoFactory, m�todo createSellerDao , conex�o com o BD
	
	public List<Seller> findAll(){ //chamada no m�todo updateView() da classe SellerListController
		
		return dao.findAll();		
	}
	
	//m�todo para inserir ou atualizar um Departamente no banco de dados
	public void saveOrUpdate(Seller obj) { //chamado no m�todo onBtSaveAction(ActionEvent event) da classe SellerFormController
		
		if (obj.getId() == null) { //se o Id for nulo, ele insere. Se o ID ja for existente, ele atualiza
			
			dao.insert(obj);
		}
		else {
			
			dao.update(obj);
		}
	}
	
	public void remove(Seller obj) {
		
		dao.deleteById(obj.getId());
	}
}
//para recuperar os valores do BD
//1- declarar uma dependencia
//2- injetou a dependencia usando o padr�o Factory