package fairy.core;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

// Monitors items (Files or directories) for changes as modifications, adding/deleting sub-items to/from directory, etc
public abstract class AItemsMonitor {
	// Does the monitoring
	private WatchService m_WatchService;
	
	// Whatch service gives watch key for every event, each key for a path
	private Map<WatchKey, Directory> m_WatchKeys2Paths = new HashMap<WatchKey, Directory>();
	
	// Item actioners do some actions upon each action detected during monitoring:
	// create, delete, modify
	// Each class that extends abstract AItemActioner does another action, that can
	// be any action
	private TreeMap<ItemUniqueProperties, Collection<AItemActioner>> m_ItemsMarkedForAction = new TreeMap<ItemUniqueProperties, Collection<AItemActioner>>();
	
	private AFileSystem m_FileSystem;
	
	// Extract name of monitored item from WatchEvent context
	protected abstract String extractName(Object i_Context) throws Throwable;
		
	protected AItemsMonitor(WatchService i_WatchService) {
		m_WatchService = i_WatchService;
	}
	
	void setFileSystem(AFileSystem i_FileSystem) {
		m_FileSystem = i_FileSystem;
	}
	
	//*
	// Register item so it is watched
	protected abstract WatchKey registerDirectory(Path i_Path, WatchService i_WatchService) throws IOException;

	void registerDirectory(Directory i_Directory, Path i_Path) throws IOException {
		try {
				WatchKey watchKey = registerDirectory(i_Path, m_WatchService);
				m_WatchKeys2Paths.put(watchKey, i_Directory);
		} catch (AccessDeniedException e) {
		}
	}
	
	public void markItemForAction(AItem i_Item) throws IOException {
		m_ItemsMarkedForAction.putIfAbsent(i_Item.getUniqueProperties(), new ArrayList<AItemActioner>());
		
		if (i_Item.isDirectory) {
			Collection<AItem> children = ((Directory)i_Item).getChildren();
			
			if (children != null && !children.isEmpty()) {
				for (AItem child : children) {
					markItemForAction(child);
				}
			}
		}
	}
	//*
	
	public void addItemActioners(AItem i_Item, AItemActioner ... i_ItemActioners) {
		this.addItemActioners(i_Item, Arrays.asList(i_ItemActioners));
	}
	
	public void addItemActioners(AItem i_Item, Collection<AItemActioner> i_ItemActioners) {
		m_ItemsMarkedForAction.putIfAbsent(i_Item.getUniqueProperties(), new ArrayList<AItemActioner>());
		m_ItemsMarkedForAction.get(i_Item.getUniqueProperties()).addAll(i_ItemActioners);
		
		if (i_Item.isDirectory) {
			for (AItem item : ((Directory)i_Item).getChildren()) {
				this.addItemActioners(item, i_ItemActioners);
			}
		}
	}
	
	private Collection<AItemActioner> getItemActioners(AItem i_Item) {
		if (!m_ItemsMarkedForAction.containsKey(i_Item.getUniqueProperties())) {
			return Collections.emptyList();
		}
		
		return m_ItemsMarkedForAction.get(i_Item.getUniqueProperties());
	}
	
	private void traverseEvents(TreeMap<ItemUniqueProperties, ItemProperties> o_AffectedPoperties, TreeSet<ItemUniqueProperties> o_ItemsToDelete) throws Throwable {
		WatchKey watchKey = m_WatchService.take();
		Directory parent = m_WatchKeys2Paths.get(watchKey);
		List<WatchEvent<?>> events = watchKey.pollEvents();
		watchKey.reset();
		
		for (WatchEvent<?> event : events) {						
				// Get name of item under directory being watched
				String name = extractName(event.context());

				// Item created
				if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
					AItem item = parent.readItem(name);
					EPropertiesAffectType affectType = EPropertiesAffectType.eCreate;
					
					if (o_ItemsToDelete.contains(item.getUniqueProperties())) {										
						affectType = EPropertiesAffectType.eRename;
					}
					
					ItemProperties itemProperties = new ItemProperties(name, 
							parent,
							affectType,
							item.newlyCreated() ? item.getCreationTime() : System.currentTimeMillis());
					
					o_AffectedPoperties.put(item.getUniqueProperties(), itemProperties);
					
					continue;
				}

				if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
					AItem item = parent.getItem(name);
					o_ItemsToDelete.add(item.getUniqueProperties());

					continue;
				}

				if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
					File modifiedFile = (File)parent.getItem(name);
					ItemProperties itemProperties = new ItemProperties(modifiedFile.getName(), 
																														modifiedFile.getParent(),
																														EPropertiesAffectType.eModify,
																														System.currentTimeMillis());
					o_AffectedPoperties.put(modifiedFile.getUniqueProperties(), itemProperties);
					modifiedFile.updateContent();
					continue;
				}
			}
	}
	
	public void startMonitor() {
		AItemsMonitor self = this;
		
		Thread monitorThread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Start monitoring");
				TreeMap<ItemUniqueProperties, ItemProperties> affectedPoperties = new TreeMap<>();
				TreeSet<ItemUniqueProperties> itemsToDelete = new TreeSet<ItemUniqueProperties>();
				
				while (true) {
					try {
						traverseEvents(affectedPoperties, itemsToDelete);
						
						long timestamp = System.currentTimeMillis();
						
						for (ItemUniqueProperties itemUniqueProperties : affectedPoperties.keySet()) {
							ItemProperties itemsProperties = affectedPoperties.get(itemUniqueProperties);
							AItem item = m_FileSystem.getItem(itemUniqueProperties);
							item.addToHistory(itemsProperties);
							Directory parent = itemsProperties.getParent();
							Collection<AItemActioner> parentItemActioners = self.getItemActioners(parent);
							
							if (!parentItemActioners.isEmpty()) {
								if (itemsProperties.getAffectType() == EPropertiesAffectType.eCreate) {
										self.addItemActioners(item, parentItemActioners);
										
										for (AItemActioner itemActioner : parentItemActioners) {
											itemActioner.created(item, itemsProperties.getTimestamp());
										}
								} else if (itemsProperties.getAffectType() == EPropertiesAffectType.eModify) {
										for (AItemActioner itemActioner : parentItemActioners) {
											itemActioner.modified(item, itemsProperties.getSize(), timestamp);
										}
								} else if (itemsProperties.getAffectType() == EPropertiesAffectType.eRename) {
										for (AItemActioner itemActioner : parentItemActioners) {
											itemActioner.renamed(item, item.getPath(), itemsProperties.getPath(), timestamp);
										}
								}
							}
							
							itemsToDelete.remove(itemUniqueProperties);
						}
						
						affectedPoperties.clear();
						
						for (ItemUniqueProperties itemUniqueProperties : itemsToDelete) {
							AItem itemToDelete = m_FileSystem.getItem(itemUniqueProperties);
							Directory parent = itemToDelete.getParent();
							Collection<AItemActioner> parentItemActioners = self.getItemActioners(parent);
							
							if (m_ItemsMarkedForAction.containsKey(parent.getUniqueProperties())) {
								for (AItemActioner itemActioner : parentItemActioners) {
									itemActioner.deleted(itemToDelete, timestamp);
								}
							}
							
							itemToDelete.delete(timestamp);
						}
						
						itemsToDelete.clear();
					}
					catch (Throwable t) {
						continue;
					}
				}
			}
		});

		monitorThread.start();
	}
}