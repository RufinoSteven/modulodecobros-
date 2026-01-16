/**
 * This class implements a Cucumber plugin to integrate with ExtentReports.
 *
 * <p>Main responsibilities:</p>
 * <ul>
 *   <li>Listen to Cucumber step events (start/finish)</li>
 *   <li>Create ExtentReports step nodes dynamically</li>
 *   <li>Mark steps as finished with proper status in ExtentReports</li>
 *   <li>Clear step context after each step to avoid conflicts in parallel execution</li>
 * </ul>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */

package utils.reporting;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import utils.ExtentReportManager;

public class ExtentCucumberPlugin implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, this::onStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::onStepFinished);
    }

    private void onStepStarted(TestStepStarted event) {
        if (!(event.getTestStep() instanceof PickleStepTestStep ps)) {
            ExtentReportManager.clearCurrentStep();
            return;
        }

        String keyword = ps.getStep().getKeyword();
        String kw = (keyword == null) ? "And" : keyword.trim();
        ExtentReportManager.createStep(kw.isEmpty() ? "And" : kw, ps.getStep().getText());
    }

    private void onStepFinished(TestStepFinished event) {
        ExtentReportManager.finishStep(event.getResult().getStatus().name(), event.getResult().getError());
        ExtentReportManager.clearCurrentStep();
    }
}