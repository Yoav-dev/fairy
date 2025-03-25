package fairy.core.three;

import java.util.HashMap;
import java.util.Map;

public class Children<I extends ItemProperties> {
	private Map<String, AFile<I>> m_FilesChildren = new HashMap<String, AFile<I>>();
	private Map<String, ADirectory<I>> m_DirectoriesChildren = new HashMap<String, ADirectory<I>>();
	
	void addFile(String i_Name, AFile<I> i_FileItem) {
		m_FilesChildren.put(i_Name, i_FileItem);
	}
	
	void addDirectory(String i_Name, ADirectory<I> i_DirectoryItem) {
		m_DirectoriesChildren.put(i_Name, i_DirectoryItem);
	}
	
	Map<String, AFile<I>> getFilesChildren() {
		return m_FilesChildren;
	}
	
	Map<String, ADirectory<I>> getDirectoriesChildren() {
		return m_DirectoriesChildren;
	}
}