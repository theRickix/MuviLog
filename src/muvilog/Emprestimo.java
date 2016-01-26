package muvilog;

public class Emprestimo {
	int id;
	String filme;
	String tipo;
	String pessoa;
	String dataemp;
	String dataret;
	
	
	Emprestimo() {
	
	}


	public Emprestimo(int id, String pessoa, String dataemp, String dataret) {
		super();
		this.id = id;
		this.pessoa = pessoa;
		this.dataemp = dataemp;
		this.dataret = dataret;
	}
	
	

}
