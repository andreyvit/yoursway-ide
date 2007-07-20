package com.yoursway.common;

import java.util.Iterator;

public class StringTupleIterable implements Iterable<String[]> {
    
    private final static class TupleIterator implements Iterator<String[]> {
        
        private final Iterable<String>[] elements;
        private final Iterator<String>[] iterators;
        private final String[] values;
        private boolean hasNext = true;
        
        @SuppressWarnings("unchecked")
        public TupleIterator(Iterable<String>[] elements) {
            this.elements = elements;
            iterators = (Iterator<String>[]) new Iterator<?>[elements.length];
            values = new String[elements.length];
            resetIteratorsStartingWith(0);
        }
        
        public boolean hasNext() {
            return hasNext;
        }
        
        public String[] next() {
            String[] result = new String[values.length];
            System.arraycopy(values, 0, result, 0, values.length);
            advance();
            return result;
        }
        
        private void advance() {
            int incr = findRightmostIteratorThatHasNextValue();
            if (incr < 0)
                hasNext = false;
            else {
                values[incr] = iterators[incr].next();
                resetIteratorsStartingWith(incr + 1);
            }
        }
        
        private int findRightmostIteratorThatHasNextValue() {
            int nextIterator = -1;
            for (int i = iterators.length - 1; i >= 0; i--)
                if (iterators[i].hasNext()) {
                    nextIterator = i;
                    break;
                }
            return nextIterator;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private void resetIteratorsStartingWith(int index) {
            for (int i = index; i < iterators.length; i++) {
                iterators[i] = elements[i].iterator();
                if (!iterators[i].hasNext())
                    hasNext = false;
                else
                    values[i] = iterators[i].next();
            }
        }
        
    }
    
    private final Iterable<String>[] elements;
    
    public StringTupleIterable(Iterable<String>[] elements) {
        this.elements = elements;
    }
    
    public Iterator<String[]> iterator() {
        return new TupleIterator(elements);
    }
    
}
