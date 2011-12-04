package de.gruppe12.logic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.fhhannover.inform.hnefatafl.vorgaben.MoveStrategy;


public class StrategyLoader {
	
	/** listContent
	 * 
	 * �ffnet eine Zip oder Jar datei und gibt ihren Inahlt aus
	 * 
	 * @param uri 	: Pfad zur Datei
	 * @return		: Eine Arraylist mit alle Dateien samt relativer Pfade
	 * @throws IOException
	 */
	public static ArrayList<String> listContent(String uri) throws IOException{
		
		ArrayList<String> ar = new ArrayList<String>();
		
		//Jar �ffnen und Durchsuchen
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
    	
    	for(int i=0; i<ar.size(); i++){
   	 		//Wenn nicht Dateityp dann entfernen
   	 		if(!ar.get(i).endsWith(filter)) ar.remove(i);
   	 	}
   	 	
   	 	return ar;
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
    	 @SuppressWarnings("deprecation")
 		URL jarURL = new File(path).toURI().toURL();
          
          ClassLoader classLoader =    new URLClassLoader(new URL[]{jarURL});
                  
          return (MoveStrategy) classLoader.loadClass(classname).newInstance();
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
