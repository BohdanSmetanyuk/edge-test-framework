package org.thingsboard.edgetest.services.uninstall;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.RunInstallApplication;
import org.thingsboard.edgetest.services.Service;
import org.thingsboard.edgetest.solutions.Solution;

@Component
public class UninstallService extends Service {

    public void uninstall() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RunInstallApplication.class);
        Solution solution = context.getBean(solutionName, Solution.class);
        solution.uninstall(restClient);
    }
}
