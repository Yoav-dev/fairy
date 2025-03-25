package fairy.core;

import java.nio.file.Path;

class ItemProperties {
	private String m_Name;
	private Directory m_Parent;
	private Path m_Path;
	private long m_Size;
	private EPropertiesAffectType m_AffectType;
	private long m_Timestamp;
	
	ItemProperties(String i_Name, Directory i_Parent, Path i_Path, EPropertiesAffectType i_AffectType, long i_Timestamp) {
		m_Name = i_Name;
		m_Parent = i_Parent;
		m_Path = i_Path;
		m_AffectType = i_AffectType;
		m_Timestamp = i_Timestamp;
	}

	ItemProperties(String i_Name, Directory i_Parent, EPropertiesAffectType i_AffectType, long i_Timestamp) {
		this(i_Name, i_Parent, Directory.getSubItemPath(i_Parent, i_Name), i_AffectType, i_Timestamp);
	}
	
	String getName() {
		return m_Name;
	}

	Directory getParent() {
		return m_Parent;
	}

	Path getPath() {
		return m_Path;
	}

	long getSize() {
		return m_Size;
	}
	
	void setSize(long i_Size) {
		m_Size = i_Size;
	}
	
	EPropertiesAffectType getAffectType() {
		return m_AffectType;
	}
	
	long getTimestamp() {
		return m_Timestamp;
	}
}