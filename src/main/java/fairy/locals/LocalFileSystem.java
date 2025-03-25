package fairy.locals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fairy.core.AFileSystem;

public class LocalFileSystem extends AFileSystem {
	private static final Pattern FILE_KEY_PATTERN = Pattern.compile("(\\(|.+,)ino=([0-9]+).*");

	private LocalFileSystem(LocalItemsMonitor i_LocalItemsMonitor) {
		super(i_LocalItemsMonitor);
	}

	public static LocalFileSystem getLocalFileSystem() throws IOException {
		LocalItemsMonitor localItemsMonitor = LocalItemsMonitor.getLocalItemsMonitor();
		
		return new LocalFileSystem(localItemsMonitor);
	}
	
	@Override
	protected Collection<String> getRootsNames() {
		Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
		Collection<String> names = new ArrayList<String>();
		
		for (Path rootDirectory : rootDirectories) {
			names.add(rootDirectory.toString());
		}
		
		return names;
	}

	@Override
	protected InputStream getFileInputStream(Path i_Path) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OutputStream getFileOutputStream(Path i_Path) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected long readFileSize(Path i_Path) throws Throwable {
		return getLocalFile(i_Path).length();
	}

	@Override
	protected Collection<String> getChildrenNames(Path i_Path) {
		File directory = getLocalFile(i_Path);
		File [] children = directory.listFiles();
		Collection<String> childrenNames = new HashSet<String>();
		
		if (children == null) {
			return childrenNames;
		}
		
		for (int i = 0; i < children.length; i++) {
			File child = children[i];
			childrenNames.add(child.getName());
		}
		
		return childrenNames;
	}
	
	private static File getLocalFile(Path i_Path) {
		return i_Path.toFile();
	}

	@Override
	protected String getPathId(Path i_Path) {
		BasicFileAttributes attr;
		
		try {
			attr = Files.readAttributes(i_Path, BasicFileAttributes.class);
		} catch (IOException e) {
			return null;
		}
		
		Matcher matcher = FILE_KEY_PATTERN.matcher(attr.fileKey().toString());
		
		if (!matcher.matches()) {
			return null;
		}
		
		return matcher.group(2);
	}

	@Override
	protected boolean isLink(Path i_Path) throws Throwable {
		return Files.isSymbolicLink(i_Path);
	}

	@Override
	protected boolean isDirectory(Path i_Path) throws Throwable {
		return i_Path.toFile().isDirectory();
	}

	@Override
	protected long getCreationTime(Path i_Path) {
		try {
			return Files.readAttributes(i_Path, BasicFileAttributes.class).creationTime().toMillis();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Collection<String> getChildrenIds(Path i_Path) {
		File directory = getLocalFile(i_Path);
		File [] children = directory.listFiles();
		Collection<String> childrenIds = new HashSet<String>();
		
		if (children == null) {
			return childrenIds;
		}
		
		for (int i = 0; i < children.length; i++) {
			File child = children[i];
			childrenIds.add(getPathId(child.toPath()));
		}
		
		return childrenIds;
	}
}