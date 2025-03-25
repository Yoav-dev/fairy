package fairy.core.three;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ADirectoryMonitor {
//	private class Renames {
//		Set<String> filesIdsCreated = new HashSet<String>();
//		Set<String> filesIdsDeleted = new HashSet<String>();
//		Map<String, String> filesNames2IDs = new HashMap<String, String>();
//	}
//	
//	private ADirectory<I> m_MonitoredDirectory;
//	
//	protected ADirectoryMonitor(ADirectory<I> i_MonitoredDirectory) {
//		m_MonitoredDirectory = i_MonitoredDirectory;
//	}
//	
//	protected abstract WatchService register(ADirectory<I> i_MonitoredDirectory);
//	protected abstract String extractName(Object i_Context);
//	
//	public void start() throws Throwable {
//		WatchService watchService = register(m_MonitoredDirectory);
//		WatchKey watchKey;
//		Set<String> filesIdsCreated = new HashSet<String>();
//		Set<String> filesIdsDeleted = new HashSet<String>();
//		Map<String, String> filesNames2IDs = new HashMap<String, String>();
//		Set<String> directoriesIdsCreated = new HashSet<String>();
//		Set<String> directoriesIdsDeleted = new HashSet<String>();
//		Map<String, String> directoriesNames2IDs = new HashMap<String, String>();
//		long eventTime;
//		
//		while ((watchKey = watchService.take()) != null) {
//			filesIdsCreated.clear();
//			filesIdsDeleted.clear();
//			filesNames2IDs.clear();
//			directoriesIdsCreated.clear();
//			directoriesIdsDeleted.clear();
//			directoriesNames2IDs.clear();
//			
//            for (WatchEvent<?> event : watchKey.pollEvents()) {
//            	eventTime = System.currentTimeMillis();
//            	String name = extractName(event.context());
//            	
//            	if (m_MonitoredDirectory.fileExists(name)) {
//            		AFile<I> file = m_MonitoredDirectory.getFile(name);
//            		String fileId = file.getItem().getId();
//            		
//            		if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
//            			filesIdsCreated.add(fileId);
//            			filesNames2IDs.put(name, fileId);
//            		}
//            	} else if (m_MonitoredDirectory.directoryExists(name)) {
//            		ADirectory<I> directory = m_MonitoredDirectory.getDirectory(name);
//            		String directoryId = directory.getItem().getId();
//            		
//            		if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
//            			directoriesIdsCreated.add(directoryId);
//            			directoriesNames2IDs.put(name, directoryId);
//            		}
//            	} else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
//            		if (filesNames2IDs.containsKey(name)) {
//            			filesIdsDeleted.add(name);
//                    } else if (directoriesNames2IDs.containsKey(name)) {
//            			directoriesIdsDeleted.add(name);
//            		}
//            	}
//            }
//            
//            if ()
//            watchKey.reset();
//        }
//	}
}