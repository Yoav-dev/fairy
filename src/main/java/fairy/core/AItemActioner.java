package fairy.core;

import java.nio.file.Path;

//Action can be every method ran upon events on item
// Events are: created, modified, renamed, deleted
public abstract class AItemActioner {
	protected abstract void created(AItem i_Item, long i_CreationTime);
	protected abstract void deleted(AItem i_Item, long i_DeletionTime);
	protected abstract void renamed(AItem i_Item, Path i_OldPath, Path i_NewPath, long i_RenameTime);
	protected abstract void modified(AItem i_Item, long i_NewSize, long i_ModificationTime);
}