package muvilog;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Filme {
	int id;
	String imdbid;
	String titulo;
	int ano;
	int duracao;
	String sinopse;
	String poster;
	int metascore;
	float imdbrating;
	int tipo;
	String generos[];
	String paises[];
	String atores[];
	String realizadores[];
	int anofim;
	
	Filme() throws IOException {
		
	}
	
	public Filme(int id, String imdbid, String titulo, int ano, int duracao,
			String sinopse, String poster, int metascore, float imdbrating,int tipo, int anofim) throws IOException {

		this.id = id;
		this.imdbid = imdbid;
		this.titulo = titulo;
		this.ano = ano;
		this.duracao = duracao;
		this.sinopse = sinopse;
		this.poster = poster;
		this.metascore = metascore;
		this.imdbrating = imdbrating;
		this.tipo = tipo;
		this.anofim = anofim;
	}
	
	public Filme(String imdbid, String titulo, int ano, int tipo) throws IOException {

		this.imdbid = imdbid;
		this.titulo = titulo;
		this.ano = ano;
		this.tipo = tipo;
	}
	
	public Filme(String imdbid, String titulo, int ano, int tipo, int anofim) throws IOException {

		this.imdbid = imdbid;
		this.titulo = titulo;
		this.ano = ano;
		this.tipo = tipo;
		this.anofim = anofim;
	}
	
	//To implement into UI
	public ArrayList<Filme> searchFilm(String search) throws IOException {
		ArrayList<Filme> filmlist = new ArrayList<Filme>();
		search=search.replaceAll(" ","+"); //Substituir espa�os por +
		String sURL = "http://www.omdbapi.com/?s="+search+"&r=json"; //Especificar link 
		
		//Conex�o � p�gina de internet
	    URL url = new URL(sURL);
	    HttpURLConnection request = (HttpURLConnection) url.openConnection();
	    request.connect();
	    //Utiliza��o do plugin de JSON da Google
	    JsonParser jp = new JsonParser(); 
	    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent(), "UTF-8")); 
	    JsonObject rootobj = root.getAsJsonObject();
	    Boolean response = rootobj.get("Response").getAsBoolean();
	    System.out.println(response);
	    JsonArray searchlist = rootobj.get("Search").getAsJsonArray();
	   
	    if(response==true) { //Se existir, recebe informa��es
	    	for (int i = 0; i<searchlist.size();i++) {
	    		JsonElement filmelem = searchlist.get(i);
	    		JsonObject filmobj = filmelem.getAsJsonObject();
	    		tipo = new LigacaoBD().verificarTipo(filmobj.get("Type").getAsString());
			    System.out.println(tipo);
			    if (tipo==0) break;
		    	imdbid=filmobj.get("imdbID").getAsString();
			    titulo=filmobj.get("Title").getAsString();
			    System.out.println(titulo);
			    if(tipo==1) {
			    	 ano=filmobj.get("Year").getAsInt();
			    	 filmlist.add(new Filme(imdbid,titulo,ano,tipo));
			    }
			    else {
			    	System.out.println(filmobj.get("Year").getAsString());
			    	String anos[];
			    	anos = (filmobj.get("Year").getAsString()).split("�");
			    	ano = Integer.parseInt(anos[0]);
			    	System.out.println(ano);
			    	if(anos.length>1)
			    		anofim = Integer.parseInt(anos[1]);
			    	else
			    		anofim = 0;
				    filmlist.add(new Filme(imdbid,titulo,ano,tipo,anofim));
			    }
	    	}
	    }
	    return filmlist;
	}
	
	public Boolean receberFilme(String tituloPesquisa,String anoPesquisa) throws IOException{
			tituloPesquisa=tituloPesquisa.replaceAll(" ","+"); //Substituir espa�os por +
			String sURL = "http://www.omdbapi.com/?t="+tituloPesquisa+"&y="+anoPesquisa+"&plot=short&r=json"; //Especificar link 
			
			//Conex�o � p�gina de internet
		    URL url = new URL(sURL);
		    HttpURLConnection request = (HttpURLConnection) url.openConnection();
		    request.connect();
		    //Utiliza��o do plugin de JSON da Google
		    JsonParser jp = new JsonParser(); 
		    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent(), "UTF-8")); 
		    JsonObject rootobj = root.getAsJsonObject();
		    Boolean response = rootobj.get("Response").getAsBoolean();
		    System.out.println(response);
		   
		    if(response==true) { //Se existir, recebe informa��es
		    	tipo = new LigacaoBD().verificarTipo(rootobj.get("Type").getAsString());
			    System.out.println(tipo);
			    if (tipo==0) return false;
		    	imdbid=rootobj.get("imdbID").getAsString();
			    titulo=rootobj.get("Title").getAsString();
			    if(tipo==1) {
			    	 ano=rootobj.get("Year").getAsInt();
			    }
			    else {
			    	System.out.println(rootobj.get("Year").getAsString());
			    	String anos[];
			    	anos = (rootobj.get("Year").getAsString()).split("�");
			    	ano = Integer.parseInt(anos[0]);
			    	anofim = Integer.parseInt(anos[1]);
			    }
			    
			    String duracaotmp=rootobj.get("Runtime").getAsString();
			    duracao=Integer.parseInt(duracaotmp.replaceAll(" min","")); //Retira "min" para tornar inteiro
			    sinopse=rootobj.get("Plot").getAsString();
			    sinopse=sinopse.replaceAll("'","''"); //Apagar apostrofos para n�o causar problemas no SQL
			    String posterext=rootobj.get("Poster").getAsString();
			    if(posterext.equals("N/A")) { //Caso poster n�o exista, usar poster padr�o
			    	poster="./posters/desconhecido.jpg";
			    }
			    else { //Caso exista, guard�-lo como ficheiro
			    	poster="./posters/"+tituloPesquisa+".jpg";
			    	BufferedImage posterimg = ImageIO.read(new URL(posterext));
			    	AffineTransform tx = new AffineTransform();
			    	double altura = 400;
			    	double scale = altura/(double)posterimg.getHeight();
			        tx.scale(scale, scale);

			        AffineTransformOp op = new AffineTransformOp(tx,
			            AffineTransformOp.TYPE_BICUBIC);
			        posterimg = op.filter(posterimg, null);
				    ImageIO.write(posterimg,"jpg",new File(poster));
			    }

			    if("N/A".equals(rootobj.get("Metascore").getAsString())) { //Se o metascore for N/A, usar 0
			    	metascore=0;
			    }
			    else {
			    	metascore=rootobj.get("Metascore").getAsInt();
			    }
			    imdbrating=rootobj.get("imdbRating").getAsFloat();
			    
			    
			    
			    
			    //Separar generos, paises, atores e realizadores
			    generos = (rootobj.get("Genre").getAsString()).split(", "); 
			    paises = (rootobj.get("Country").getAsString()).split(", ");
			    atores = (rootobj.get("Actors").getAsString()).split(", ");
			    realizadores = (rootobj.get("Director").getAsString()).split(", ");
			    
			    return true;
		    }
		    else {
		    	return false;
		    }
		    
	}
	
	
	
}
