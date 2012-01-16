/** StrategyLoader
 * 
 * L�d Strategies aus einer Jar oder dem Classpath
 * 
 * Copyright: (c) 2011 <p>
 * Company: Gruppe 12 <p>
 * @author Julian Kipka
 * @version 2011.12.01
 */

package de.gruppe12.logic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;


public class StrategyLoader {
	
			
	/** listContentOfJar
	 * 
	 * @deprecated
	 * 
	 * �ffnet eine Zip oder Jar datei und gibt ihren Inahlt aus
	 * 
	 * @param uri 	: Pfad zur Datei
	 * @return		: Eine Arraylist mit alle Dateien samt relativer Pfade
	 */
	private static ArrayList<String> listContentOfJar(String uri){
		return listContentOfJar(uri,"");
	    
	}
	
	/** listContentOfJar
	 * 
	 * @param extension
	 * @param uri 	: Pfad zur Datei
	 * @return		: Eine Arraylist mit alle Dateien samt relativer Pfade
	 */
	private static ArrayList<String> listContentOfJar(String uri, String extension){
		
		ArrayList<String> ar = new ArrayList<String>();
		
		//Jar �ffnen und Durchsuchen
		 try {
	          ZipFile zf=new ZipFile(uri);
	          Enumeration<? extends ZipEntry> e=zf.entries();
	          while (e.hasMoreElements()) {
	              ZipEntry ze=(ZipEntry)e.nextElement();
	              if(extension.length()==0) 
	            	  ar.add(ze.toString());
	              else 
	            	  ar.add(extension+"-"+ze.toString());
	          }
	          zf.close();	       
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	     
	     return ar;
	}
	
	/** listContent
	 * 
	 * Testet ob der Pfad eine Datei ist oder ein Ornder und Listet entweder alle klasses in allen Jars oder nur in einer Jar
	 * 
	 * @param uri
	 * @return
	 */
	public static ArrayList<String> listContent(String uri){
		File dir = new File(uri);
		if(!dir.isDirectory()) return listContentOfJar(uri,dir.getName());
		
		ArrayList<String> classNames = new ArrayList<String>();
		
		if(dir.isDirectory()){
			for(File file : dir.listFiles()){
				classNames.addAll(listContentOfJar(file.getAbsolutePath(),file.getName()));
			}
		}
		
		return classNames;
	}
	
	
	/** filterExtension
	 * 
	 * Filtert eine ArrayList nach einer bestimmten Dateiendung
	 * 
	 * @param ar		: ArrayList zum durchsuchen
	 * @param filter	: Endung der Datei / In unserem Fall class
	 * @return			: ArrayList nur mit der vorgegebenen Endung
	 */
    public static ArrayList<String> filterExtension(ArrayList<String> ar, String filter){
    	ArrayList<String> newList = new ArrayList<String>();
    	
    	for (String s: ar){
   	 		//Wenn entsprechender Dateityp zur neuen Liste hinzufuegen
   	 		if(s.endsWith(filter)&&!s.contains("$")){
   	 			newList.add(s);
   	 		}
   	 	}
   	 	
   	 	return newList;
    }
    
    
    /** getStrategy
     * 
     * Aus der Class eine MoveStrategy Casten
     * 
     * @param path
     * @param classname
     * @return
     * @throws Exception
     */
    public static MoveStrategy getStrategy( String path, String classname ) throws Exception 
    {
    	 
    	 URL jarURL = new File(path+"/"+classname.substring(0,classname.indexOf('-'))).toURI().toURL();
    	 
    	 String binaryName= classname.substring(classname.indexOf('-')+1, classname.lastIndexOf('.'));
    	 binaryName= binaryName.replace('/', '.');
    	 
    	 ClassLoader classLoader =    new URLClassLoader(new URL[]{jarURL});

    	 return (MoveStrategy) classLoader.loadClass(binaryName).newInstance();
    }
    
    
    /**getFromClassPath
     * 
     * Simple way um Classen aus dem Classpath zu laden.
     * 
     * @param name
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    static MoveStrategy getFromClassPath(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
    	final Class<?> clazz = Class.forName(name);
		return (MoveStrategy) clazz.newInstance();
    }   
    
    
}
