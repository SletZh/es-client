package com.zhangyk.esclient.api;

import com.zhangyk.esclient.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "/search")
public class SearchApi {

    @Autowired
    private SearchService searchService;

    /**
     * 业务数据初始化
     * @param indexName 索引名称
     */
    @PostMapping(value = "/initData/{indexName}")
    public void busDataInit(@PathVariable String indexName){
        searchService.busDataInit(indexName);
    }

    /**
     * 业务数据刷新
     * @param indexName 索引名称
     */
    @PostMapping(value = "/updateData/{indexName}")
    public void busDataUpdate(@PathVariable String indexName){
        searchService.busDataUpdate(indexName);
    }

    /**
     * 业务数据查询
     * @param indexName 索引名称
     */
    @PostMapping(value = "/queryData/{indexName}")
    public List<Object> queryData(@PathVariable String indexName,@RequestBody String jsonParam){
        return searchService.queryData(indexName,jsonParam);
    }

}
