package fairy.core;

class FileUniqueProperties extends ItemUniqueProperties {
	private byte [] checkSum;
	
	/**
	 * @param i_creationTime
	 * @param i_fileSystemId
	 * @param i_checkSum
	 */
	FileUniqueProperties(long i_creationTime, String i_fileSystemId, byte[] i_checkSum) {
		super(i_creationTime, i_fileSystemId);
		checkSum = i_checkSum;
	}

	public int compareTo(FileUniqueProperties i_Object) {
		FileUniqueProperties other = (FileUniqueProperties) i_Object;
		
		if (this.checkSum.length < other.checkSum.length) {
			return -1;
		}
		
		if (this.checkSum.length > other.checkSum.length) {
			return 1;
		}
		
		for (int i = 0; i < this.checkSum.length; i++) {
			if (this.checkSum[i] < other.checkSum[i]) {
				return -1;
			}
			
			if (this.checkSum[i] > other.checkSum[i]) {
				return 1;
			}
		}
		
		return 0;
	}
}