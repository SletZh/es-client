package com.zhangyk.esclient.repo.es;

import com.zhangyk.esclient.bean.Company;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CompanyEsRepository extends ElasticsearchRepository<Company,Long> {
    
}
