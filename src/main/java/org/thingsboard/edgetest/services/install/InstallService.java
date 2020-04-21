package org.thingsboard.edgetest.services.install;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.RunInstallApplication;
import org.thingsboard.edgetest.services.Service;
import org.thingsboard.edgetest.solutions.Solution;

@Component
public class InstallService extends Service {

    public void install() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RunInstallApplication.class);
        Solution solution = context.getBean(solutionName, Solution.class);
        solution.install(restClient);
    }
}
