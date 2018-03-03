package Gestion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Data.UsuTwitter;

public class Conexion {
	
	private final String direccion="localhost";
	private final String usuario="";
	private final String pass="";
	private final String db="ts3dinamicbot";
	
	public boolean estaUniqueId(String uniqueID){
		boolean esta=false;
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			ResultSet resul=sentencia.executeQuery("select uniqueid from usuarios where uniqueid='"+uniqueID+"'");
			
			while(resul.next()){
				esta=true;
			}
			
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return esta;
	}
	
	
	/**
	 * 
	 * Apartado Detector De Trolls y VPN
	 * 
	 */
	
	public boolean aniadirTroll(String ip,String uniqueid){
		
		if(!estaIP(ip)){
			Connection conexion=null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conexion = DriverManager.getConnection					                         
						 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
				
				Statement sentencia = conexion.createStatement();
				sentencia.executeUpdate("insert into trolls values('"+uniqueid+"','"+ip+"')");
				
				sentencia.close();
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println(e.getMessage());
			}finally{
				if(conexion!=null){
					try {
						conexion.close();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean estaIP(String ip){
		boolean esta=false;
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			ResultSet resul=sentencia.executeQuery("select ip from trolls where ip='"+ip+"'");
			String ipsacada="";
			
			while(resul.next()){
				ipsacada=resul.getString(1);
				
				if(ipsacada.length()>0 && ipsacada!=null){
					if(ipsacada.equalsIgnoreCase(ip)){
						esta=true;
					}
				}
			}
			
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return esta;
	}
	
	public String sacarPrimeraID(String ip){
		String id="";
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			ResultSet resul=sentencia.executeQuery("select uniqueid,ip from trolls where ip='"+ip+"'");
			String ipsacada="";
			
			while(resul.next()){
				ipsacada=resul.getString(2);
				
				if(ipsacada.length()>0 && ipsacada!=null){
					if(ipsacada.equalsIgnoreCase(ip)){
						id=resul.getString(1);
					}
				}
			}
			
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return id;
	}
	
	public boolean estaTroll(String uniqueId){
		boolean esta=false;
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			ResultSet resul=sentencia.executeQuery("select uniqueid from trolls where uniqueid='"+uniqueId+"'");
			String idSacada="";
			
			while(resul.next()){
				idSacada=resul.getString(1);
				
				if(idSacada.length()>0 && idSacada!=null){
					if(idSacada.equalsIgnoreCase(uniqueId)){
						esta=true;
					}
				}
			}
			
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return esta;
	}
	
	public void sacarDatosBD(){
		
		System.out.println("Sacando todos los datos de la BD");
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			ResultSet resul=sentencia.executeQuery("select * from trolls");
			
			while(resul.next()){
				System.out.println(resul.getString(1)+" - "+resul.getString(2));
			}
			
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	/**
	 * Apartado AutoRank
	 * 
	 */
	
	public void aniadirPunto(String uniqueid){
		if(estaUniqueId(uniqueid)){
			int puntos=getPuntosUsuario(uniqueid)+1;
			
			Connection conexion=null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conexion = DriverManager.getConnection					                         
						 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
				
				Statement sentencia = conexion.createStatement();
				sentencia.executeUpdate("update usuarios set puntos="+puntos+" where uniqueid='"+uniqueid+"'");
				
				sentencia.close();
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println(e.getMessage());
			}finally{
				if(conexion!=null){
					try {
						conexion.close();
					} catch (SQLException e) {
					}
				}
			}
		}else{
			Connection conexion=null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conexion = DriverManager.getConnection					                         
						 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
				
				Statement sentencia = conexion.createStatement();
				sentencia.executeUpdate("insert into usuarios(uniqueid,puntos) values('"+uniqueid+"',1)");
				
				sentencia.close();
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println(e.getMessage());
			}finally{
				if(conexion!=null){
					try {
						conexion.close();
					} catch (SQLException e) {
					}
				}
			}
		}
	}
	
	public int getPuntosUsuario(String uniqueId){
		int puntos=0;
		
		//Sacamos los puntos de ese usuario
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			ResultSet resul=sentencia.executeQuery("select puntos from usuarios where uniqueid='"+uniqueId+"'");
			
			while(resul.next()){
				puntos=resul.getInt(1);
			}
			
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return puntos;
	}
	
	public boolean eliminarTrollUniqueId(String uniqueID){
		boolean esta=false;
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			int resul=sentencia.executeUpdate("delete from trolls where uniqueid='"+uniqueID+"'");
			
			if(resul>0){
				esta=true;
			}
			
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
		return esta;
	}
	
	public ArrayList<String> sacarTodosLosTrollsIp(String UniqueId){
		ArrayList<String> todosTrolls=new ArrayList<String>();
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			ResultSet resul=sentencia.executeQuery("select ip from trolls where uniqueid='"+UniqueId.trim()+"'");
			String ip="";
			
			while(resul.next()){
				ip=resul.getString(1);
			}
			
			resul.close();
			
			ResultSet resul2=sentencia.executeQuery("select uniqueid from trolls where ip='"+ip.trim()+"'");
			
			while(resul2.next()){
				todosTrolls.add(resul2.getString(1));
			}
			
			resul2.close();
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
		return todosTrolls;
	}
	
	/**
	 * Verificador Twitter
	 */
	
	
	public boolean estaTwitter(String twitter){
		boolean esta=false;
		
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection					                         
					 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
			
			Statement sentencia = conexion.createStatement();
			ResultSet resul=sentencia.executeQuery("select twitter from usutwitter where twitter='"+twitter+"'");
			String twitterSacado="";
			
			while(resul.next()){
				twitterSacado=resul.getString(1);
				if(twitterSacado.equalsIgnoreCase(twitter)){
					esta=true;
				}
			}
			
			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(conexion!=null){
				try {
					conexion.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return esta;
	}
	public boolean añadirTwitter(UsuTwitter usuarioTwitter){
		
		if(!estaTwitter(usuarioTwitter.getTwitter())){
			Connection conexion=null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conexion = DriverManager.getConnection					                         
						 ("jdbc:mysql://"+direccion+"/"+db+"?user="+usuario+"&password="+pass); 
				
				Statement sentencia = conexion.createStatement();
				sentencia.executeUpdate("insert into usutwitter values('"+usuarioTwitter.getUniqueId()+"','"+usuarioTwitter.getTwitter()+"','"+usuarioTwitter.getAccesstoken()+"','"+usuarioTwitter.getTokensecret()+"')");
				
				sentencia.close();
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println(e.getMessage());
			}finally{
				if(conexion!=null){
					try {
						conexion.close();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			
			return true;
		}else{
			return false;
		}
		
	}
	
}
