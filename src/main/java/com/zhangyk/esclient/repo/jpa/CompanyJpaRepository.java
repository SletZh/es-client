package com.zhangyk.esclient.repo.jpa;

import com.zhangyk.esclient.bean.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<Company,Long> {

}
