package com.mimozalab.innbalance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mimozalab.innbalance.model.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    
    // Найти все сессии пользователя
    List<Session> findByUserId(Long userId);
    
    // Найти сессии по типу упражнения
    List<Session> findByUserIdAndExerciseType(Long userId, String exerciseType);
    
    // Найти сессии за период
    List<Session> findByUserIdAndCreatedAtBetween(
        Long userId, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
    
    // Получить последние N сессий
    List<Session> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Подсчет сессий пользователя
    Long countByUserId(Long userId);
    
    // Сумма времени всех сессий
    @Query("SELECT SUM(s.duration) FROM Session s WHERE s.user.id = :userId")
    Long getTotalDurationByUserId(@Param("userId") Long userId);
    
    // Средний рейтинг сессий
    @Query("SELECT AVG(s.feelingAfter) FROM Session s WHERE s.user.id = :userId")
    Double getAverageFeelingByUserId(@Param("userId") Long userId);
    
    // Удалить все сессии пользователя
    void deleteByUserId(Long userId);
}