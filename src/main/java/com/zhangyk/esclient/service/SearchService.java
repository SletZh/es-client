package com.zhangyk.esclient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangyk.esclient.bean.Company;
import com.zhangyk.esclient.bean.User;
import com.zhangyk.esclient.repo.jpa.CompanyJpaRepository;
import com.zhangyk.esclient.repo.jpa.UserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private UserJpaRepository userRepo;
    @Autowired
    private CompanyJpaRepository companyRepo;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 数据初始化
     * @param indexName
     */
    public void busDataInit(String indexName){
        IndexCoordinates curIndex = IndexCoordinates.of(indexName);
        IndexOperations indexOperations = elasticsearchOperations.indexOps(curIndex);
        boolean delResult = indexOperations.delete();
        log.info("索引：{}，删除结果：{}",indexName,delResult);
        boolean creResult = indexOperations.create();
        log.info("索引：{}，创建结果：{}",indexName,creResult);
        if("user".equals(indexName)){
            Iterable<User> users = userRepo.findAll();
            elasticsearchOperations.save(users,curIndex);
        }else if("company".equals(indexName)){
            Iterable<Company> companies = companyRepo.findAll();
            elasticsearchOperations.save(companies,curIndex);
        }
        indexOperations.refresh();
        long count = elasticsearchOperations.count(Query.findAll(),curIndex);
        log.info("索引：{}, 查询总量：{}",indexName,count);
    }

    /**
     * 增量更新数据，最近一小时更新
     * 1.定时自动刷新
     * 2.手动刷新
     * @param indexName
     */
    public void busDataUpdate(String indexName)  {
        IndexCoordinates curIndex = IndexCoordinates.of(indexName);
        IndexOperations indexOperations = elasticsearchOperations.indexOps(curIndex);
        List<UpdateQuery> updateQueryList = new ArrayList<>();
        if("user".equals(indexName)){
            Iterable<User> users = userRepo.findAll();
            users.forEach(e->{
                try {
                    String updateJson = objectMapper.writeValueAsString(e);
                    UpdateQuery updateQuery = UpdateQuery.builder(e.getId().toString()).withDocument(Document.parse(updateJson)).build();
                    updateQueryList.add(updateQuery);
                } catch (JsonProcessingException exp) {
                    log.info("json序列化失败：{}",exp.getMessage());
                }
            });
        }else if("company".equals(indexName)){
            Iterable<Company> companies = companyRepo.findAll();
            companies.forEach(e->{
                try {
                    String updateJson = objectMapper.writeValueAsString(e);
                    UpdateQuery updateQuery = UpdateQuery.builder(e.getId().toString()).withDocument(Document.parse(updateJson)).build();
                    updateQueryList.add(updateQuery);
                } catch (JsonProcessingException exp) {
                    log.info("json序列化失败：{}",exp.getMessage());
                }
            });
        }
        elasticsearchOperations.bulkUpdate(updateQueryList,curIndex);
        indexOperations.refresh();
        log.info("索引：{}, 更新总量：{}",indexName,updateQueryList.size());
    }

    /**
     * 查询
     * @param indexName
     * @return
     */
    public List<Object> queryData(String indexName,String jsonParam) {
        StringQuery sq = new StringQuery(jsonParam);
        //默认查询10条，调整为20条
        sq.setPageable(PageRequest.ofSize(20));
        List<Object> result = new ArrayList<>();
        if("user".equals(indexName)){
            SearchHits<User> searchHits =  elasticsearchOperations.search(sq,User.class);
            searchHits.iterator().forEachRemaining(e->{
                result.add(e.getContent());
            });
            log.info("查询总量：{}",searchHits.getTotalHits());
        }else if("company".equals(indexName)){
            //默认返回10条，如何需要调整，需要修改from、size参数
            SearchHits<Company> companySearchHits =  elasticsearchOperations.search(sq,Company.class);
            companySearchHits.iterator().forEachRemaining(e->{
                result.add(e.getContent());
            });
            log.info("查询总量：{}",companySearchHits.getTotalHits());
        }
        return result;
    }
}
