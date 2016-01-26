package muvilog;

import java.io.IOException;
import java.sql.Connection;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class VideotecaPessoal {
	static Janela janela; //Janela global para poder ser atualizada

	public static void main(String[] args) throws IOException {
		//Mudar L&F da interface
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
		}
		catch (ClassNotFoundException e) {
		}
		catch (InstantiationException e) {
		}
		catch (IllegalAccessException e) {
		}
		Connection con = new LigacaoBD().obterLigacao(); //Obter ligação à BD
		new LigacaoBD().criarTabelas(con); //Criar tabelas
		new LigacaoBD().popularTabelas(con); //Popular tabelas
		janela=new Janela(); //Iniciar janela
	}

}
