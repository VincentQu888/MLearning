package mlearning.usecase;

/** A per-operation output port. No Swing, HTTP, JSON, or view types cross this boundary. */
public interface OutputBoundary<T> {
    void present(T result);
    void fail(String message);
}
