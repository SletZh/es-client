package com.zhangyk.esclient.repo.jpa;


import com.zhangyk.esclient.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User,Long> {

}
