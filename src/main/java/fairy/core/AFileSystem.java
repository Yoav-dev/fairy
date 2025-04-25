package fairy.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Represents a file system, such as local file system on hard drive, remote file system on cloud, etc
 */
public abstract class AFileSystem {
	// Root directories, such as c:, d:, etc in Windows, / in Linux, etc
	private Map<String, Directory> m_RootDirectories = new HashMap<String, Directory>();
	protected abstract Collection<String> getRootsNames();
	
	private TreeMap<ItemUniqueProperties, AItem> items = new TreeMap<ItemUniqueProperties, AItem>();
	
	// Monitors items (Files or directories) for changes as modifications, adding/deleting sub-items to/from directory, etc 
	private AItemsMonitor m_ItemsMonitor;
		
	protected AFileSystem(AItemsMonitor i_AItemsMonitor) {
		m_ItemsMonitor = i_AItemsMonitor;
		m_ItemsMonitor.setFileSystem(this);
		
		//*
		// Start from reading root directories
		// These can be used later to read all sub-items recursively
		Collection<String> rootsNames = getRootsNames();
		
		if (rootsNames == null || rootsNames.size() == 0) {
			return;
		}

		for (String rootName : rootsNames) {
			Directory rootDirectory = new Directory(rootName, null, this);
			m_RootDirectories.put(rootName, rootDirectory);
		}
		//*
		
		m_ItemsMonitor.startMonitor();
	}

	public AItemsMonitor getItemsMonitor() {
		return m_ItemsMonitor;
	}
	
	Directory getDirectory(Path i_Path) throws Throwable {
		ItemUniqueProperties directoryUniqueProperties = new DirectoryUniqueProperties(this.getCreationTime(i_Path), this.getPathId(i_Path), this.getChildrenIds(i_Path));
		AItem item = getItem(directoryUniqueProperties);
		
		if (item == null) {
			return null;
		}
		
		return (Directory) item;
	}
	
	void addItem(AItem i_Item) {
		this.items.put(i_Item.getUniqueProperties(), i_Item);
	}
	
	File getFile(Path i_Path) throws Throwable {
		ItemUniqueProperties fileUniqueProperties = new FileUniqueProperties(this.getCreationTime(i_Path), this.getPathId(i_Path), Utils.getCheckSum(this.getFileInputStream(i_Path)));
		AItem item = getItem(fileUniqueProperties);
		
		if (item == null) {
			return null;
		}
		
		return (File) item;
	}
		
	AItem getItem(ItemUniqueProperties i_ItemUniqueProperties) {

			if (this.items.containsKey(i_ItemUniqueProperties)) {
				return this.items.get(i_ItemUniqueProperties);
			}

		return null;
	}
	
	void removeItemId(AItem i_Item) {
		this.items.remove(i_Item.getUniqueProperties());
	}

	/**
	 * 
	 * @param i_Name - directory name
	 * @return root {link: Directory} whose name is i_Name, or null if so such root {link: Directory} exists
	 * @throws Throwable 
	 */
	public Directory getDirectory(String i_Name) {
		return m_RootDirectories.get(i_Name);
	}
	
	protected abstract InputStream getFileInputStream(Path i_Path) throws Throwable;
	protected abstract OutputStream getFileOutputStream(Path i_Path) throws Throwable;
	protected abstract long readFileSize(Path i_Path) throws Throwable;	
	protected abstract Collection<String> getChildrenNames(Path i_Path);
	protected abstract Collection<String> getChildrenIds(Path i_Path);
	protected abstract String getPathId(Path i_Path);
	protected abstract boolean isDirectory(Path i_Path) throws Throwable;
	protected abstract boolean isLink(Path i_Path) throws Throwable;
	protected abstract long getCreationTime(Path i_Path);
	protected abstract void addFile(Path i_ParentPath, String i_Name) throws IOException;
	protected abstract void addDirectory(Path i_ParentPath, String i_Name);
}