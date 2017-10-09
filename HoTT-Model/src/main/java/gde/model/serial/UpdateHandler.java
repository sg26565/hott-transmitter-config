package gde.model.serial;

public interface UpdateHandler {
    boolean isCancelled();

    void update(int step, int count);
}
