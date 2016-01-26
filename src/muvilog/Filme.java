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

import javax.imageio.ImageIO;

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
	
	Filme() throws IOException {
		
	}
	
	public Filme(int id, String imdbid, String titulo, int ano, int duracao,
			String sinopse, String poster, int metascore, float imdbrating,int tipo) throws IOException {

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
	}
	
	public Boolean receberFilme(String tituloPesquisa,String anoPesquisa) throws IOException{
			tituloPesquisa=tituloPesquisa.replaceAll(" ","+"); //Substituir espaços por +
			String sURL = "http://www.omdbapi.com/?t="+tituloPesquisa+"&y="+anoPesquisa+"&plot=short&r=json"; //Especificar link 
			
			//Conexão à página de internet
		    URL url = new URL(sURL);
		    HttpURLConnection request = (HttpURLConnection) url.openConnection();
		    request.connect();
		    //Utilização do plugin de JSON da Google
		    JsonParser jp = new JsonParser(); 
		    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent(), "UTF-8")); 
		    JsonObject rootobj = root.getAsJsonObject();
		    Boolean response = rootobj.get("Response").getAsBoolean();
		    System.out.println(response);
		   
		    if(response==true) { //Se existir, recebe informações
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
			    	anos = (rootobj.get("Year").getAsString()).split("–");
			    	ano = Integer.parseInt(anos[0]);
			    }
			    
			    String duracaotmp=rootobj.get("Runtime").getAsString();
			    duracao=Integer.parseInt(duracaotmp.replaceAll(" min","")); //Retira "min" para tornar inteiro
			    sinopse=rootobj.get("Plot").getAsString();
			    sinopse=sinopse.replaceAll("'","''"); //Apagar apostrofos para não causar problemas no SQL
			    String posterext=rootobj.get("Poster").getAsString();
			    if(posterext.equals("N/A")) { //Caso poster não exista, usar poster padrão
			    	poster="./posters/desconhecido.jpg";
			    }
			    else { //Caso exista, guardá-lo como ficheiro
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
