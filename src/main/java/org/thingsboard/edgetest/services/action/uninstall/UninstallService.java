package org.thingsboard.edgetest.services.action.uninstall;

import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.services.action.ActionService;

@Service
public class UninstallService extends ActionService {

    public void start() {
        solution.uninstall();
    }
}
