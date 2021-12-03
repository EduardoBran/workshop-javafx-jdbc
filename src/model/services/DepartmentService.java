package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService { //� uma classe de servi�o respons�vel por nos fornecer servi�os(opera��es) relacionadas a Department	
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao(); //classe DaoFactory, m�todo createDepartmentDao , conex�o com o BD
	
	public List<Department> findAll(){ //chamada no m�todo updateView() da classe DepartmentListController
		
		return dao.findAll();		
	}
	
	//m�todo para inserir um Departamente no banco ou atualizar um existente
	public void saveOrUpdate(Department obj) { //chamado no m�todo 
		
		if (obj.getId() == null) { //se o Id for nulo, ele insere. Se o ID ja for existente, ele atualiza
			
			dao.insert(obj);
		}
		else {
			
			dao.update(obj);
		}
	}
}
//para recuperar os valores do BD
//1- declarar uma dependencia
//2- injetou a dependencia usando o padr�o Factory