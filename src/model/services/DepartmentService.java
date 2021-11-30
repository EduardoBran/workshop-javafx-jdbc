package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService { //É uma classe de serviço responsável por nos fornecer serviços(operações) relacionadas a Department	
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao(); //classe DaoFactory, método createDepartmentDao
	
	public List<Department> findAll(){
		
		return dao.findAll();		
	}
}
//para recuperar os valores do BD
//1- declarar uma dependencia
//2- injetou a dependencia usando o padrão factory