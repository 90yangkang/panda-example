package com.example.shardingjdbc;


import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class PandaShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    private static Map<String, String> dataSourceMap = new HashMap<String, String>();

    static {

        dataSourceMap.put("1", "ds01");

        dataSourceMap.put("2", "ds02");

        dataSourceMap.put("3", "ds03");

        log.info("hhhhgggg:"+dataSourceMap.size());
    }


    /**
     *
     * @param collection 当前维护的数据库源列表
     * @param preciseShardingValue 当前维护的数据库源列表
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {

        log.info("availableTargetNames: {}", collection.toArray().toString());

        log.info("shardingValue: {}", preciseShardingValue.toString());

        String tid = TradeUtils.getLoginUser();

        log.info("tid: {}", tid);
        return "ds" + tid;

    }

}
