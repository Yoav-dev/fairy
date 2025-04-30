package fairy.locals;

import java.io.IOException;
import java.nio.file.Path;

import org.testng.annotations.Test;

import fairy.core.AItem;
import fairy.core.AItemActioner;
import fairy.core.Directory;

public class BasicTests {
  @Test
  void test() throws Throwable {
		try {
			LocalFileSystem localFileSystem = LocalFileSystem.getLocalFileSystem();
			
			Directory root = localFileSystem.getDirectory("/");
			Directory y = root
					.getDirectory("run")
					.getDirectory("media")
					.getDirectory("yoav")
					.getDirectory("3C9550D109262BB2")
					.getDirectory("y");
			
			y.getPath().resolve("sandbox").toFile().mkdir();
			Directory sandbox = y.getDirectory("sandbox");
			
			sandbox.addDirectory("NF1").addDirectory("NF1.1");
			sandbox.addDirectory("NF2").addDirectory("NF2.1");
			Thread.sleep(Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
