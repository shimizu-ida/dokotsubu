package model;

public class Mutter {

	private int id;
	private String userName;
	private String text;
	
	public Mutter() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public Mutter(String userName, String text) {
		super();
		this.userName = userName;
		this.text = text;
	}

	public Mutter(int id, String userName, String text) {
		super();
		this.id = id;
		this.userName = userName;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getText() {
		return text;
	}
	
}
