package fairy.locals;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import fairy.core.AItemsMonitor;

public class LocalItemsMonitor extends AItemsMonitor {	
	private LocalItemsMonitor(WatchService i_WatchService) {
		super(i_WatchService);
	}

	static LocalItemsMonitor getLocalItemsMonitor() throws IOException {
		WatchService watchService = FileSystems.getDefault().newWatchService();
		
		return new LocalItemsMonitor(watchService);
	}
	
	@Override
	protected String extractName(Object i_Context) throws Throwable {
		return ((Path)i_Context).toString();
	}
	
	@Override
	protected WatchKey registerDirectory(Path i_Path, WatchService i_WatchService) throws IOException {
		return i_Path.register(i_WatchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);	
	}
}