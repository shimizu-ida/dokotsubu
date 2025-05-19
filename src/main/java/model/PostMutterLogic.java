package model;

import java.util.List;

import dao.MutterDAO;

public class PostMutterLogic {

	public void execute(Mutter m, List<Mutter> list) {
		list.add(0, m);
	}
	
	public void execute(Mutter m) {
		new MutterDAO().create(m);
	}
}
