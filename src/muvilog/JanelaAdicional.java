package muvilog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


public class JanelaAdicional extends JFrame {
	
	void fecharJanela() {
		super.dispose();
	}
	
	void adicionarFilme() { //Janela de adicionar filme
		setTitle("Adicionar filme");
		setSize(200,120);
		setLayout(new FlowLayout());
		JLabel jl1 = new JLabel("Título do filme:");
		final JTextField txt1 = new JTextField(10);
		JLabel jl2 = new JLabel("Ano (opcional):");
		final JTextField txt2 = new JTextField(4);
		
		JButton bt1 = new JButton("Adicionar");
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Filme filme = new Filme();
					Boolean bool = filme.receberFilme(txt1.getText(),txt2.getText());
					
					if(bool==true) { //Caso exista no IMDb
						bool=new LigacaoBD().verificarFilme(filme);
						if(bool==false) { //Caso não exista na BD
							new LigacaoBD().insertFilme(filme);
							JOptionPane.showMessageDialog(null,"Filme adicionado");
							VideotecaPessoal.janela.atualizarJanela();
						}
						else { //Caso exista na BD
							JOptionPane.showMessageDialog(null,"ERRO: Filme já existe.");
						}
					}
					else { //Caso não exista no IMDb
						JOptionPane.showMessageDialog(null,"ERRO: Filme inexistente");
					}
					fecharJanela();
					
					System.out.println(filme.id+filme.titulo+filme.imdbrating);
				} catch (IOException e1) {
				}
			}
		});
		
		add(jl1);
		add(txt1);
		add(jl2);
		add(txt2);
		add(bt1);
		
		
		setResizable(false);
		setVisible(true);
	}
	
	void verFilme(Filme filme) {
		setTitle(filme.titulo);
		setSize(400,320);
		setLayout(new BorderLayout());
		final int id = filme.id;
		
		JPanel painel = new JPanel();
		
		final Suporte[] suportes = new LigacaoBD().receberSuportes(filme.id);
		
		painel.setLayout(new BoxLayout(painel,BoxLayout.PAGE_AXIS));
		
		
		JPanel painel2 = new JPanel(new FlowLayout());
		painel2.setLayout(new BoxLayout(painel2,BoxLayout.PAGE_AXIS));
		
		JPanel painel3 = new JPanel();
		painel3.setLayout(new FlowLayout());
		
		JLabel tipo = new JLabel("<html><b>Tipo:&nbsp;&nbsp;</b> "+new LigacaoBD().receberTipo(filme.tipo)+"</html>");
		JLabel titulo = new JLabel("<html><b>Título:&nbsp;&nbsp;</b> "+filme.titulo+"</html>");
		JLabel ano = new JLabel("<html><b>Ano:&nbsp;&nbsp;</b> "+filme.ano+"</html>");
		JLabel duracao = new JLabel("<html><b>Duração:&nbsp;&nbsp;</b> "+filme.duracao+" min</html>");
		JLabel generos = new JLabel("<html><b>Géneros:&nbsp;&nbsp;</b> "+new LigacaoBD().receberGeneros(filme.id)+"</html>");
		JLabel paises = new JLabel("<html><b>Países:&nbsp;&nbsp;</b> "+new LigacaoBD().receberPaises(filme.id)+"</html>");
		JLabel realizador = new JLabel("<html><b>Realizador:&nbsp;&nbsp;</b> "+new LigacaoBD().receberRealizadores(filme.id)+"</html>");
		
		JLabel atores = new JLabel("<html><b>Atores:&nbsp;&nbsp;</b> "+new LigacaoBD().receberAtores(filme.id)+"</html>");
		JLabel metascore;
		if (filme.metascore==0) { //Se o metascore for 0, corresponde a N/A
			metascore = new JLabel("<html><b>Metascore:&nbsp;&nbsp;</b> N/A</html>");
		}
		else {
			metascore = new JLabel("<html><b>Metascore:&nbsp;&nbsp;</b> "+filme.metascore+"</html>");
		}
		JLabel imdbrating = new JLabel("<html><b>IMDb:&nbsp;&nbsp;</b> "+filme.imdbrating+"</html>");
		JLabel sinopse = new JLabel("<html><b>Sinopse:&nbsp;&nbsp;</b> "+filme.sinopse+"</html>");

		
		painel.add(tipo);
		painel.add(titulo);
		painel.add(ano);
		painel.add(duracao);
		painel.add(generos);
		painel.add(paises);
		if(filme.tipo==1) painel.add(realizador);
		painel.add(atores);
		painel.add(metascore);
		painel.add(imdbrating);
		painel.add(sinopse);
		add(painel,BorderLayout.NORTH);
		
		//Adicionar suportes do filme
		for (int i=0;i<suportes.length;i++){
			JPanel painelsuporte = new JPanel();
			painelsuporte.setLayout(new FlowLayout());
			JLabel nsuporte = new JLabel("#"+(i+1));
			JLabel tiposuporte = new JLabel(suportes[i].nome);
			JButton visualizar = new JButton("+1");
			JButton localizacao = new JButton("Localização");
			JButton emprestimo = new JButton("Empréstimo");
			JButton apagarsuporte = new JButton("X");
			painelsuporte.add(nsuporte);
			painelsuporte.add(tiposuporte);
			painelsuporte.add(visualizar);
			painelsuporte.add(localizacao);
			painelsuporte.add(emprestimo);
			painelsuporte.add(apagarsuporte);
			painel2.add(painelsuporte);
			
			final int j = i;
			
			if(suportes[j].nome.equals("Digital")) { //Se for digital, bloqueia a localização
				localizacao.setEnabled(false);      //E o empréstimo
				emprestimo.setEnabled(false);
			}
			//Adicionar ação de visualizar
			visualizar.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					new JanelaAdicional().adicionarVisualizacao(suportes[j].id);
					fecharJanela();
				}
			});
			
			if(localizacao.isEnabled()) { //Caso a localização esteja disponível
				localizacao.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) { //Ação da localização
						try {
							new JanelaAdicional().ligarLocalizacao(suportes[j].id);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						fecharJanela();
					}
				});
			}

			
			if(emprestimo.isEnabled()) { //Se empréstimo estiver disponível
				emprestimo.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) { //Ação do emprestimo
						new JanelaAdicional().adicionarEmprestimo(suportes[j].id);
						fecharJanela();
					}
				});
			}
			//Ação de apagar suporte
			apagarsuporte.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					try {
						new LigacaoBD().apagarSuporte(suportes[j].id);
						JOptionPane.showMessageDialog(null,"Suporte eliminado");
						fecharJanela();
						VideotecaPessoal.janela.atualizarJanela();
					} catch (IOException e1) {
					}
					fecharJanela();
				}
			});
		}
		
		add(painel2,BorderLayout.CENTER);
	
		JButton addsuporte = new JButton("Adicionar suporte");
		JButton delfilme = new JButton("Eliminar filme");
		painel3.add(addsuporte);
		painel3.add(delfilme);
		add(painel3,BorderLayout.SOUTH);
		
		JScrollPane scroll = new JScrollPane(painel2,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll);
		
		//Ação de apagar suporte
		addsuporte.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				new JanelaAdicional().adicionarSuporte(id);
				fecharJanela();
			}
		});
		//Ação de eliminar filme
		delfilme.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					new LigacaoBD().apagarFilme(id);
					JOptionPane.showMessageDialog(null,"Filme eliminado");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
				fecharJanela();
			}
		});
		
		
		setResizable(false);
		setVisible(true);
	}
	
	void verVisualizacoes() throws IOException {
		setTitle("Diário");
		setSize(400,320);
		setLayout(new BorderLayout());
		final ArrayList<Visualizacao> visualizacoes=new ArrayList<Visualizacao>();
		new LigacaoBD().listarVisualizacoes(visualizacoes);
		String[] colunas = {"Filme","Formato","Data"}; //Título das colunas
		DefaultTableModel tableModel = new DefaultTableModel(colunas, 0); //TableModel possibilita adicionar linhas
		final JTable tabela = new JTable(tableModel);                    //Sem especificar o número
		//Adicionar linha a linha
		for (int i=0;i<visualizacoes.size();i++) {
			String[] visualizacao = {visualizacoes.get(i).filme,visualizacoes.get(i).suporte,visualizacoes.get(i).data};
			tableModel.addRow(visualizacao);
		}
		
		JScrollPane scroll = new JScrollPane(tabela);
		add(scroll);
		//Tornar cada linha da tabela clicável
		tabela.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int linha = tabela.rowAtPoint(e.getPoint());
				new JanelaAdicional().alterarEliminarVisualizacao(visualizacoes.get(linha).id);
				fecharJanela();
			}
		});
		
		setVisible(true);
		
	}
	
	void verEmprestimos() throws IOException {
		setTitle("Empréstimos");
		setSize(400,320);
		setLayout(new BorderLayout());
		final ArrayList<Emprestimo> emprestimos=new ArrayList<Emprestimo>();
		new LigacaoBD().listarEmprestimos(emprestimos);
		String[] colunas = {"Filme","Suporte","Pessoa","Empréstimo","Retorno"};
		DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
		final JTable tabela = new JTable(tableModel);
		
		for (int i=0;i<emprestimos.size();i++) {
			String[] emprestimo = {emprestimos.get(i).filme,emprestimos.get(i).tipo,emprestimos.get(i).pessoa,emprestimos.get(i).dataemp,emprestimos.get(i).dataret};
			tableModel.addRow(emprestimo);
		}
		
		JScrollPane scroll = new JScrollPane(tabela);
		add(scroll);
		
		tabela.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int linha = tabela.rowAtPoint(e.getPoint());
				new JanelaAdicional().alterarEliminarEmprestimo(emprestimos.get(linha).id);
				fecharJanela();
			}
		});
		
		setVisible(true);
		
	}
	
	void verPrateleiras() throws IOException {
		setTitle("Localizações");
		setSize(230,360);
		setLayout(new GridLayout(1,1));
		ArrayList<Localizacao> localizacoes=new ArrayList<Localizacao>();
		new LigacaoBD().listarPrateleiras(localizacoes);
		
		String[] colunas = {"Filme","Suporte"};
		
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel,BoxLayout.PAGE_AXIS));
		
		
		//Criar diferentes tabelas para cada prateleira
		for (int i=0;i<localizacoes.size();i++) {
			DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
			final JTable tabela = new JTable(tableModel);
			JPanel painel = new JPanel();
			painel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                    localizacoes.get(i).armario+"-"+localizacoes.get(i).prateleira,
                    TitledBorder.CENTER,
                    TitledBorder.TOP)); //Adicionar borda e nome da prateleira
			Suporte[] suportes = new LigacaoBD().listarSuportesPrateleira(localizacoes.get(i).armario,localizacoes.get(i).prateleira);
			for (int j=0;j<suportes.length;j++) {
				String[] suporte = {suportes[j].filme,suportes[j].nome};
				tableModel.addRow(suporte);
			}
			painel.add(tabela);
			JScrollPane scroll = new JScrollPane(painel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			mainpanel.add(scroll);
			
		}
		JScrollPane scroll1 = new JScrollPane(mainpanel);
		add(scroll1);
		
		setResizable(false);
		setVisible(true);
		
	}

	protected void adicionarSuporte(final int idfilme) {
		setTitle("Adicionar suporte");
		setSize(220,100);
		setLayout(new FlowLayout());
		JLabel jl1 = new JLabel("Formato:");
		String[] formatos = {"VHS","DVD","Blu-Ray","Blu-Ray 3D","UMD","LaserDisc","Digital"}; //Elementos da combobox
		final JComboBox combo = new JComboBox(formatos);
		JButton bt1 = new JButton("Adicionar");
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new LigacaoBD().adicionarSuporte(idfilme,combo.getSelectedItem().toString());
					JOptionPane.showMessageDialog(null,"Suporte adicionado");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		add(jl1);
		add(combo);
		add(bt1);
		
		setResizable(false);
		setVisible(true);
		
	}
	
	protected void adicionarVisualizacao(final int idsuporte) {
		setTitle("Adicionar suporte");
		setSize(220,100);
		setLayout(new FlowLayout());
		JLabel jl1 = new JLabel("Data (DD-MM-AAAA):");
		final JTextField txt1 = new JTextField(2); //Dia
		final JTextField txt2 = new JTextField(2); //Mes
		final JTextField txt3 = new JTextField(4); //Ano
		
		
		JButton bt1 = new JButton("Adicionar");
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					final String data=txt3.getText()+"-"+txt2.getText()+"-"+txt1.getText();
					new LigacaoBD().adicionarVisualizacao(idsuporte,data);
					JOptionPane.showMessageDialog(null,"Visualizacão adicionado");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		add(jl1);
		add(txt1);
		add(txt2);
		add(txt3);
		add(bt1);
		
		setResizable(false);
		setVisible(true);
		
	}
	
	protected void adicionarArmario() {
		setTitle("Adicionar armário");
		setSize(220,100);
		setLayout(new FlowLayout());
		JLabel jl1 = new JLabel("Número de prateleiras:");
		final JTextField txt1 = new JTextField(3);
		
		JButton bt1 = new JButton("Adicionar");
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					final int nprateleiras=Integer.parseInt(txt1.getText());
					new LigacaoBD().adicionarArmario(nprateleiras);
					JOptionPane.showMessageDialog(null,"Armário e prateleiras criadas!");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		add(jl1);
		add(txt1);
		add(bt1);
		
		setResizable(false);
		setVisible(true);
		
	}
	
	protected void adicionarEmprestimo(final int idsuporte) {
		setTitle("Adicionar empréstimo");
		setSize(200,155);
		setLayout(new FlowLayout());
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel,BoxLayout.PAGE_AXIS));
		JLabel jl1 = new JLabel("Pessoa: ");
		final JTextField txt1 = new JTextField(10);
		JLabel jl2 = new JLabel("Data empréstimo:");
		final JTextField txt2 = new JTextField(2);
		final JTextField txt3 = new JTextField(2);
		final JTextField txt4 = new JTextField(4);
		JLabel jl3 = new JLabel("Data retorno:");
		final JTextField txt5 = new JTextField(2);
		final JTextField txt6 = new JTextField(2);
		final JTextField txt7 = new JTextField(4);
		
		JButton bt1 = new JButton("Adicionar");
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Concatenar datas para uma String única
					final String dataemp=txt4.getText()+"-"+txt3.getText()+"-"+txt2.getText();
					final String dataret=txt7.getText()+"-"+txt6.getText()+"-"+txt5.getText();
					new LigacaoBD().adicionarEmprestimo(idsuporte,txt1.getText(),dataemp,dataret);
					JOptionPane.showMessageDialog(null,"Empréstimo criado!");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		JPanel painel1 = new JPanel();
		painel1.add(jl1);
		painel1.add(txt1);
		JPanel painel2 = new JPanel();
		painel2.add(jl2);
		painel2.add(txt2);
		painel2.add(txt3);
		painel2.add(txt4);
		JPanel painel3 = new JPanel();
		painel3.add(jl3);
		painel3.add(txt5);
		painel3.add(txt6);
		painel3.add(txt7);
		JPanel painel4 = new JPanel();
		painel4.add(bt1);
		mainpanel.add(painel1);
		mainpanel.add(painel2);
		mainpanel.add(painel3);
		mainpanel.add(painel4);
		add(mainpanel);
		
		
		setResizable(false);
		setVisible(true);
		
	}
	
	protected void alterarEliminarEmprestimo(final int idemprestimo) {
		setTitle("Eliminar empréstimo");
		setSize(200,155);
		setLayout(new FlowLayout());
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel,BoxLayout.PAGE_AXIS));
		JLabel jl1 = new JLabel("Pessoa: ");
		final JTextField txt1 = new JTextField(10);
		JLabel jl2 = new JLabel("Data empréstimo:");
		final JTextField txt2 = new JTextField(2);
		final JTextField txt3 = new JTextField(2);
		final JTextField txt4 = new JTextField(4);
		JLabel jl3 = new JLabel("Data retorno:");
		final JTextField txt5 = new JTextField(2);
		final JTextField txt6 = new JTextField(2);
		final JTextField txt7 = new JTextField(4);
		
		JButton bt1 = new JButton("Adicionar");
		JButton bt2 = new JButton("Eliminar");
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					final String dataemp=txt4.getText()+"-"+txt3.getText()+"-"+txt2.getText();
					final String dataret=txt7.getText()+"-"+txt6.getText()+"-"+txt5.getText();
					new LigacaoBD().alterarEmprestimo(idemprestimo,txt1.getText(),dataemp,dataret);
					JOptionPane.showMessageDialog(null,"Empréstimo alterado!");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		bt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new LigacaoBD().apagarEmprestimo(idemprestimo);
					JOptionPane.showMessageDialog(null,"Empréstimo eliminado!");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		JPanel painel1 = new JPanel();
		painel1.add(jl1);
		painel1.add(txt1);
		JPanel painel2 = new JPanel();
		painel2.add(jl2);
		painel2.add(txt2);
		painel2.add(txt3);
		painel2.add(txt4);
		JPanel painel3 = new JPanel();
		painel3.add(jl3);
		painel3.add(txt5);
		painel3.add(txt6);
		painel3.add(txt7);
		JPanel painel4 = new JPanel();
		painel4.add(bt1);
		painel4.add(bt2);
		mainpanel.add(painel1);
		mainpanel.add(painel2);
		mainpanel.add(painel3);
		mainpanel.add(painel4);
		add(mainpanel);
		
		
		setResizable(false);
		setVisible(true);
		
	}
	
	protected void alterarEliminarVisualizacao(final int idvisualizacao) {
		setTitle("Alterar suporte");
		setSize(220,100);
		setLayout(new FlowLayout());
		JLabel jl1 = new JLabel("Data (DD-MM-AAAA):");
		final JTextField txt1 = new JTextField(2);
		final JTextField txt2 = new JTextField(2);
		final JTextField txt3 = new JTextField(4);
		
		
		JButton bt1 = new JButton("Alterar");
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					final String data=txt3.getText()+"-"+txt2.getText()+"-"+txt1.getText();
					new LigacaoBD().alterarVisualizacao(idvisualizacao,data);
					JOptionPane.showMessageDialog(null,"Visualizacão alterada");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		JButton bt2 = new JButton("Eliminar");
		bt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new LigacaoBD().apagarVisualizacao(idvisualizacao);
					JOptionPane.showMessageDialog(null,"Visualizacão eliminada");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		add(jl1);
		add(txt1);
		add(txt2);
		add(txt3);
		add(bt1);
		add(bt2);
		
		setResizable(false);
		setVisible(true);
		
	}
	
	protected void ligarLocalizacao(final int idsuporte) throws IOException {
		setTitle("Adicionar localizacao");
		setSize(220,100);
		setLayout(new FlowLayout());
		
		ArrayList<Localizacao> localizacoes=new ArrayList<Localizacao>();
		new LigacaoBD().listarPrateleiras(localizacoes);
		
		ArrayList<String> prateleiras = new ArrayList<String>();
		
		for (int i=0;i<localizacoes.size();i++) {
			String prateleira = localizacoes.get(i).armario+"-"+Integer.toString(localizacoes.get(i).prateleira);
			System.out.println(prateleira);
			prateleiras.add(prateleira);
		}
		
		JLabel jl1 = new JLabel("Prateleira:");
		final JComboBox combo = new JComboBox(prateleiras.toArray());
		JButton bt1 = new JButton("Adicionar");
		
		
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String[] partes = combo.getSelectedItem().toString().split("-"); //Separar em armário e prateleira
					new LigacaoBD().ligarLocalizacao(idsuporte,partes[0].charAt(0),Integer.parseInt(partes[1]));
					JOptionPane.showMessageDialog(null,"Localização adicionada");
					fecharJanela();
					VideotecaPessoal.janela.atualizarJanela();
				} catch (IOException e1) {
				}
			}
		});
		
		add(jl1);
		add(combo);
		add(bt1);
		
		setResizable(false);
		setVisible(true);
		
	}
	
}
