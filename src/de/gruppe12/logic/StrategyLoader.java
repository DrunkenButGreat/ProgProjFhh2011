package de.gruppe12.logic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.gruppe12.ki.MoveStrategy;


public class StrategyLoader {
	
	/** listContent
	 * 
	 * Öffnet eine Zip oder Jar datei und gibt ihren Inahlt aus
	 * 
	 * @param uri 	: Pfad zur Datei
	 * @return		: Eine Arraylist mit alle Dateien samt relativer Pfade
	 * @throws IOException
	 */
	public static ArrayList<String> listContent(String uri) throws IOException{
		
		ArrayList<String> ar = new ArrayList<String>();
		
		//Jar öffnen und Durchsuchen
		 try {
	          ZipFile zf=new ZipFile(uri);
	          Enumeration<? extends ZipEntry> e=zf.entries();
	          while (e.hasMoreElements()) {
	              ZipEntry ze=(ZipEntry)e.nextElement();
	                 ar.add(ze.toString());
	          }
	          zf.close();
		 } finally {
			 
		 }
	     
	     return ar;
	    
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
    	
    	int len = filter.length(), endIndex, startIndex;
    	String temp;
    	
    	for(int i=0; i<ar.size(); i++){
    		//Dateiendung ermitteln
    		temp = ar.get(i);
    		endIndex = temp.length();
    		startIndex = endIndex -len;
   	 		temp =temp.substring(startIndex, endIndex);
   	 		
   	 		//Wenn nicht Dateityp dann entfernen
   	 		if(!temp.equals(filter)) ar.remove(i);
   	 	}
   	 	
   	 	return ar;
    }
    
    static Class getClass( String path, String classname ) throws Exception 
    { 
    	 @SuppressWarnings("deprecation")
		URL jarURL = new File(path).toURI().toURL();
         
         //Entweder so
         ClassLoader classLoader =    new URLClassLoader(new URL[]{jarURL});
                 
         return classLoader.loadClass(classname);
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
    static MoveStrategy getStrategy( String path, String classname ) throws Exception 
    {
    	return (MoveStrategy) getClass( path, classname ).newInstance();
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
