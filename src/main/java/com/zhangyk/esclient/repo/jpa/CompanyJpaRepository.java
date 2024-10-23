package com.zhangyk.esclient.repo.jpa;

import com.zhangyk.esclient.bean.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyJpaRepository extends CrudRepository<Company,Long> {

}
