package fairy.core.three;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a file
 * Can be on either local or remote environment
 */
public abstract class AFile<I extends ItemProperties> {
	private I m_Item;
	
	protected AFile(I i_Item) {
		m_Item = i_Item;
	}
	
	public abstract InputStream getInputStream() throws Throwable;
	public abstract OutputStream getOutputStream() throws Throwable;
	
	public I getItem() {
		return m_Item;
	}
}