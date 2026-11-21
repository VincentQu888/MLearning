package mlearning.usecase.prepare;

import mlearning.entity.*;
import mlearning.usecase.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Retrieves and validates data before atomically replacing the current selection. */
public final class PrepareDatasetInteractor implements PrepareDataset {
    private final LearningProject project;
    private final DatasetGateway gateway;

    public PrepareDatasetInteractor(LearningProject project, DatasetGateway gateway) {
        this.project = project;
        this.gateway = gateway;
    }

    @Override public void execute(Input input, OutputBoundary<Output> presenter) {
        try {
            synchronized (project) { project.requireIdle(); }
            Dataset dataset = gateway.load(input.source()).selectFeatures(input.features());
            List<Row> sample = new ArrayList<>();
            // Show an example from each species, rather than only the first sorted rows.
            for (int label = 0; label < dataset.classes().size(); label++) {
                for (int row = 0; row < dataset.size(); row++) {
                    if (dataset.target(row) == label) {
                        sample.add(new Row(Arrays.stream(dataset.input(row)).boxed().toList(), dataset.classes().get(label)));
                        break;
                    }
                }
            }
            synchronized (project) { project.selectDataset(dataset); }
            presenter.present(new Output(dataset.name(), dataset.source(), dataset.features(),
                    dataset.classes(), dataset.size(), List.copyOf(sample)));
        } catch (IllegalArgumentException | IllegalStateException | DatasetGateway.AccessException exception) {
            presenter.fail(exception.getMessage());
        }
    }
}
