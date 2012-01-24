/** StrategyLoader
 * 
 * Laed Strategies aus einer Jar oder dem Classpath
 * 
 * Copyright: (c) 2011 <p>
 * Company: Gruppe 12 <p>
 * @author Julian Kipka
 * @version 2011.12.01
 */

package de.gruppe12.logic;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;


public class StrategyLoader {
	
	/** listContent
	 * 
	 * Oeffnet eine Zip oder Jar Datei und gibt ihren Inhalt aus
	 * 
	 * @param uri 	: Pfad zur Datei
	 * @return		: Eine Arraylist mit alle Dateien samt relativer Pfade
	 * @throws IOException
	 */
	public static ArrayList<String> listContent(String uri) throws IOException{
		return listContent(uri,"");
	    
	}
	
	public static ArrayList<String> listContent(String uri, String extension){
		
		ArrayList<String> ar = new ArrayList<String>();
		
		if(uri!=null&&uri.contains("class")){
			File f = new File(uri);
			ar.add(f.getName());
			return ar;
		}
		
		//Jar �ffnen und Durchsuchen
		 try {
	          ZipFile zf=new ZipFile(uri);
	          Enumeration<? extends ZipEntry> e=zf.entries();
	          while (e.hasMoreElements()) {
	              ZipEntry ze=(ZipEntry)e.nextElement();
	                 ar.add(extension+/*"-"+*/ze.toString());
	          }
	          zf.close();	       
		 } catch (Exception e) {
		 }
	     
	     return ar;
	}
	
	public static ArrayList<String> listContent(String uri, Boolean folder) throws IOException{
		if(!folder) return listContent(uri);
		
		ArrayList<String> classNames = new ArrayList<String>();
		
		File dir = new File(uri);
		if(dir.isDirectory()){
			for(File file : dir.listFiles()){
				classNames.addAll(listContent(file.getAbsolutePath(),file.getName()));
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
    public static MoveStrategy getStrategy( String path, String classname )
    {
    	if(path.contains(classname)){
    		if(path.contains("de")) classname = path.substring(path.indexOf("de"),path.length()).replace("\\","/");
    		if(path.contains("com")) classname = path.substring(path.indexOf("com"),path.length()).replace("\\","/");
    		if(path.contains("org")) classname = path.substring(path.indexOf("org"),path.length()).replace("\\","/");
    		
    	} else classname = classname.substring(0);
    	
    	try{
	    	 URL jarURL = new File(path).toURI().toURL();
	    	 
	    	 String binaryName= classname.substring(0, classname.lastIndexOf('.'));
	    	 binaryName= binaryName.replace('/', '.');
	    	 
	    	 ClassLoader classLoader =    new URLClassLoader(new URL[]{jarURL});
	
	    	 return (MoveStrategy) classLoader.loadClass(binaryName).newInstance();
    	
    	} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	return null;
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
