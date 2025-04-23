package leibooks.domain.facade;

import leibooks.domain.controllers.LibraryController;
import leibooks.domain.controllers.ShelvesController;
import leibooks.domain.core.ILibrary;
import leibooks.domain.core.Library;
import leibooks.domain.shelves.IShelves;
import leibooks.domain.shelves.Shelves;

public class LEIBooks {

	private final Library library;
	private final IShelves shelves;
	
	private final IShelvesController shelvesController;
	private final ILibraryController libraryController;
	
	public LEIBooks() {
		//lib and its controller
		library = new Library ();
		libraryController = new LibraryController(library);
		
		//shelves and its controller
		shelves = new Shelves (library);
		shelvesController = new ShelvesController(shelves);
	}

	public ILibrary getLibrary() {
		return library;
	}

	public IShelves getShelves() {
		return shelves;
	}

	public IShelvesController getShelvesController() {
		return shelvesController;
	}

	public ILibraryController getLibraryController() {
		return libraryController;
	}
	
	
}
