package fairy.core;

import java.nio.file.Path;
import java.util.TreeMap;

// File or Directory
public abstract class AItem {
	// Histories of all items properties that can change throughout its life cycle
	protected TreeMap<Long, ItemProperties> m_History = new TreeMap<Long, ItemProperties>();
	protected long m_DeletionTimeStamp = -1;
	protected ItemUniqueProperties uniqueProperties;

	// File sysytem where item resides
	protected AFileSystem m_FileSystem;

	public final boolean isFile;
	public final boolean isDirectory;

	protected AItem(String i_Name, Directory i_Parent, AFileSystem i_FileSystem, boolean i_IsFile, boolean i_IsDirectory) {
		m_FileSystem = i_FileSystem;
		Path path = Directory.getSubItemPath(i_Parent, i_Name);
		long creationTime = m_FileSystem.getCreationTime(path);
		m_History.put(creationTime,
				new ItemProperties(i_Name, i_Parent, path, EPropertiesAffectType.eCreate, creationTime));
		isFile = i_IsFile;
		isDirectory = i_IsDirectory;
	}

	boolean newlyCreated() {
		return m_History.isEmpty();
	}
	public String getName() {
		return m_History.get(m_History.lastKey()).getName();
	}

	public Directory getParent() {
		return m_History.get(m_History.lastKey()).getParent();
	}

	public Path getPath() {
		return m_History.get(m_History.lastKey()).getPath();
	}

	public String getId() {
		return this.uniqueProperties.getFileSystemId();
	}

	public long getCreationTime() {
		return m_History.firstKey();
	}

	public long getSize() {
		return m_History.get(m_History.lastKey()).getSize();
	}

	void addToHistory(ItemProperties i_ItemProperties) {
		m_History.put(i_ItemProperties.getTimestamp(), i_ItemProperties);
	}

	void delete(long i_DeletionTimeStamp) {
		m_DeletionTimeStamp = i_DeletionTimeStamp;
		getParent().unreadItem(getName());
	}

	/**
	 * @return the uniqueProperties
	 */
	ItemUniqueProperties getUniqueProperties() {
		return uniqueProperties;
	}
	
	void updateSize(long i_NewSize, long i_TimeStamp) {
		addToHistory(new ItemProperties(this.getName(), 
				this.getParent(),
				EPropertiesAffectType.eModify,
				i_TimeStamp));
	}
}