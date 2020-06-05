package org.thingsboard.edgetest.black.box;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.thingsboard.edgetest.black.box.cases.emulation.TestEmulation;
import org.thingsboard.edgetest.black.box.cases.entity.BuildRelations;
import org.thingsboard.edgetest.black.box.cases.entity.CreateEntities;
import org.thingsboard.edgetest.black.box.cases.entity.DeleteEntities;
import org.thingsboard.edgetest.black.box.cases.entity.DestroyRelations;


@Slf4j
@RunWith(Suite.class)
@Suite.SuiteClasses({CreateEntities.class, BuildRelations.class, TestEmulation.class,
                     DestroyRelations.class, DeleteEntities.class})
public class TestSuite {

}
