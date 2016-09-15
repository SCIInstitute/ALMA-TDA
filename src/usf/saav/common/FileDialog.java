package usf.saav.common;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class FileDialog {

	public static File open_file(Component parent){
	  return open_file(parent,null);
	}

	public static File open_file(Component parent, FileFilter filter){
		  
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) { 
			e.printStackTrace();  
		}
	
		JFileChooser fc = new JFileChooser(); 
		if( filter != null) fc.setFileFilter( filter );
		
		if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) { 
			return fc.getSelectedFile(); 
		}
		return null; 
	}
	
	public static File open_directory(Component parent){
		  
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) { 
			e.printStackTrace();  
		}
	
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) { 
			return fc.getSelectedFile(); 
		}
		return null; 
	}
	

	public static class ExtensionFileFilter extends FileFilter {
		String description;

		String extensions[];

		public ExtensionFileFilter(String description, String extension) {
			this(description, new String[] { extension });
		}

		public ExtensionFileFilter(String description, String extensions[]) {
			if (description == null) {
				this.description = extensions[0];
			} else {
				this.description = description;
			}
			this.extensions = (String[]) extensions.clone();
			toLower(this.extensions);
		}

		private void toLower(String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
			}
		}

		public String getDescription() {
			return description;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String path = file.getAbsolutePath().toLowerCase();
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
						return true;
					}
				}
			}
			return false;
		}
	}

	
	public static class NameFileFilter extends FileFilter {
		String description;

		String names[];

		public NameFileFilter(String description, String ... names) {
			if (description == null) {
				this.description = names[0];
			} else {
				this.description = description;
			}
			this.names = (String[]) names.clone();
		}

		public String getDescription() {
			return description;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String name = file.getName();
				for( String s : names ){
					if( s.compareTo(name) == 0 ) 
						return true;
				}
			}
			return false;
		}
	}

}

