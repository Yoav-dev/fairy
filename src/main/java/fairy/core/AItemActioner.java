package fairy.core;

import java.nio.file.Path;

//Action can be every method ran upon events on item
// Events are: created, modified, renamed, deleted
public abstract class AItemActioner {
	protected abstract void created(Path i_Path, long i_CreationTime);
	protected abstract void deleted(Path i_Path, long i_DeletionTime);
	protected abstract void renamed(Path i_OldPath, Path i_NewPath, long i_RenameTime);
	protected abstract void modified(Path i_Path, long i_NewSize, long i_ModificationTime);
}