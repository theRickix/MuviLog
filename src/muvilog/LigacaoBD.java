package muvilog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LigacaoBD {
	
	//Estabelecer ligação à BD
	public Connection obterLigacao(){
		System.out.println("Teste ao acesso a uma BD SQL.");     
		Connection con = null;
	  
		try {
			Class.forName("org.h2.Driver");
			con = DriverManager.getConnection("jdbc:h2:file:./muvilogdb", "sa", "");
			System.out.println("Ligação efetuada com sucesso.");
		}
		catch (ClassNotFoundException cnfe){
			System.out.println("ClassNotFoundException");
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
		return con;
	}
  
	//Fechar ligação à BD
	public void fecharLigacao(Connection con){
		try {
			con.close();
			System.out.println("Ligação fechada com sucesso.");
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		} 
	}

	//Criar tabelas, caso não existem
	public void criarTabelas(Connection con) {
		try {
			Statement stmt=con.createStatement();
			
			//Criar tabela de géneros
			String sql="CREATE TABLE IF NOT EXISTS genero(" 
					+ "id INTEGER PRIMARY KEY,"
					+ "nome_eng CHAR(20),"
					+ "nome CHAR(20));"
			//Criar tabela de países		
					+ "CREATE TABLE IF NOT EXISTS pais("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "nome CHAR(50));"
			//Criar tabela de idiomas
					+ "CREATE TABLE IF NOT EXISTS idioma("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "nome CHAR(20));"
			//Criar tabela de tipos de filme
					+ "CREATE TABLE IF NOT EXISTS tipo("
					+ "id INTEGER PRIMARY KEY,"
					+ "nome_eng CHAR(20),"
					+ "nome CHAR(20));"
			//Criar tabela de filmes
					+ "CREATE TABLE IF NOT EXISTS filme("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "imdbid CHAR(20),"
					+ "titulo VARCHAR,"
					+ "ano INTEGER,"
					+ "duracao INTEGER,"
					+ "sinopse CHAR(255),"
					+ "poster CHAR(70),"
					+ "metascore INTEGER,"
					+ "imdbRating FLOAT,"
					+ "tipo INTEGER REFERENCES tipo(id));"
			//Criar tabela de séries
					+ "CREATE TABLE IF NOT EXISTS serie("
					+ "id INTEGER REFERENCES filme(id) ON DELETE CASCADE,"
					+ "anofim INTEGER,"
					+ "PRIMARY KEY (id));"
			//Criar tabela de pessoas
					+ "CREATE TABLE IF NOT EXISTS pessoa("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "nome CHAR(40));"
			//Criar tabela de filmes/generos
					+ "CREATE TABLE IF NOT EXISTS filme_genero("
					+ "filme INTEGER REFERENCES filme(id) ON DELETE CASCADE,"
					+ "genero INTEGER REFERENCES genero(id),"
					+ "PRIMARY KEY (filme,genero));"
			//Criar tabela de filmes/idiomas
					+ "CREATE TABLE IF NOT EXISTS filme_idioma("
					+ "filme INTEGER REFERENCES filme(id) ON DELETE CASCADE,"
					+ "idioma INTEGER REFERENCES idioma(id),"
					+ "PRIMARY KEY (filme,idioma));"
			//Criar tabela de filmes/paises		
					+ "CREATE TABLE IF NOT EXISTS filme_pais("
					+ "filme INTEGER REFERENCES filme(id) ON DELETE CASCADE,"
					+ "pais INTEGER REFERENCES pais(id),"
					+ "PRIMARY KEY (filme,pais));"
			//Criar tabela de atores
					+ "CREATE TABLE IF NOT EXISTS ator("
					+ "filme INTEGER REFERENCES filme(id) ON DELETE CASCADE,"
					+ "pessoa INTEGER REFERENCES pessoa(id),"
					+ "PRIMARY KEY (filme,pessoa));"
			//Criar tabela de realizadores
					+ "CREATE TABLE IF NOT EXISTS realizador("
					+ "filme INTEGER REFERENCES filme(id) ON DELETE CASCADE,"
					+ "pessoa INTEGER REFERENCES pessoa(id),"
					+ "PRIMARY KEY (filme,pessoa));"
			//Criar tabela de tipos de suporte	
					+ "CREATE TABLE IF NOT EXISTS tiposuporte("
					+ "id INTEGER PRIMARY KEY,"
					+ "nome CHAR(15));"
			//Criar tabela de suportes	
					+ "CREATE TABLE IF NOT EXISTS suporte("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "filme INTEGER REFERENCES filme(id) ON DELETE CASCADE,"
					+ "tipo INTEGER REFERENCES tiposuporte(id));"
			//Criar tabela de visualizações		
					+ "CREATE TABLE IF NOT EXISTS visualizacao("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "data DATE,"
					+ "suporte INTEGER REFERENCES suporte(id) ON DELETE CASCADE);"
			//Criar tabela de unidades de localização		
					+ "CREATE TABLE IF NOT EXISTS unidadelocalizacao("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "armario CHAR(2),"
					+ "prateleira INTEGER);"
			//Criar tabela de suportes físicos
					+ "CREATE TABLE IF NOT EXISTS suportefisico("
					+ "id INTEGER PRIMARY KEY REFERENCES suporte(id) ON DELETE CASCADE,"
					+ "localizacao INTEGER REFERENCES unidadelocalizacao(id));"
			//Criar tabela de emprestimo	
					+ "CREATE TABLE IF NOT EXISTS emprestimo("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "suporte INTEGER REFERENCES suportefisico(id) ON DELETE CASCADE,"
					+ "pessoa CHAR(30),"
					+ "data_emp DATE,"
					+ "data_ret DATE);";
			stmt.executeUpdate(sql);
			System.out.println("Tabelas criadas");
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public void popularTabelas(Connection con) {
		try {
			Statement stmt=con.createStatement();
			
			/*Verificar se as tabelas já estão populadas com um COUNT
			 * a cada uma, não povoando se os valores já existirem.
			*/
			
			String sql="SELECT COUNT(*) AS count FROM genero;";
			ResultSet rs=stmt.executeQuery(sql);
			int ngeneros=0;
			
			while(rs.next()) {
				ngeneros=rs.getInt("count");
				System.out.println(ngeneros);
			}
			
			
			if(ngeneros!=26) {
				sql="INSERT INTO genero VALUES " + 
						"(1,'Action','Ação'),(2,'Adventure','Aventura'),(3,'Animation','Animação')," + 
						"(4,'Biography','Biografia'),(5,'Comedy','Comédia'),(6,'Crime','Crime')," + 
						"(7,'Documentary','Documentário'),(8,'Drama','Drama'),(9,'Family','Família')," + 
						"(10,'Fantasy','Fantasia'),(11,'Film-Noir','Film-Noir'),(12,'Game-Show','Game-Show')," + 
						"(13,'History','História'),(14,'Horror','Terror'),(15,'Music','Música')," + 
						"(16,'Musical','Musical'),(17,'Mystery','Mistério'),(18,'News','Notícias')," + 
						"(19,'Reality-TV','Reality-TV'),(20,'Romance','Romance'),(21,'Sci-Fi','Ficção Científica')," + 
						"(22,'Sport','Desporto'),(23,'Talk-Show','Talk-Show'),(24,'Thriller','Thriller')," + 
						"(25,'War','Guerra'),(26,'Western','Western');";
				System.out.println("Tabela 'genero' populada.");
				stmt.executeUpdate(sql);
			}
			else {
				System.out.println("Tabela 'genero' não populada.");
			}
			
			sql="SELECT COUNT(*) AS count FROM tipo;";
			rs=stmt.executeQuery(sql);
			int ntipos=0;
			
			while(rs.next()) {
				ntipos=rs.getInt("count");
				System.out.println(ntipos);
			}
			
			
			if(ntipos!=3) {
				sql="INSERT INTO tipo VALUES " + 
						"(1,'movie','Filme'),(2,'series','Série'),(3,'episode','Episódio');";
				System.out.println("Tabela 'tipo' populada.");
				stmt.executeUpdate(sql);
			}
			else {
				System.out.println("Tabela 'tipo' não populada.");
			}
			
			sql="SELECT COUNT(*) AS count FROM pais;";
			rs=stmt.executeQuery(sql);
			int npaises=0;
			
			while(rs.next()) {
				npaises=rs.getInt("count");
				System.out.println(npaises);
			}
			
			
			if(npaises!=259) {
				sql="INSERT INTO pais(nome) VALUES "
						+ "('Argentina')," + 
						"('Australia')," + 
						"('Austria')," + 
						"('Belgium')," + 
						"('Brazil')," + 
						"('Bulgaria')," + 
						"('Canada')," + 
						"('China')," + 
						"('Colombia')," + 
						"('Costa Rica')," + 
						"('Czech Republic')," + 
						"('Denmark')," + 
						"('Finland')," + 
						"('France')," + 
						"('Germany')," + 
						"('Greece')," + 
						"('Hong Kong')," + 
						"('Hungary')," + 
						"('Iceland')," + 
						"('India')," + 
						"('Iran')," + 
						"('Ireland')," + 
						"('Italy')," + 
						"('Japan')," + 
						"('Malaysia')," + 
						"('Mexico')," + 
						"('Netherlands')," + 
						"('New Zealand')," + 
						"('Pakistan')," + 
						"('Poland')," + 
						"('Portugal')," + 
						"('Romania')," + 
						"('Russia')," + 
						"('Singapore')," + 
						"('South Africa')," + 
						"('Spain')," + 
						"('Sweden')," + 
						"('Switzerland')," + 
						"('Thailand')," + 
						"('UK')," + 
						"('USA')," + 
						"('Afghanistan')," + 
						"('Åland Islands')," + 
						"('Albania')," + 
						"('Algeria')," + 
						"('American Samoa')," + 
						"('Andorra')," + 
						"('Angola')," + 
						"('Anguilla')," + 
						"('Antarctica')," + 
						"('Antigua and Barbuda')," + 
						"('Armenia')," + 
						"('Aruba')," + 
						"('Azerbaijan')," + 
						"('Bahamas')," + 
						"('Bahrain')," + 
						"('Bangladesh')," + 
						"('Barbados')," + 
						"('Belarus')," + 
						"('Belize')," + 
						"('Benin')," + 
						"('Bermuda')," + 
						"('Bhutan')," + 
						"('Bolivia')," + 
						"('Bosnia and Herzegovina')," + 
						"('Botswana')," + 
						"('Bouvet Island')," + 
						"('British Indian Ocean Territory')," + 
						"('British Virgin Islands')," + 
						"('Brunei Darussalam')," + 
						"('Burkina Faso')," + 
						"('Burma')," + 
						"('Burundi')," + 
						"('Cambodia')," + 
						"('Cameroon')," + 
						"('Cape Verde')," + 
						"('Cayman Islands')," + 
						"('Central African Republic')," + 
						"('Chad')," + 
						"('Chile')," + 
						"('Christmas Island')," + 
						"('Cocos (Keeling) Islands')," + 
						"('Comoros')," + 
						"('Congo')," + 
						"('Cook Islands')," + 
						"('Côte d''Ivoire')," + 
						"('Croatia')," + 
						"('Cuba')," + 
						"('Cyprus')," + 
						"('Czechoslovakia')," + 
						"('Democratic Republic of the Congo')," + 
						"('Djibouti')," + 
						"('Dominica')," + 
						"('Dominican Republic')," + 
						"('East Germany')," + 
						"('Ecuador')," + 
						"('Egypt')," + 
						"('El Salvador')," + 
						"('Equatorial Guinea')," + 
						"('Eritrea')," + 
						"('Estonia')," + 
						"('Ethiopia')," + 
						"('Falkland Islands')," + 
						"('Faroe Islands')," + 
						"('Federal Republic of Yugoslavia')," + 
						"('Federated States of Micronesia')," + 
						"('Fiji')," + 
						"('French Guiana')," + 
						"('French Polynesia')," + 
						"('French Southern Territories')," + 
						"('Gabon')," + 
						"('Gambia')," + 
						"('Georgia')," + 
						"('Ghana')," + 
						"('Gibraltar')," + 
						"('Greenland')," + 
						"('Grenada')," + 
						"('Guadeloupe')," + 
						"('Guam')," + 
						"('Guatemala')," + 
						"('Guernsey')," + 
						"('Guinea')," + 
						"('Guinea-Bissau')," + 
						"('Guyana')," + 
						"('Haiti')," + 
						"('Heard Island and McDonald Islands')," + 
						"('Holy See (Vatican City State)')," + 
						"('Honduras')," + 
						"('Indonesia')," + 
						"('Iraq')," + 
						"('Isle of Man')," + 
						"('Israel')," + 
						"('Jamaica')," + 
						"('Jersey')," + 
						"('Jordan')," + 
						"('Kazakhstan')," + 
						"('Kenya')," + 
						"('Kiribati')," + 
						"('Korea')," + 
						"('Kosovo')," + 
						"('Kuwait')," + 
						"('Kyrgyzstan')," + 
						"('Laos')," + 
						"('Latvia')," + 
						"('Lebanon')," + 
						"('Lesotho')," + 
						"('Liberia')," + 
						"('Libya')," + 
						"('Liechtenstein')," + 
						"('Lithuania')," + 
						"('Luxembourg')," + 
						"('Macao')," + 
						"('Madagascar')," + 
						"('Malawi')," + 
						"('Maldives')," + 
						"('Mali')," + 
						"('Malta')," + 
						"('Marshall Islands')," + 
						"('Martinique')," + 
						"('Mauritania')," + 
						"('Mauritius')," + 
						"('Mayotte')," + 
						"('Moldova')," + 
						"('Monaco')," + 
						"('Mongolia')," + 
						"('Montenegro')," + 
						"('Montserrat')," + 
						"('Morocco')," + 
						"('Mozambique')," + 
						"('Myanmar')," + 
						"('Namibia')," + 
						"('Nauru')," + 
						"('Nepal')," + 
						"('Netherlands Antilles')," + 
						"('New Caledonia')," + 
						"('Nicaragua')," + 
						"('Niger')," + 
						"('Nigeria')," + 
						"('Niue')," + 
						"('Norfolk Island')," + 
						"('North Korea')," + 
						"('North Vietnam')," + 
						"('Northern Mariana Islands')," + 
						"('Norway')," + 
						"('Oman')," + 
						"('Palau')," + 
						"('Palestine')," + 
						"('Palestinian Territory')," + 
						"('Panama')," + 
						"('Papua New Guinea')," + 
						"('Paraguay')," + 
						"('Peru')," + 
						"('Philippines')," + 
						"('Pitcairn')," + 
						"('Puerto Rico')," + 
						"('Qatar')," + 
						"('Republic of Macedonia')," + 
						"('Réunion')," + 
						"('Rwanda')," + 
						"('Saint Barthélemy')," + 
						"('Saint Helena')," + 
						"('Saint Kitts and Nevis')," + 
						"('Saint Lucia')," + 
						"('Saint Martin (French part)')," + 
						"('Saint Pierre and Miquelon')," + 
						"('Saint Vincent and the Grenadines')," + 
						"('Samoa')," + 
						"('San Marino')," + 
						"('Sao Tome and Principe')," + 
						"('Saudi Arabia')," + 
						"('Senegal')," + 
						"('Serbia')," + 
						"('Serbia and Montenegro')," + 
						"('Seychelles')," + 
						"('Siam')," + 
						"('Sierra Leone')," + 
						"('Slovakia')," + 
						"('Slovenia')," + 
						"('Solomon Islands')," + 
						"('Somalia')," + 
						"('South Georgia and the South Sandwich Islands')," + 
						"('South Korea')," + 
						"('Soviet Union')," + 
						"('Sri Lanka')," + 
						"('Sudan')," + 
						"('Suriname')," + 
						"('Svalbard and Jan Mayen')," + 
						"('Swaziland')," + 
						"('Syria')," + 
						"('Taiwan')," + 
						"('Tajikistan')," + 
						"('Tanzania')," + 
						"('Timor-Leste')," + 
						"('Togo')," + 
						"('Tokelau')," + 
						"('Tonga')," + 
						"('Trinidad and Tobago')," + 
						"('Tunisia')," + 
						"('Turkey')," + 
						"('Turkmenistan')," + 
						"('Turks and Caicos Islands')," + 
						"('Tuvalu')," + 
						"('U.S. Virgin Islands')," + 
						"('Uganda')," + 
						"('Ukraine')," + 
						"('United Arab Emirates')," + 
						"('United States Minor Outlying Islands')," + 
						"('Uruguay')," + 
						"('Uzbekistan')," + 
						"('Vanuatu')," + 
						"('Venezuela')," + 
						"('Vietnam')," + 
						"('Wallis and Futuna')," + 
						"('West Germany')," + 
						"('Western Sahara')," + 
						"('Yemen')," + 
						"('Yugoslavia')," + 
						"('Zaire')," + 
						"('Zambia');";
				System.out.println("Tabela 'pais' populada.");
				stmt.executeUpdate(sql);
			}
			else {
				System.out.println("Tabela 'pais' não populada.");
			}
			
			sql="SELECT COUNT(*) AS count FROM tiposuporte;";
			rs=stmt.executeQuery(sql);
			int ntipossuporte=0;
			
			while(rs.next()) {
				ntipossuporte=rs.getInt("count");
				System.out.println(ntipossuporte);
			}
			
			
			if(ntipossuporte!=7) {
				sql="INSERT INTO tiposuporte VALUES " + 
						"(1,'VHS'),(2,'DVD'),(3,'Blu-Ray')," + 
						"(4,'Blu-Ray 3D'),(5,'UMD'),(6,'LaserDisc'),(7,'Digital');";
				System.out.println("Tabela 'tiposuporte' populada.");
				stmt.executeUpdate(sql);
			}
			else {
				System.out.println("Tabela 'tiposuporte' não populada.");
			}
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
  
	public void listarFilmes(ArrayList<Filme> filmes) throws IOException {
		Connection con = this.obterLigacao();
		try{
			Statement stmt=con.createStatement();
			//Receber filmes e as suas informações
			String sql="SELECT * from filme LEFT JOIN serie ON filme.id=serie.id";
			ResultSet rs= stmt.executeQuery(sql);
			
			while(rs.next()){				
				Filme filme = new Filme();
				filme.id=rs.getInt("id");
				filme.imdbid=rs.getString("imdbid");
				filme.titulo=rs.getString("titulo");
				filme.ano=rs.getInt("ano");
				filme.duracao=rs.getInt("duracao");
				filme.sinopse=rs.getString("sinopse");
				filme.poster=rs.getString("poster");
				filme.metascore=rs.getInt("metascore");
				filme.imdbrating=rs.getFloat("imdbrating");
				filme.tipo=rs.getInt("tipo");
				if(filme.tipo==2) {
					filme.anofim=rs.getInt("anofim");
				}
				
				filmes.add(filme);
			}
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public void listarVisualizacoes(ArrayList<Visualizacao> visualizacoes) throws IOException {
		Connection con = this.obterLigacao();
		try{
			Statement stmt=con.createStatement();
			//Receber visualizações e as suas informações
			String sql="SELECT visualizacao.id,suporte.id,filme.titulo,tiposuporte.nome,visualizacao.data from visualizacao "
					+ "INNER JOIN suporte ON visualizacao.suporte=suporte.id "
					+ "INNER JOIN tiposuporte ON suporte.tipo=tiposuporte.id "
					+ "INNER JOIN filme ON suporte.filme=filme.id "
					+ "ORDER by visualizacao.data;";
			System.out.println(sql);
			ResultSet rs= stmt.executeQuery(sql);
			
			while(rs.next()){				
				Visualizacao visualizacao = new Visualizacao();
				visualizacao.id=rs.getInt("id");
				visualizacao.filme=rs.getString("titulo");
				visualizacao.suporte=rs.getString("nome");
				visualizacao.data=rs.getString("data");				
				
				visualizacoes.add(visualizacao);
			}
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public Boolean verificarFilme(Filme filme) throws IOException {
		Connection con = this.obterLigacao();
		Boolean encontrado=false;
		try{
			//Verificar se já existe na BD
			Statement stmt=con.createStatement();
			String sql="SELECT * from filme";
			ResultSet rs= stmt.executeQuery(sql);

			while(rs.next() && encontrado==false){
				if((filme.titulo).equals(rs.getString("titulo"))) {
					System.out.println(filme.titulo+" vs "+rs.getString("titulo"));
					encontrado=true;					
				}
			}
			
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
		return encontrado;
  }
  
	public void insertFilme(Filme filme) throws IOException {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Adicionar filme à BD
			String sql="INSERT INTO filme(imdbid,titulo,ano,duracao,sinopse,poster,metascore,imdbrating,tipo) VALUES "
					+ "('"+filme.imdbid+"','"+filme.titulo+"',"+filme.ano+","+
											+filme.duracao+",'"+filme.sinopse+"','"+filme.poster+"',"+filme.metascore+","+filme.imdbrating+","+filme.tipo+");";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
			sql="SELECT id FROM filme "
					+ "WHERE imdbid='"+filme.imdbid+"';";
			ResultSet rs= stmt.executeQuery(sql);
			
			while(rs.next()){
				filme.id=rs.getInt("id");
			}
			
			this.ligarGeneros((filme.id),filme.generos);
			this.ligarPaises((filme.id),filme.paises);
			this.ligarPessoas(filme.id,filme.atores,0);
			this.ligarPessoas(filme.id,filme.realizadores,1);
			
			if(filme.tipo==2) {
				sql="INSERT INTO serie(id,anofim) VALUES "
						+ "("+filme.id+","+filme.anofim+");";
				System.out.println(sql);
				stmt.executeUpdate(sql);
			}
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public void apagarFilme(int idfilme) throws IOException {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Apagar filme da BD
			String sql="DELETE FROM filme "
					+ "WHERE id="+idfilme+";";
			System.out.println(sql);
			stmt.executeUpdate(sql);

		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public int verificarTipo(String tipostring) {
		Connection con = this.obterLigacao();
		try{			
			int tipo=0;
			Statement stmt=con.createStatement();
			//Verificar tipo do filme
			String sql="SELECT tipo.id,tipo.nome_eng from tipo "
					+ "WHERE nome_eng='"+tipostring+"';";
			System.out.println(sql);
			ResultSet rs= stmt.executeQuery(sql);
			
			while(rs.next()){
				tipo=rs.getInt("id");
			}
			
			return tipo;
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
			return 0;
		}	
	}
	
	public String receberTipo(int tipo) {
		if(tipo==1)
			return "Filme";
		if(tipo==2)
			return "Série";
		else
			return "Episódio";
	}
	
	public void ligarGeneros(int idfilme,String generostring[]) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			
			//Ligar filme aos seus géneros
			
			for(int i=0; i<generostring.length;i++) {
		        System.out.println(generostring[i]);
		        int idgenero=0;
		        String sql="SELECT genero.id,genero.nome_eng from genero "
						+ "WHERE nome_eng='"+generostring[i]+"';";
		        System.out.println(sql);
				ResultSet rs= stmt.executeQuery(sql);
				
				while(rs.next()){
					idgenero=rs.getInt("id");
				}
				
				sql="INSERT INTO filme_genero VALUES "
						+ "("+idfilme+","+idgenero+");";
				stmt.executeUpdate(sql);
				System.out.println(sql);
		    }
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	public String receberGeneros(int idfilme) {
		Connection con = this.obterLigacao();
		ArrayList<String> generos = new ArrayList<String>();
		try{			
			Statement stmt=con.createStatement();
			//Receber os géneros de um filme
			String sql="SELECT genero.id,genero.nome from genero "
					+ "INNER JOIN filme_genero ON genero.id=filme_genero.genero "
					+ "INNER JOIN filme ON filme.id=filme_genero.filme "
					+ "WHERE filme.id="+idfilme+";";
		    System.out.println(sql);
		    ResultSet rs= stmt.executeQuery(sql);
		   
			while(rs.next()){
				generos.add(rs.getString("nome"));
				System.out.println(rs.getString("nome"));
			}
			
			
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
		
		//Juntar tudo numa string
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < generos.size(); i++) { 
			if (i > 0) {
				sb.append(", ");
			}

			sb.append(generos.get(i)); 
		}
		return sb.toString();
	}
	
	public String receberPaises(int idfilme) {
		Connection con = this.obterLigacao();
		ArrayList<String> paises = new ArrayList<String>();
		try{			
			Statement stmt=con.createStatement();
			//Receber países do filme
			String sql="SELECT pais.id,pais.nome from pais "
					+ "INNER JOIN filme_pais ON pais.id=filme_pais.pais "
					+ "INNER JOIN filme ON filme.id=filme_pais.filme "
					+ "WHERE filme.id="+idfilme+";";
		    System.out.println(sql);
		    ResultSet rs= stmt.executeQuery(sql);
		   
			while(rs.next()){
				paises.add(rs.getString("nome"));
				System.out.println(rs.getString("nome"));
			}
			
			
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
		//Juntar tudo numa String
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paises.size(); i++) { 
			if (i > 0) {
				sb.append(", ");
			}

			sb.append(paises.get(i)); 
		}
		return sb.toString();
	}
  
	public String receberAtores(int idfilme) {
		Connection con = this.obterLigacao();
		ArrayList<String> atores = new ArrayList<String>();
		try{			
			Statement stmt=con.createStatement();
			//Receber atores de um filme
			String sql="SELECT pessoa.id,pessoa.nome from pessoa "
					+ "INNER JOIN ator ON pessoa.id=ator.pessoa "
					+ "INNER JOIN filme ON filme.id=ator.filme "
					+ "WHERE filme.id="+idfilme+";";
		    System.out.println(sql);
		    ResultSet rs= stmt.executeQuery(sql);
		   
			while(rs.next()){
				atores.add(rs.getString("nome"));
				System.out.println(rs.getString("nome"));
			}
			
			
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
		//Juntar tudo numa string
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < atores.size(); i++) { 
			if (i > 0) {
				sb.append(", ");
			}

			sb.append(atores.get(i)); 
		}
		return sb.toString();
	}
	
	public String receberRealizadores(int idfilme) {
		Connection con = this.obterLigacao();
		ArrayList<String> realizadores = new ArrayList<String>();
		try{			
			Statement stmt=con.createStatement();
			//Receber realizadores do filmne
			String sql="SELECT pessoa.id,pessoa.nome from pessoa "
					+ "INNER JOIN realizador ON pessoa.id=realizador.pessoa "
					+ "INNER JOIN filme ON filme.id=realizador.filme "
					+ "WHERE filme.id="+idfilme+";";
		    System.out.println(sql);
		    ResultSet rs= stmt.executeQuery(sql);
		   
			while(rs.next()){
				realizadores.add(rs.getString("nome"));
				System.out.println(rs.getString("nome"));
			}
			
			
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
		//Juntar tudo numa String
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < realizadores.size(); i++) { 
			if (i > 0) {
				sb.append(", ");
			}

			sb.append(realizadores.get(i)); 
		}
		return sb.toString();
	}
	
	public Suporte[] receberSuportes(int idfilme) {
		Connection con = this.obterLigacao();
		ArrayList<Suporte> suportes = new ArrayList<Suporte>();
		try{			
			Statement stmt=con.createStatement();
			//Receber suportes e as suas informações
			String sql="SELECT suporte.id,filme.titulo,tiposuporte.nome FROM suporte "
					+ "INNER JOIN tiposuporte ON suporte.tipo=tiposuporte.id "
					+ "INNER JOIN filme ON filme.id=suporte.filme "
					+ "WHERE filme.id="+idfilme+";";
		    System.out.println(sql);
		    ResultSet rs= stmt.executeQuery(sql);
		   
			while(rs.next()){
				suportes.add(new Suporte(rs.getInt("id"),rs.getString("titulo"),rs.getString("nome")));
				System.out.println(rs.getString("nome"));
			}
			
			
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
		return suportes.toArray(new Suporte[suportes.size()]);
	}
	
	public Suporte[] listarSuportesPrateleira(char armario,int prateleira) {
		Connection con = this.obterLigacao();
		ArrayList<Suporte> suportes = new ArrayList<Suporte>();
		try{			
			Statement stmt=con.createStatement();
			//Receber suportes, conforme as suas localizações
			String sql="SELECT suportefisico.id,filme.titulo,tiposuporte.nome FROM suportefisico "
					+ "INNER JOIN unidadelocalizacao ON suportefisico.localizacao=unidadelocalizacao.id "
					+ "INNER JOIN suporte ON suportefisico.id=suporte.id "
					+ "INNER JOIN tiposuporte ON suporte.tipo=tiposuporte.id "
					+ "INNER JOIN filme ON filme.id=suporte.filme "
					+ "WHERE unidadelocalizacao.armario='"+armario+"' AND unidadelocalizacao.prateleira="+prateleira+";";
		    System.out.println(sql);
		    ResultSet rs= stmt.executeQuery(sql);
		   
			while(rs.next()){
				suportes.add(new Suporte(rs.getInt("id"),rs.getString("titulo"),rs.getString("nome")));
				System.out.println(rs.getString("titulo")+rs.getString("nome"));
			}
			
			
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
		return suportes.toArray(new Suporte[suportes.size()]);
	}
	
	public void ligarPaises(int idfilme,String paisstring[]) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Ligar filmes aos países
			for(int i=0; i<paisstring.length;i++) {
		        System.out.println(paisstring[i]);
		        int idpais=0;
		        String sql="SELECT pais.id,pais.nome from pais "
						+ "WHERE nome='"+paisstring[i]+"';";
		        System.out.println(sql);
				ResultSet rs= stmt.executeQuery(sql);
				
				while(rs.next()){
					idpais=rs.getInt("id");
				}
				
				sql="INSERT INTO filme_pais VALUES "
						+ "("+idfilme+","+idpais+");";
				stmt.executeUpdate(sql);
				System.out.println(sql);
		    }
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}

	
	public void ligarPessoas(int idfilme,String pessoastring[],int opcao) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Ligar filmes às pessoas (atores e realizadores)
			for(int i=0; i<pessoastring.length;i++) {
		        System.out.println(pessoastring[i]);
		        int idpessoa=0;
		        //Verificar se já existe na BD
		        String sql="SELECT pessoa.id,pessoa.nome from pessoa "
						+ "WHERE nome='"+pessoastring[i]+"';";
		        System.out.println(sql);
				ResultSet rs= stmt.executeQuery(sql);
				
				if(rs.next()){
					idpessoa=rs.getInt("id");
					System.out.println("ID da pessoa:"+idpessoa);
				}
				else { //Criar pessoa
					idpessoa=this.adicionarPessoa(pessoastring[i]);
				}
				//0=ator;1=realizador
				if(opcao==0) {
					sql="INSERT INTO ator VALUES "
							+ "("+idfilme+","+idpessoa+");";
					System.out.println(sql);
					stmt.executeUpdate(sql);
				}
				else {
					sql="INSERT INTO realizador VALUES "
							+ "("+idfilme+","+idpessoa+");";
					System.out.println(sql);
					stmt.executeUpdate(sql);
				}		
				
				
		    }
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	public int adicionarPessoa(String pessoa) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Adicionar pessoa à BD
			int idpessoa=0;
			String sql="INSERT INTO pessoa(nome) VALUES "
						+ "('"+pessoa+"');";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
			sql="SELECT id FROM pessoa "
					+ "WHERE nome='"+pessoa+"';";
			
			ResultSet rs= stmt.executeQuery(sql);
			
			while(rs.next()){
				idpessoa=rs.getInt("id");
				System.out.println("ID da pessoa:"+idpessoa);
			}
			return idpessoa;
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
			return 0;
		}
	}
	
	public void adicionarSuporte(int idfilme,String tiposuporte) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Adicionar suporte ao filme
			int idtiposuporte=0;
			
			System.out.println(tiposuporte);
	  
	        String sql="SELECT tiposuporte.id,tiposuporte.nome from tiposuporte "
					+ "WHERE nome='"+tiposuporte+"';";
	        System.out.println(sql);
			ResultSet rs= stmt.executeQuery(sql);
		
			while(rs.next()){
				idtiposuporte=rs.getInt("id");
				System.out.println("ID do formato:"+idtiposuporte);
			}
			
			sql="INSERT INTO suporte(filme,tipo) VALUES "
					+ "("+idfilme+","+idtiposuporte+");";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
			if (idtiposuporte!=7) { //Verificar se é físico, ou seja, não digital
				//Receber id do último suporte, ou seja, o inserido agora
				sql="SELECT TOP 1 id FROM suporte " + 
						"ORDER BY id DESC;";
		        System.out.println(sql);
				rs= stmt.executeQuery(sql);
				
				int idsuporte=0;
				
				while(rs.next()){
					idsuporte=rs.getInt("id");
					System.out.println("ID do suporte:"+idtiposuporte);
				}
				//Criar suporte físico do sistema (ISA)
				sql="INSERT INTO suportefisico VALUES "
						+ "("+idsuporte+",NULL);";
				System.out.println(sql);
				stmt.executeUpdate(sql);
			}
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	public void apagarSuporte(int idsuporte) throws IOException {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Apagar suporte
			String sql="DELETE FROM suporte "
					+ "WHERE id="+idsuporte+";";
			System.out.println(sql);
			stmt.executeUpdate(sql);

		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public void adicionarVisualizacao(int idsuporte,String data) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();		
			//Adicionar visualização a um suporte
			String sql="INSERT INTO visualizacao(data,suporte) VALUES "
					+ "('"+data+"',"+idsuporte+");";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	public void alterarVisualizacao(int idvisualizacao,String data) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();		
			//Alterar uma visualização
			String sql="UPDATE visualizacao "
					+ "SET data='"+data+"' "
					+ "WHERE id="+idvisualizacao+";";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	public void apagarVisualizacao(int idvisualizacao) throws IOException {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Apagar uma visualização
			String sql="DELETE FROM visualizacao "
					+ "WHERE id="+idvisualizacao+";";
			System.out.println(sql);
			stmt.executeUpdate(sql);

		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	
	public void adicionarArmario(int nprateleiras) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();		
			//Adicionar armário e respetivas prateleiras
			char armario='A'; //Inicia com a primeira letra do alfabeto
			//Recebe a última letra usada
			String sql="SELECT TOP 1 armario FROM unidadelocalizacao " + 
					"ORDER BY armario DESC;";
			System.out.println(sql);
			ResultSet rs= stmt.executeQuery(sql);
		
			while(rs.next()){
				armario=(rs.getString("armario")).charAt(0); //Converter para char
				armario=(char) (armario+1); //Adicionar 1 ao apontador de chars
											//Recebenbdo a letra seguinte
			}
			
			//Criar prateleiras
			for (int i=1;i<=nprateleiras;i++) {
				sql="INSERT INTO unidadelocalizacao(armario,prateleira) VALUES "
						+ "('"+armario+"',"+i+");";
				System.out.println(sql);
				stmt.executeUpdate(sql);
			}
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	public void listarPrateleiras(ArrayList<Localizacao> prateleiras) throws IOException {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Listar prateleiras
			String sql="SELECT * from unidadelocalizacao";
			System.out.println(sql);
			ResultSet rs= stmt.executeQuery(sql);
			
			while(rs.next()){				
				Localizacao prateleira = new Localizacao();
				prateleira.id=rs.getInt("id");
				prateleira.armario=(rs.getString("armario")).charAt(0);
				prateleira.prateleira=rs.getInt("prateleira");		
				
				prateleiras.add(prateleira);
			}

		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public void apagarArmario(char armario) throws IOException {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Apagar armario e respetivas prateleiras
			//Colocar localização como NULL nos suportes associados, não correndo
			//o risco de serem apagados pela uso do ON DELETE CASCADE
			String sql="UPDATE suportefisico" 
					+ "SET localizacao=NULL"
					+ "WHERE localizacao='"+armario+"';";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			//Apagar localização
			sql="DELETE FROM unidadelocalizacao "
					+ "WHERE armario='"+armario+"';";
			System.out.println(sql);
			stmt.executeUpdate(sql);

		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public void ligarLocalizacao(int idsuporte,char armario,int prateleira) throws IOException {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Associar localização
			String sql="SELECT * from unidadelocalizacao "
					+ "WHERE armario='"+armario+"' AND prateleira="+prateleira+";";
			System.out.println(sql);
			ResultSet rs= stmt.executeQuery(sql);
			
			int idlocalizacao = 0;
			
			while(rs.next()){				
				idlocalizacao=rs.getInt("id");
			}
			
			sql="UPDATE suportefisico "
					+ "SET localizacao="+idlocalizacao+" "
					+ "WHERE id="+idsuporte+";";
			System.out.println(sql);
			stmt.executeUpdate(sql);

		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public void adicionarEmprestimo(int idsuporte,String pessoa, String dataemp, String dataret) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();		
			//Adicionar emprestimo
			String sql="INSERT INTO emprestimo(suporte,pessoa,data_emp,data_ret) VALUES "
				+ "("+idsuporte+",'"+pessoa+"','"+dataemp+"','"+dataret+"');";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	public void listarEmprestimos(ArrayList<Emprestimo> emprestimos) throws IOException {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();
			//Listar emprestimos
			String sql="SELECT emprestimo.id,filme.titulo,tiposuporte.nome,emprestimo.pessoa,emprestimo.data_emp,emprestimo.data_ret from emprestimo "
					+ "INNER JOIN suportefisico ON suportefisico.id=emprestimo.suporte "
					+ "INNER JOIN suporte ON suportefisico.id=suporte.id "
					+ "INNER JOIN tiposuporte ON tiposuporte.id=suporte.tipo "
					+ "INNER JOIN filme ON filme.id=suporte.filme;";
			System.out.println(sql);
			ResultSet rs= stmt.executeQuery(sql);
			
			while(rs.next()){				
				Emprestimo emprestimo = new Emprestimo();
				emprestimo.id=rs.getInt("id");
				emprestimo.filme=rs.getString("titulo");
				emprestimo.tipo=rs.getString("nome");
				emprestimo.pessoa=rs.getString("pessoa");
				emprestimo.dataemp=rs.getString("data_emp");
				emprestimo.dataret=rs.getString("data_ret");	
				
				emprestimos.add(emprestimo);
			}

		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
  }
	
	public void alterarEmprestimo(int idemprestimo,String pessoa, String dataemp, String dataret) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();		
			//Alterar empréstimo
			String sql="UPDATE emprestimo "
					+ "SET pessoa='"+pessoa+"' AND data_emp='"+dataemp+"' AND data_ret='"+dataret+"' "
					+ "WHERE id="+idemprestimo+";";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	public void apagarEmprestimo(int idemprestimo) {
		Connection con = this.obterLigacao();
		try{			
			Statement stmt=con.createStatement();		
			//Apagar empréstimo
			String sql="DELETE FROM emprestimo "
					+ "WHERE id="+idemprestimo+";";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
		}
		catch(SQLException sqle){
			System.out.println("SQLException");
		}
	}
	
	
}