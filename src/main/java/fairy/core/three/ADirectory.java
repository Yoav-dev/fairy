package fairy.core.three;

import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

/**
 * Represents a directory
 * Can be on either local or remote environment
 */
public abstract class ADirectory<I extends ItemProperties> {
	private Map<String, AFile<I>> m_SubFiles;
	private Map<String, ADirectory<I>> m_SubDirectories;
	private boolean m_ChildrenRead = false;
	private ItemProperties m_Item;
	
	protected ADirectory(ItemProperties i_Item) throws Throwable {
		m_Item = i_Item;
		m_Item.setIsFile(false);
	}
	
	protected abstract Children<I> readChildren() throws Throwable;
	
	private void prepareChildren() throws Throwable {
		if (!m_ChildrenRead) {
			Children<I> children = readChildren();
			m_SubFiles = children.getFilesChildren();
			m_SubDirectories = children.getDirectoriesChildren();
			m_ChildrenRead = true;
		}
	}
	
	public boolean directoryExists(String i_Name) throws Throwable {
		prepareChildren();
		
		return m_SubDirectories.containsKey(i_Name);
	}
	
	public boolean fileExists(String i_Name) throws Throwable {
		prepareChildren();
		
		return m_SubFiles.containsKey(i_Name);
	}
	
	public ADirectory<I> getDirectory(String i_Name) throws Throwable {
		prepareChildren();
		
		return m_SubDirectories.get(i_Name);
	}
	
	AFile<I> getFile(String i_Name) throws Throwable {
		prepareChildren();
		
		return m_SubFiles.get(i_Name);
	}
	
	protected abstract AFile<I> createFile(String i_Name, ADirectory<I> i_Parent) throws Throwable;
	protected abstract ADirectory<I> createDirectory(String i_Name, ADirectory<I> i_Parent) throws Throwable;
	
	public AFile<I> addFile(String i_Name) throws Throwable {
		if (fileExists(i_Name)) {
			throw new FileAlreadyExistsException("File " + i_Name + " already exists");
		}
		
		return createFile(i_Name, this);
	}
	
	public ADirectory<I> addDirectory(String i_Name) throws Throwable {
		if (directoryExists(i_Name)) {
			throw new FileAlreadyExistsException("Directory " + i_Name + " already exists");
		}
		
		return createDirectory(i_Name, this);
	}

	protected long calculateSize() throws Throwable {
		prepareChildren();
		long size = 0;
		
		for (AFile<I> file : m_SubFiles.values()) {
			size += file.getItem().getSize();
		}
		
		for (ADirectory<I> directory : m_SubDirectories.values()) {
			size += directory.getItem().getSize();
		}
		
		return size;
	}

	public ItemProperties getItem() {
		return m_Item;
	}
}