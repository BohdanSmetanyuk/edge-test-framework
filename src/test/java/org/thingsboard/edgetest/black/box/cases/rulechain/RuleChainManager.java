package org.thingsboard.edgetest.black.box.cases.rulechain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thingsboard.edgetest.black.box.util.DataSolver;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.rule.RuleChain;
import org.thingsboard.server.common.data.rule.RuleChainMetaData;

import javax.annotation.PostConstruct;

@Slf4j
@RunWith(SpringRunner.class)
public class RuleChainManager {

    private RestClient restClient;
    private DataSolver ds;
    private ObjectMapper mapper;

    @PostConstruct
    public void init() {
        restClient = new RestClient("http://localhost:8080");
        restClient.login("tenant@thingsboard.org", "tenant");
        ds = new DataSolver();
        mapper = new ObjectMapper();
    }

    @Test
    public void createAndSetRootRuleChain() throws Exception {
        JsonNode data = ds.getRuleChainAsJsonNode();
        RuleChain ruleChain = mapper.treeToValue(data.get("ruleChain"), RuleChain.class);
        RuleChainMetaData ruleChainMetaData = mapper.treeToValue(data.get("metadata"), RuleChainMetaData.class);
        RuleChain savedRuleChain = restClient.saveRuleChain(ruleChain);
        ruleChainMetaData.setRuleChainId(savedRuleChain.getId());
        restClient.saveRuleChainMetaData(ruleChainMetaData);
        restClient.setRootRuleChain(savedRuleChain.getId());
    }

}
