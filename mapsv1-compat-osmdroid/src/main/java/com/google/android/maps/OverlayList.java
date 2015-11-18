package com.google.android.maps;

import java.util.*;

public class OverlayList implements List<Overlay> {

	private final List<org.osmdroid.views.overlay.Overlay> wrapped;

	public OverlayList(List<org.osmdroid.views.overlay.Overlay> wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public Overlay get(int location) {
		return Overlay.unwrap(wrapped.get(location));
	}

	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}

	@Override
	public int indexOf(Object object) {
		return wrapped.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return wrapped.isEmpty();
	}

	@Override
	public Iterator<Overlay> iterator() {
		return new OverlayListIterator(wrapped.listIterator());
	}

	@Override
	public int lastIndexOf(Object object) {
		return wrapped.lastIndexOf(object);
	}

	@Override
	public ListIterator<Overlay> listIterator() {
		return new OverlayListIterator(wrapped.listIterator());
	}

	@Override
	public ListIterator<Overlay> listIterator(int location) {
		return new OverlayListIterator(wrapped.listIterator(location));
	}

	@Override
	public Overlay remove(int location) {
		return Overlay.unwrap(wrapped.remove(location));
	}

	@Override
	public boolean remove(Object object) {
		return wrapped.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return wrapped.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return wrapped.retainAll(collection);
	}

	@Override
	public Overlay set(int location, Overlay object) {
		return Overlay.unwrap(wrapped.set(location, object));
	}

	@Override
	public void add(int location, Overlay object) {
		wrapped.add(location, object);
	}

	@Override
	public boolean add(Overlay object) {
		return wrapped.add(object);
	}

	@Override
	public boolean addAll(int location, Collection<? extends Overlay> collection) {
		return wrapped.addAll(location, collection);
	}

	@Override
	public boolean addAll(Collection<? extends Overlay> collection) {
		return wrapped.addAll(collection);
	}

	@Override
	public void clear() {
		wrapped.clear();
	}

	@Override
	public boolean contains(Object object) {
		return wrapped.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return wrapped.containsAll(collection);
	}

	@Override
	public boolean equals(Object object) {
		return wrapped.equals(object);
	}

	@Override
	public int size() {
		return wrapped.size();
	}

	@Override
	public List<Overlay> subList(int start, int end) {
		return Overlay.unwrap(wrapped.subList(start, end));
	}

	@Override
	public Object[] toArray() {
		return wrapped.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return wrapped.toArray(array);
	}

}
