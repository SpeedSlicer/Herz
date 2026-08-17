package net.ada.util;

public class Sordidabilis<T> {
    T currentValue;
    T newValue;
    public Sordidabilis(T value) {
        currentValue = value;
    }
    public void push() {
        currentValue = newValue;
    }
    public Sordidabilis<T> cancel() {
        set(null);
        return this;
    }
    public Sordidabilis<T> set(T value) {
        newValue = value;
        return this;
    }
    public T get() {
        return currentValue;
    }
    public T getQueued() {
        return newValue;
    }
}
