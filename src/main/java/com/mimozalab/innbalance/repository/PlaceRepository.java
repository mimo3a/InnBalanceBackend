package com.mimozalab.innbalance.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mimozalab.innbalance.model.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    
    // Найти все места пользователя
    List<Place> findByUserId(Long userId);
    
    // Найти места по умолчанию
    List<Place> findByIsDefaultTrue();
    
    
    // Удалить все места пользователя (кроме default)
    void deleteByUserIdAndIsDefaultFalse(Long userId);
}