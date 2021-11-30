package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService { //� uma classe de servi�o respons�vel por nos fornecer servi�os(opera��es) relacionadas a Department	
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao(); //classe DaoFactory, m�todo createDepartmentDao
	
	public List<Department> findAll(){
		
		return dao.findAll();		
	}
}
//para recuperar os valores do BD
//1- declarar uma dependencia
//2- injetou a dependencia usando o padr�o factory