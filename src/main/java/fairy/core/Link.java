package fairy.core;

import java.nio.file.Path;

public class Link extends AItem {
	private Path m_Target;
	
	protected Link(String i_Name, Directory i_Parent, AFileSystem i_FileSystem, Path i_Target) throws Throwable {
		super(i_Name, i_Parent, i_FileSystem, false, false);
		m_Target = i_Target;
	}
	
	public Path getTarget() {
		return m_Target;
	}
}