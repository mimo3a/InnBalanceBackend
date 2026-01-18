package com.mimozalab.innbalance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mimozalab.innbalance.dto.SessionDTO;
import com.mimozalab.innbalance.dto.StatisticsSummary;
import com.mimozalab.innbalance.security.JwtTokenProvider;
import com.mimozalab.innbalance.service.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    // GET /api/statistics - Получить все сессии пользователя
    @GetMapping
    public ResponseEntity<List<SessionDTO>> getUserSessions(
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        List<SessionDTO> sessions = statisticsService.getUserSessions(username);
        return ResponseEntity.ok(sessions);
    }
    
    // POST /api/statistics - Создать новую сессию
    @PostMapping
    public ResponseEntity<SessionDTO> createSession(
            @RequestBody SessionDTO sessionDTO,
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        SessionDTO created = statisticsService.createSession(sessionDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // PUT /api/statistics/{id} - Обновить сессию (например, добавить feeling after)
    @PutMapping("/{id}")
    public ResponseEntity<SessionDTO> updateSession(
            @PathVariable Long id,
            @RequestBody SessionDTO sessionDTO) {
        SessionDTO updated = statisticsService.updateSession(id, sessionDTO);
        return ResponseEntity.ok(updated);
    }
    
    // GET /api/statistics/summary - Получить сводную статистику
    @GetMapping("/summary")
    public ResponseEntity<StatisticsSummary> getStatisticsSummary(
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        StatisticsSummary summary = statisticsService.getStatisticsSummary(username);
        return ResponseEntity.ok(summary);
    }
    
    // GET /api/statistics/period - Статистика за период
    @GetMapping("/period")
    public ResponseEntity<List<SessionDTO>> getSessionsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        List<SessionDTO> sessions = statisticsService.getSessionsByPeriod(
            username, startDate.atStartOfDay(), endDate.atTime(23, 59, 59)
        );
        return ResponseEntity.ok(sessions);
    }
    
    // DELETE /api/statistics - Удалить все сессии пользователя
    @DeleteMapping
    public ResponseEntity<Void> clearAllSessions(
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        statisticsService.clearAllSessions(username);
        return ResponseEntity.noContent().build();
    }
    
    // DELETE /api/statistics/{id} - Удалить конкретную сессию
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        statisticsService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}