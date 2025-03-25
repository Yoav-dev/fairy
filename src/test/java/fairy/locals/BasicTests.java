package fairy.locals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import fairy.core.AItemActioner;
import fairy.core.Directory;

class BasicTests {
	@Test
	void test() throws Throwable {
		try {
			LocalFileSystem localFileSystem = LocalFileSystem.getLocalFileSystem();
			localFileSystem.getItemsMonitor().addItemActioners(new AItemActioner[] {new AItemActioner() {
				
				@Override
				protected void renamed(Path i_OldPath, Path i_NewPath, long i_RenameTime) {
					System.out.println(i_OldPath.toString() + " renamed to " + i_NewPath);
				}
				
				@Override
				protected void modified(Path i_Path, long i_NewSize, long i_ModificationTime) {
					System.out.println(i_Path.toString() + " modified");
				}
				
				@Override
				protected void deleted(Path i_Path, long i_DeletionTime) {
					System.out.println(i_Path.toString() + " deleted");
					
				}
				
				@Override
				protected void created(Path i_Path, long i_CreationTime) {
					System.out.println(i_Path.toString() + " created");
					
				}
			}});
			Directory root = localFileSystem.getDirectory("/");
			localFileSystem.getItemsMonitor().markItemForAction(root
					.getDirectory("run")
					.getDirectory("media")
					.getDirectory("yoav")
					.getDirectory("3C9550D109262BB2")
					.getDirectory("y"));
			localFileSystem.getItemsMonitor().startMonitor();
			Thread.sleep(Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}