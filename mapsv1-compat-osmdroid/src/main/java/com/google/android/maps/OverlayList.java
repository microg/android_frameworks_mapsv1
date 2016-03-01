/*
 * Copyright 2013-2016 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.maps;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class OverlayList implements List<Overlay> {
    private final List<org.osmdroid.views.overlay.Overlay> wrapped;
    private final NewOverlayAttachedListener attachedListener;

    public OverlayList(List<org.osmdroid.views.overlay.Overlay> wrapped, NewOverlayAttachedListener attachedListener) {
        this.wrapped = wrapped;
        this.attachedListener = attachedListener;
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
        attachedListener.onNewOverlayAttached(object);
        return Overlay.unwrap(wrapped.set(location, object));
    }

    @Override
    public void add(int location, Overlay object) {
        attachedListener.onNewOverlayAttached(object);
        wrapped.add(location, object);
    }

    @Override
    public boolean add(Overlay object) {
        attachedListener.onNewOverlayAttached(object);
        return wrapped.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends Overlay> collection) {
        for (Overlay overlay : collection) {
            attachedListener.onNewOverlayAttached(overlay);
        }
        return wrapped.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends Overlay> collection) {
        for (Overlay overlay : collection) {
            attachedListener.onNewOverlayAttached(overlay);
        }
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

    interface NewOverlayAttachedListener {
        void onNewOverlayAttached(Overlay overlay);
    }
}
