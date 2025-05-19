package model;

import java.util.List;

import dao.MutterDAO;

public class GetMutterListLogic {

	public List<Mutter> execute(){
		return new MutterDAO().findAll();
	}
}
