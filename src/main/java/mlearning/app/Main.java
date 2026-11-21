package mlearning.app;

import mlearning.adapter.LearningController;
import mlearning.adapter.LearningViewModel;
import mlearning.desktop.DatasetPanel;
import mlearning.entity.LearningProject;
import mlearning.infrastructure.IrisDatasetGateway;
import mlearning.usecase.prepare.PrepareDatasetInteractor;
import javax.swing.*;
import java.awt.Dimension;

/** Dataset-only desktop prototype. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MLearning - Prepare a Dataset");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(createPanel());
            frame.setMinimumSize(new Dimension(780, 650));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static DatasetPanel createPanel() {
        LearningViewModel model = new LearningViewModel();
        LearningController controller = new LearningController(
                new PrepareDatasetInteractor(new LearningProject(), new IrisDatasetGateway()),
                model, SwingUtilities::invokeLater);
        return new DatasetPanel(model, controller);
    }
}
