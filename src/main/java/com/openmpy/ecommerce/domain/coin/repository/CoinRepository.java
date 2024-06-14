package com.openmpy.ecommerce.domain.coin.repository;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinRepository extends JpaRepository<CoinEntity, Long> {

    @Query("select c from CoinEntity c where c.koreanName LIKE %:query% OR c.englishName LIKE %:query%")
    List<CoinEntity> findByQuery(@Param("query") String query);

    boolean existsByMarket(String market);
}
