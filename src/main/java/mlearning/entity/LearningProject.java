package mlearning.entity;

/** One in-memory workspace. Use cases synchronize on this object for atomic changes. */
public final class LearningProject {
    private Dataset dataset;
    private ModelDefinition definition;
    private TrainedModel trainedModel;
    private boolean training;

    public void requireIdle() {
        if (training) {
            throw new IllegalStateException("Training is in progress. Wait before changing the project.");
        }
    }

    public Dataset dataset() {
        if (dataset == null) {
            throw new IllegalStateException("Prepare a dataset first.");
        }
        return dataset;
    }

    public ModelDefinition definition() {
        if (definition == null) {
            throw new IllegalStateException("Apply a valid network first.");
        }
        return definition;
    }

    public TrainedModel trainedModel() {
        requireIdle();
        if (trainedModel == null) {
            throw new IllegalStateException("Train the current network before making predictions.");
        }
        return trainedModel;
    }

    public void selectDataset(Dataset selected) {
        requireIdle();
        dataset = selected;
        definition = null;
        trainedModel = null;
    }

    public void clearDefinition() {
        requireIdle();
        definition = null;
        trainedModel = null;
    }

    public void define(ModelDefinition selected) {
        requireIdle();
        definition = selected;
        trainedModel = null;
    }

    public void beginTraining() {
        requireIdle();
        dataset();
        definition();
        trainedModel = null;
        training = true;
    }

    public void finishTraining(TrainedModel model) {
        trainedModel = model;
        training = false;
    }
}
