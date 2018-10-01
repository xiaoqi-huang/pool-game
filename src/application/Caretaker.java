package application;

import java.util.Stack;

/**
 * Caretaker which is resposible for a Stack of Mementos
 */
public class Caretaker {

    private Stack<Memento> mementos = new Stack<>();

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
