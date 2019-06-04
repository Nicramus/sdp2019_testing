package com.sdp.testing.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "exchangerates", path = "exchangerates")
public interface ExchangeRepository extends PagingAndSortingRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findByKey(@Param("key") String key);
}
