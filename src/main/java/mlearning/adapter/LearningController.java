package mlearning.adapter;

import mlearning.usecase.prepare.PrepareDataset;
import mlearning.usecase.OutputBoundary;
import java.util.List;
import java.util.concurrent.Executor;

/** Converts dataset selections into use-case input */
public final class LearningController {
    private final PrepareDataset prepare;
    private final LearningViewModel model;
    private final Executor dispatcher;

    public LearningController(PrepareDataset prepare, LearningViewModel model, Executor dispatcher) {
        this.prepare = prepare;
        this.model = model;
        this.dispatcher = dispatcher;
    }

    public void prepare(String source, List<Integer> features) {
        prepare.execute(new PrepareDataset.Input(source, List.copyOf(features)),
                new OutputBoundary<PrepareDataset.Output>() {
                    @Override public void present(PrepareDataset.Output result) {
                        dispatcher.execute(() -> model.datasetPrepared(result));
                    }
                    @Override public void fail(String message) {
                        dispatcher.execute(() -> model.fail(message));
                    }
                });
    }
}
