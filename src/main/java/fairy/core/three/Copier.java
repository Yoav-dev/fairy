package fairy.core.three;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Copier implements Serializable {
//	private static final long serialVersionUID = 5421078731142296849L;
//	
//	private static final File SESSIONS_DIR = Paths.get(Constants.MAIN_DIR.getAbsolutePath(), "fairy").toFile();		
//	private static final int CHUNK_SIZE = 1000;
//	private static final Comparator<ItemProperties<?>> ITEMS_COMPARATOR = new Comparator<ItemProperties<?>>() {
//		@Override
//		public int compare(ItemProperties<?> i_Item1, ItemProperties<?> i_Item2) {
//			Iterator<String> item1FullPathIterator = i_Item1.getFullPathIterator();
//			Iterator<String> item2FullPathIterator = i_Item2.getFullPathIterator();
//			int comparison;
//			
//			while (item1FullPathIterator.hasNext() && item2FullPathIterator.hasNext()) {
//				comparison = item1FullPathIterator.next().compareTo(item2FullPathIterator.next());
//				
//				if (comparison < 0) {
//					return -1;
//				}
//				
//				if (comparison > 0) {
//					return 1;
//				}
//			}
//			
//			if (item2FullPathIterator.hasNext()) {
//				return -1;
//			}
//			
//			if (item1FullPathIterator.hasNext()) {
//				return 1;
//			}
//			
//			return 0;
//		}
//	};
//		
//	private ItemProperties<?> m_Source;
//	private ADirectory<?, ?> m_Destination;
//	private TreeMap<ItemProperties<?>, Long> m_ModificationTimes = new TreeMap<ItemProperties<?>, Long>(ITEMS_COMPARATOR);
//	private String m_Name;
//	
//	public Copier(String i_Name, ItemProperties<?> i_Source, ADirectory<?, ?> i_Destination) {
//		m_Source = i_Source;
//		m_Destination = i_Destination;
//		m_Name = i_Name;
//	}
//	
//	public void copy() throws Throwable {
//		File seesionFile = Paths.get(SESSIONS_DIR.getAbsolutePath(), m_Name).toFile();
//		copy(m_Source, m_Destination);
//	}
//	
//	public void copy(ItemProperties<?> i_Source, ADirectory<?, ?> i_Destination) throws Throwable {
//		File seesionFile = Paths.get(SESSIONS_DIR.getAbsolutePath(), m_Name).toFile();
//		
//		if (i_Source instanceof AFile) {
//			AFile<?> destinationFile = i_Destination.addFile(i_Source.getName());
//			
//			try (InputStream sourceFileInputStream = ((AFile<?>)i_Source).getInputStream(); OutputStream destinationFileOutputStream = destinationFile.getOutputStream()) {
//				byte [] bytes = new byte[CHUNK_SIZE];
//				int bytesRead;
//				
//				while ((bytesRead = sourceFileInputStream.read(bytes)) != -1) {
//					destinationFileOutputStream.write(bytes, 0, bytesRead);
//					destinationFileOutputStream.flush();
//				}
//			}
//		} else {
//			ADirectory<?, ?> destinationDirectory = i_Destination.addDirectory(i_Source.getName());
//			ADirectory<?, ?> sourceDirectory = (ADirectory<?, ?>)i_Source;
//			
//			for (Entry<String, ?> itemEntry : sourceDirectory.getChildren().entrySet()) {
//				ItemProperties<?> item = (ItemProperties<?>) itemEntry.getValue();
//				
//				copy(item, destinationDirectory);
//			}
//		}
//	}
}