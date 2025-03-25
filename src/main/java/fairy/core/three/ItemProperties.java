package fairy.core.three;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Holds common properties of items, both files and directories
 * Can be on either local or remote environment
 */
public abstract class ItemProperties {
	protected long m_PrevLastModified = -1;
	private String m_Name;
	protected ArrayList<String> m_FullPath;
	protected int m_FullPathLastIndex;
	private ADirectory<?> m_Parent;
	private long m_Size;
	private Iterator<String> m_FullPathIterator;
	private Iterator<String> m_FullPathReverseIterator;
	private boolean m_IsFile;
	
	protected ItemProperties(String i_Name, ADirectory<?> i_Parent) {
		m_Name = i_Name;
		setParent(i_Parent);
	}
	
	void setIsFile(boolean i_IsFile) {
		m_IsFile = i_IsFile;
	}
	
	boolean isFile() {
		return m_IsFile;
	}
	
	private void setParent(ADirectory<?> i_Parent) {
		m_Parent = i_Parent;
		
		if (m_Parent == null) {
			m_FullPath = new ArrayList<String>();
		} else {
			m_FullPath = m_Parent.getItem().m_FullPath;
		}
		
		m_FullPath.add(m_Name);
		m_FullPathLastIndex = m_FullPath.size() - 1;
		m_FullPathIterator = new Iterator<String>() {
			int index = 0;
			
			@Override
			public String next() {
				return m_FullPath.get(++index);
			}
			
			@Override
			public boolean hasNext() {
				return index < m_FullPathLastIndex;
			}
		};
		
		m_FullPathReverseIterator = new Iterator<String>() {
			int index = m_FullPathLastIndex;
			
			@Override
			public String next() {
				return m_FullPath.get(--index);
			}
			
			@Override
			public boolean hasNext() {
				return index > 0;
			}
		};
	}
	
	public Iterator<String> getFullPathIterator() {
		return m_FullPathIterator;
	}
	
	public Iterator<String> getFullPathReverseIterator() {
		return m_FullPathReverseIterator;
	}
	
	protected abstract long getLastModified() throws Throwable;
	
	long getPrevLastModified() {
		return m_PrevLastModified;
	}
	
	void setPrevLastModified(long i_PrevLastModified) {
		m_PrevLastModified = i_PrevLastModified;
	}
	
	protected abstract long calculateSize() throws Throwable;
	
	public long getSize() throws Throwable {
		long lastModified = getLastModified();
		
		if (lastModified != m_PrevLastModified) {
			m_PrevLastModified = lastModified;
			m_Size = calculateSize();
		}
		
		return m_Size;
	}
		
	public String getName() {
		return m_Name;
	}
	
	public ADirectory<?> getParent() {
		return m_Parent;
	}
	
	public abstract String getId();
}