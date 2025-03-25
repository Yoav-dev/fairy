package fairy.core;


class ItemUniqueProperties implements Comparable<ItemUniqueProperties> {
	private long creationTime;
	private String fileSystemId;

	/**
	 * @param i_creationTime
	 * @param i_fileSystemId
	 */
	ItemUniqueProperties(long i_creationTime, String i_fileSystemId) {
		this.creationTime = i_creationTime;
		this.fileSystemId = i_fileSystemId;
	}

	long getCreationTime() {
		return creationTime;
	}

	String getFileSystemId() {
		return fileSystemId;
	}

	@Override
	public int compareTo(ItemUniqueProperties i_Object) {
		int comparison = 0;
		
		if (this instanceof FileUniqueProperties) {
			if (i_Object instanceof DirectoryUniqueProperties) {
				return -1;
			}
			
			comparison = ((FileUniqueProperties)this).compareTo((FileUniqueProperties)i_Object);
		}
		
		if (this instanceof DirectoryUniqueProperties) {
			if (i_Object instanceof FileUniqueProperties) {
				return 1;
			}
			
			comparison = ((DirectoryUniqueProperties)this).compareTo((DirectoryUniqueProperties)i_Object);
		}
		
		if (comparison != 0) {
			return comparison;
		}
		
		if (this.getCreationTime() < i_Object.getCreationTime()) {
			return -1;
		}
		
		if (this.getCreationTime() > i_Object.getCreationTime()) {
			return 1;
		}
		
		int idsComparison = this.getFileSystemId().compareTo(i_Object.fileSystemId);
		
		if (idsComparison != 0) {
			return idsComparison;
		}
		
		return 0;
	}
}
