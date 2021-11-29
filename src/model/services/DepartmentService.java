package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService { //É uma classe de serviço responsável por nos fornecer serviços(operações) relacionadas a Department
	
	public List<Department> findAll(){
		
		//MOCK
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Eletronics"));
		
		return list;		
	}
}
