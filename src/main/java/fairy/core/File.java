package fairy.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeMap;

public class File extends AItem {
	protected TreeMap<Long, InputStream> m_ContentsHistory = new TreeMap<Long, InputStream>();
	
	File(String i_Name, Directory i_Parent, AFileSystem i_FileSystem) throws Throwable {
		super(i_Name, i_Parent, i_FileSystem, true, false);
		this.uniqueProperties = new FileUniqueProperties(getCreationTime(), i_FileSystem.getPathId(this.getPath()), Utils.getCheckSum(getInputStream()));
		m_History.get(m_History.lastKey()).setSize(m_FileSystem.readFileSize(this.getPath()));
	}

	public InputStream getInputStream() throws Throwable {
		return m_FileSystem.getFileInputStream(this.getPath());
	}
	
	public OutputStream getOutputStream() throws Throwable {
		return m_FileSystem.getFileOutputStream(this.getPath());
	}

	void updateContent() throws Throwable {
		long timeStamp = System.currentTimeMillis();
		long oldSize = getSize();
		long sizesDiff = oldSize - readSize();
		Directory parent = getParent();
		
		while (parent != null) {
			parent.updateSize(parent.getSize() + sizesDiff, timeStamp);
			parent = parent.getParent();
		}
	}
	
	public long readSize() throws Throwable {
		return m_FileSystem.readFileSize(this.getPath());
	}
	
	FileUniqueProperties getUniqueProperties() {
		return (FileUniqueProperties) super.getUniqueProperties();
	}
}