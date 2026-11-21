package mlearning.adapter;

import mlearning.usecase.prepare.PrepareDataset;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** Dataset presentation state. */
public final class LearningViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private PrepareDataset.Output dataset;
    private boolean busy;
    private String status = "Choose a source and features, then prepare the dataset.";
    private String error = "";

    public void addListener(PropertyChangeListener listener) { changes.addPropertyChangeListener(listener); }
    private void changed() { changes.firePropertyChange("state", null, null); }
    public PrepareDataset.Output dataset() { return dataset; }
    public boolean busy() { return busy; }
    public String status() { return status; }
    public String error() { return error; }

    public void beginTask(String message) {
        busy = true;
        error = "";
        status = message;
        changed();
    }

    public void endTask() { busy = false; changed(); }

    public void fail(String message) {
        error = message;
        status = "Action did not complete. " + message;
        changed();
    }

    public void datasetPrepared(PrepareDataset.Output result) {
        dataset = result;
        error = "";
        status = result.count() + " " + result.name() + " examples ready from " + result.source() + ".";
        changed();
    }

    public void datasetSelectionChanged() {
        status = "Selection changed. Prepare the dataset to update the displayed results.";
        changed();
    }
}
