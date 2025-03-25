/**
 *
 */
package fairy.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 *
 */
class DirectoryUniqueProperties extends ItemUniqueProperties {
	private TreeSet<String> childrenIds = new TreeSet<>();

	/**
	 * @param i_creationTime
	 * @param i_fileSystemId
	 * @param i_childrenIds
	 */
	DirectoryUniqueProperties(long i_creationTime, String i_fileSystemId, Collection<String> i_childrenIds) {
		super(i_creationTime, i_fileSystemId);
		this.childrenIds = new TreeSet<String>(i_childrenIds);
	}

	void addChildId(String i_ChildId) {
		this.childrenIds.add(i_ChildId);
	}

	void removeChildId(String i_ChildId) {
		this.childrenIds.remove(i_ChildId);
	}

	public int compareTo(DirectoryUniqueProperties i_Object) {
		DirectoryUniqueProperties other = (DirectoryUniqueProperties) i_Object;

		if (this.childrenIds.size() < other.childrenIds.size()) {
			return -1;
		}

		if (this.childrenIds.size() > other.childrenIds.size()) {
			return 1;
		}
		
		Iterator<String> otherIt = other.childrenIds.iterator();

		for (String element : this.childrenIds) {
			int comparison = element.compareTo(otherIt.next());
			
			if (comparison != 0) {
				return comparison;
			}
		}

		return 0;
	}

}