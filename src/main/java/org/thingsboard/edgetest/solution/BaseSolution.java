package org.thingsboard.edgetest.solution;

import org.thingsboard.edgetest.util.EntitySolver;
import org.thingsboard.rest.client.RestClient;

abstract public class BaseSolution {

    protected EntitySolver entitySolver;

    public void initSolution(RestClient restClient) {
        entitySolver = new EntitySolver(restClient, getSolutionDir());
    }

    abstract String getSolutionDir();
}
