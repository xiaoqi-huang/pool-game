package application;

import java.util.Stack;

public class Caretaker {

    private Stack<Memento> mementos = new Stack<Memento>();

    public boolean isEmpty() {
        return mementos.isEmpty();
    }

    public void addMemento(Memento m) {
        mementos.push(m);
    }

    public Memento getMemento() {
        return mementos.pop();
    }
}
