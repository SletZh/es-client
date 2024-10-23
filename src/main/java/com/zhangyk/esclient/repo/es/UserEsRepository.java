package com.zhangyk.esclient.repo.es;

import com.zhangyk.esclient.bean.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserEsRepository extends ElasticsearchRepository<User,Long> {

}
