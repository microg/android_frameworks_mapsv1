package com.google.android.maps;

import java.util.ListIterator;

public class OverlayListIterator implements ListIterator<Overlay> {

	private final ListIterator<org.osmdroid.views.overlay.Overlay> iterator;

	public OverlayListIterator(ListIterator<org.osmdroid.views.overlay.Overlay> iterator) {
		this.iterator = iterator;
	}

	@Override
	public void add(Overlay object) {
		iterator.add(object);
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public boolean hasPrevious() {
		return iterator.hasPrevious();
	}

	@Override
	public Overlay next() {
		return Overlay.unwrap(iterator.next());
	}

	@Override
	public int nextIndex() {
		return iterator.nextIndex();
	}

	@Override
	public Overlay previous() {
		return Overlay.unwrap(iterator.previous());
	}

	@Override
	public int previousIndex() {
		return iterator.previousIndex();
	}

	@Override
	public void remove() {
		iterator.remove();
	}

	@Override
	public void set(Overlay object) {
		iterator.set(object);
	}
}
