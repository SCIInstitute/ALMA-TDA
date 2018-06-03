/*
 *     saav-core - A (very boring) software development support library.
 *     Copyright (C) 2016 PAUL ROSEN
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You may contact the Paul Rosen at <prosen@usf.edu>.
 */
package usf.saav.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;


public class SystemX {

	public static void printProperties( ){
		Vector<String> keys = new Vector<String>();
		for( Entry<Object,Object> e : System.getProperties().entrySet() ){

			keys.add( (String)e.getKey() );
		}

		Collections.sort( keys );

		for( String k : keys ){
			System.out.printf("%s -> %s\n", k, System.getProperty(k) );
		}

	}


	public static String [] readFileContents( BufferedReader reader ) throws IOException {
		Vector<String> ret = new Vector<String>();
		 String line;
		 while( ( line = reader.readLine() ) != null ) {
	            ret.add(line);
	        }
		 reader.close();
		 return ret.toArray( new String[ret.size()] );
	}	


	public static String [] readFileContents( String filename ) throws IOException {
		return readFileContents( new BufferedReader(new FileReader( filename )) );
	}

	public static String [] readFileContents( URL filename ) throws IOException {
		return readFileContents( new BufferedReader( new InputStreamReader( filename.openStream() ) ) );
	}

	public static String [] readFileContents(File file) throws IOException {
		return readFileContents( new BufferedReader(new FileReader( file )) );
	}	
	
	public static String [] readFileContents(InputStream input) throws IOException {
		return SystemX.readFileContents( new BufferedReader(new InputStreamReader(input) ) );
	}
	
	
	
	public static String executeCommand(String command ) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
	

	public static String executeCommand(String ... command ) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = (new ProcessBuilder(command)).start();
			p.waitFor( );
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}	
	
	public static boolean fileExists( String filename ) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader( filename ));
			 reader.close();
	    } catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return true;
	}
	
	public static String getOS( ){
		return System.getProperty("os.name");
	}

	public static boolean isWindows( ){
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}

	public static boolean isMac( ){
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}

	public static boolean isLinux( ){
		return System.getProperty("os.name").toLowerCase().contains("linux");
	}

	public static String[] getJavaLibraryPath( ){
		String str = System.getProperty("java.library.path");
		String path_sep = System.getProperty("path.separator");
		return str.split(path_sep);
	}

	public static String getFileSeperator( ){
		return System.getProperty("file.separator");
	}

	public static String getJNIExtension() {
		if( isWindows() ) return ".dll";
		if( isMac() ) return ".jnilib";
		if( isLinux() ) return ".so"; 
		return "";
	}

	public static boolean loadLibrary(String lib, boolean verbose) {
		for( String path : SystemX.getJavaLibraryPath() ){
			try{ 
				System.load(path + SystemX.getFileSeperator() + lib + SystemX.getJNIExtension() );
				if(verbose) System.out.printf("Loaded library: " + path + SystemX.getFileSeperator() + lib + SystemX.getJNIExtension() + "\n");
				return true;
			}
			catch ( UnsatisfiedLinkError e ){
				if(verbose) System.out.printf("Cannot load library: " + path + SystemX.getFileSeperator() + lib + SystemX.getJNIExtension() + "\n");
			}
		}
		System.out.printf("Could not find library: " + lib + SystemX.getJNIExtension() + "\n");
		return false;
	}
	
	 /**
     * Loads library from current JAR archive
     * 
     * The file from JAR is copied into system temporary directory and then loaded. The temporary file is deleted after exiting.
     * Method uses String as filename because the pathname is "abstract", not system-dependent.
     * 
     * @param filename The filename inside JAR as absolute path (beginning with '/'), e.g. /package/File.ext
     * @throws IOException If temporary file creation or read/write operation fails
     * @throws IllegalArgumentException If source file (param path) does not exist
     * @throws IllegalArgumentException If the path is not absolute or if the filename is shorter than three characters (restriction of {@see File#createTempFile(java.lang.String, java.lang.String)}).
     */
    public static void loadLibraryFromJar(String path, String lib) throws IOException {
    	
    	String jarPath = path + "/" + lib + SystemX.getJNIExtension();
 
        if (!jarPath.startsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }
 
        // Obtain filename from path
        String[] parts = jarPath.split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;
 
        // Split filename to prexif and suffix (extension)
        String prefix = "";
        String suffix = null;
        if (filename != null) {
            parts = filename.split("\\.", 2);
            prefix = parts[0];
            suffix = (parts.length > 1) ? "."+parts[parts.length - 1] : null; // Thanks, davs! :-)
        }
 
        // Check if the filename is okay
        if (filename == null || prefix.length() < 3) {
            throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
        }
 
        // Prepare temporary file
        File temp = File.createTempFile(prefix, suffix);
        temp.deleteOnExit();
 
        if (!temp.exists()) {
            throw new FileNotFoundException("File " + temp.getAbsolutePath() + " does not exist.");
        }
 
        // Prepare buffer for data copying
        byte[] buffer = new byte[1024];
        int readBytes;
 
        // Open and check input stream
        InputStream is = SystemX.class.getResourceAsStream(jarPath);
        if (is == null) {
            throw new FileNotFoundException("File " + jarPath + " was not found inside JAR.");
        }
 
        // Open output stream and copy data between source file in JAR and the temporary file
        OutputStream os = new FileOutputStream(temp);
        try {
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } finally {
            // If read/write fails, close streams safely before throwing an exception
            os.close();
            is.close();
        }
 
        // Finally, load the library
        System.load(temp.getAbsolutePath());
    }
    
	private static String override_temp_dir = null;

	public static String getTempDirectory( ){
		if( override_temp_dir != null ) 
			return override_temp_dir;
		return System.getProperty("java.io.tmpdir");
	}
	
	public static void setTempDirectory( String path ){
		override_temp_dir = path;
	}

	private final static Random random = new Random();

	public static char RandomCharacter( ){
		int i = random.nextInt(37);
		if( i < 26 ) return (char) ('a'+i);
		if( i < 36 ) return (char) ('0'+i-26);
		return '_';
	}

	public static String RandomString(int length) {
		String ret = "";
		for( int i = 0; i < length; i++ ){
			ret += RandomCharacter( );
		}
		return ret;
	}


	public static String readFileContentsAsString(String filename) throws IOException {
		StringBuffer ret = new StringBuffer();
		for( String s : readFileContents(filename) ){
			ret.append( s );
		}
		return ret.toString();
	}

}
