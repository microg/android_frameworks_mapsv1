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
