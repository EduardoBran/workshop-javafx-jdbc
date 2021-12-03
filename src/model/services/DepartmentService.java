package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService { //É uma classe de serviço responsável por nos fornecer serviços(operações) relacionadas a Department	
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao(); //classe DaoFactory, método createDepartmentDao , conexão com o BD
	
	public List<Department> findAll(){ //chamada no método updateView() da classe DepartmentListController
		
		return dao.findAll();		
	}
	
	//método para inserir um Departamente no banco ou atualizar um existente
	public void saveOrUpdate(Department obj) { //chamado no método 
		
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
//2- injetou a dependencia usando o padrão Factory