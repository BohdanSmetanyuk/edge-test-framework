package org.thingsboard.edgetest.solution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thingsboard.edgetest.util.EntitySolver;
import org.thingsboard.rest.client.RestClient;

abstract public class BaseSolution {

    protected static final Logger logger = LogManager.getLogger(BaseSolution.class);

    protected EntitySolver entitySolver;

    public void initSolution(RestClient restClient) {
        entitySolver = new EntitySolver(restClient, getSolutionDir());
    }

    abstract String getSolutionDir();
}
