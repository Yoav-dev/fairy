package fairy.core;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Directory extends AItem {	
	private ConcurrentHashMap<String, AItem> m_Children = new ConcurrentHashMap<String, AItem>();
	
	static Path getSubItemPath(Directory i_Parent, String i_Name) {
		return i_Parent == null ? Path.of(i_Name) : i_Parent.getSubItemPath(i_Name);
	}
	
	protected Directory(String i_Name, Directory i_Parent, AFileSystem i_FileSystem) {
		super(i_Name, i_Parent, i_FileSystem, false, true);
		this.uniqueProperties = new DirectoryUniqueProperties(getCreationTime(), i_FileSystem.getPathId(this.getPath()), i_FileSystem.getChildrenIds(this.getPath()));
		readChildren();
		long size = 0;
		
		for (String name : m_Children.keySet()) {
			AItem child = m_Children.get(name);
			size += child.getSize();
		}
		
		m_History.get(m_History.lastKey()).setSize(size);
	}
	
	// Gets all directories and files under this directory
	private void readChildren() {
		Collection<String> childrenNames = m_FileSystem.getChildrenNames(this.getPath());
		
		for (String name : childrenNames) {
			try {
				readItem(name);
			} catch (Throwable e) {
//				System.out.println("Exception for " + getSubItemPath(name) + ": " + e.getClass());
			}
		}
	}
	
	public Collection<AItem> getChildren() {
		return m_Children.values();
	}
	
	private Path getSubItemPath(String i_Name) {
		return getPath().resolve(i_Name);
	}
	
	// Reads properties of item whose name is i_Name, under this directory
	AItem readItem(String i_Name) throws Throwable {	
		Path itemPath = getSubItemPath(i_Name);		
		
		if (m_FileSystem.isLink(itemPath)) {
			return new Link(i_Name, this, m_FileSystem, itemPath.toRealPath());
		}
		
		boolean isDirectory = m_FileSystem.isDirectory(itemPath);
		AItem item = isDirectory 
				? m_FileSystem.getDirectory(itemPath)
				: m_FileSystem.getFile(itemPath);
		
		if (item == null) {
			if (isDirectory) {
				item = new Directory(i_Name, this, m_FileSystem);
			} else {
				item = new File(i_Name, this, m_FileSystem);
			}
			
			m_FileSystem.addItem(item);
		} else {
			unreadItem(item.getName());
		}

		m_FileSystem.getItemsMonitor().registerDirectory((Directory)item, itemPath);
		m_Children.put(i_Name, item);
		
		return item;
	}
		
	AItem unreadItem(String i_Name) {
		AItem item = m_Children.get(i_Name);
		m_Children.remove(i_Name);
		
		return item;
	}
	
	public File addFile(String i_Name) throws Throwable {
		m_FileSystem.addFile(this.getPath(), i_Name);
		
		return this.getFile(i_Name);
	}
	
	public Directory addDirectory(String i_Name) throws Throwable {
		m_FileSystem.addDirectory(this.getPath(), i_Name);
		
		return this.getDirectory(i_Name);
	}
	
	public AItem getItem(String i_Name) throws Throwable {
		AItem item = null;
		
		if (!m_Children.containsKey(i_Name)) {
			item = this.readItem(i_Name);
		} else {
			item = m_Children.get(i_Name);
		}
		
		return item;
	}
	
	public boolean containsItem(String i_Name) throws Throwable {
		return m_Children.containsKey(i_Name);
	}
	
	/**
	 * i_Name - file name
	 * @param i_Name
	 * @return sub {link: File} of this Directory whose name is i_Name, or null if so such {link: File} exists
	 * @throws Throwable
	 */
	public File getFile(String i_Name) throws Throwable {
		AItem item = getItem(i_Name);
		
		if (item instanceof File) {
			return (File) item;
		}
		
		return null;
	}
	
	/**
	 * i_Name - directory name
	 * @param i_Name
	 * @return sub {link: Directory} of this Directory whose name is i_Name, or null if so such {link: Directory} exists
	 * @throws Throwable
	 */
	public Directory getDirectory(String i_Name) throws Throwable {
		AItem item = getItem(i_Name);
		
		if (item instanceof Directory) {
			return (Directory) item;
		}
		
		return null;
	}
	
	DirectoryUniqueProperties getUniqueProperties() {
		return (DirectoryUniqueProperties) super.getUniqueProperties();
	}
}