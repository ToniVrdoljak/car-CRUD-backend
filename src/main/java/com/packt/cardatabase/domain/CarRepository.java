package com.packt.cardatabase.domain;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CarRepository extends PagingAndSortingRepository <Car, Long> {
}