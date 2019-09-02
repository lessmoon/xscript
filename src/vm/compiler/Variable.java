package vm.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Variable<T> {
    T value = null;
    List<VariableListener<T>> listeners = new ArrayList<>();
    boolean isFinished = false;
    
    public void addAction(Consumer<T> action) {
        this.addListener(new VariableListener(action));
    }

    public void addListener(VariableListener<T> listener) {
        if (isFinished) {
            listener.onChanged(value);
            listener.onFinished(value);
            return;
        }
        if (!listeners.contains(listener)) {
            listener.add(listener);
        }
    }

    public Variable<T> setValue(T value) {
        if (isFinished) {
            return this;
        }
        this.value = value;
        for (VariableListener<T> listener : listeners) {
            listener.onChanged(this.value);
        }
        return this;
    }

    public T getValue() {
        return value;
    }

    public void finished() {
        isFinished = true;
        for (VariableListener<T> listener : listeners) {
            listeners.onFinished(this.value);
        }
        listeners.clear();
    }
}