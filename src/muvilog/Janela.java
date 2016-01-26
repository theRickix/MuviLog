package muvilog;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class Janela extends JFrame {
	
	Janela() throws IOException {
		configJanela();
		atualizarJanela();
	}
	

	void atualizarJanela() throws IOException {
		getContentPane().removeAll(); //Remover todos os componentes para poder atualizar
		setLayout(new GridLayout(1,1));
		menuBar();
		
		atualizarFilmes();
		setResizable(true);
		setVisible(true);
	}
	
	void atualizarFilmes() throws IOException {
		JPanel painel = new JPanel();
		final ArrayList<Filme> filmes = new ArrayList<Filme>();
		
		new LigacaoBD().listarFilmes(filmes);
		
		//Verificar se tem ou não filmes
		if (filmes.isEmpty()) {
			JLabel jl1 = new JLabel("<html><center>Nenhum filme encontrado!<br>Comece a adicionar a sua coleção.</center></html>");
			jl1.setHorizontalAlignment(SwingConstants.CENTER);
			painel.add(jl1);
			add(painel);
		}
		else {
			//Contar linhas de filmes (com 3 unidades cada)
			double linhas = (filmes.size())/3d; //O d indica que o 3 é double
			System.out.println(linhas);
			if (!(linhas%1==0)) {
				linhas++;
			}
			painel.setLayout(new GridLayout((int)(linhas),3)); //Definir linhas e colunas
			for(int i=0;i<(filmes.size());i++) {
				final int j = i;
				JLabel posterfilme = new JLabel(new ImageIcon(filmes.get(i).poster)); //Receber poster
				posterfilme.addMouseListener(new MouseAdapter() { //Ação de clicar no poster
					public void mouseClicked(MouseEvent e) {
						new JanelaAdicional().verFilme(filmes.get(j));
					}
				});
				painel.add(posterfilme);
			}
			
			JScrollPane scroll = new JScrollPane(painel);
			add(scroll);
		}
		
	}
	
	void configJanela() {
		setTitle("MuviLog");
		setSize(1000,700);
		setMinimumSize(new Dimension(1000, 400));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	void menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("Adicionar");
		JMenu menu2 = new JMenu("Listar");
		JMenuItem item1 = new JMenuItem("Adicionar filme");
		JMenuItem item2 = new JMenuItem("Adicionar armário");
		JMenuItem item3 = new JMenuItem("Listar visualizações");
		JMenuItem item4 = new JMenuItem("Listar armários");
		JMenuItem item5 = new JMenuItem("Listar empréstimos");
		menu1.add(item1);
		menu1.add(item2);
		menu2.add(item3);
		menu2.add(item4);
		menu2.add(item5);
		menuBar.add(menu1);
		menuBar.add(menu2);
		setJMenuBar(menuBar);
		
		
		//Definição de ações para cada ítem
		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new JanelaAdicional().adicionarFilme();
			}
		});
		
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new JanelaAdicional().adicionarArmario();
			}
		});
		
		item3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new JanelaAdicional().verVisualizacoes();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		item4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new JanelaAdicional().verPrateleiras();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		item5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new JanelaAdicional().verEmprestimos();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
	}
}
