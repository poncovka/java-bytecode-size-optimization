package jbyco.io.files;

import java.util.Iterator;
import java.util.NoSuchElementException;

abstract public class SearchIterator<T> implements Iterator<T>, Iterable<T>{

	protected T nextItem;
	
	abstract public T search();
	
	public void updateNext() {
		// if item is not null
		if (nextItem == null) {
			// search for next one
			nextItem = search();
		}
	}
	
	@Override
	public boolean hasNext() {
		
		// update
		updateNext();
		
		// is item null?
		return (nextItem != null);
	}

	@Override
	public T next() {
		
		// no element
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		// set null
		T item = nextItem;
		nextItem = null;

		// return item
		return item;
	}
	
	@Override
	public Iterator<T> iterator() {
		return this;
	}
}
