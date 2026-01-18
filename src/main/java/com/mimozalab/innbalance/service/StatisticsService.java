package com.mimozalab.innbalance.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mimozalab.innbalance.dto.SessionDTO;
import com.mimozalab.innbalance.dto.StatisticsSummary;
import com.mimozalab.innbalance.exception.ResourceNotFoundException;
import com.mimozalab.innbalance.model.Session;
import com.mimozalab.innbalance.model.User;
import com.mimozalab.innbalance.repository.SessionRepository;
import com.mimozalab.innbalance.repository.UserRepository;

@Service
public class StatisticsService {
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
    
    public List<SessionDTO> getUserSessions(String username) {
        User user = findUserByUsername(username);
        return sessionRepository.findByUserId(user.getId())
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public SessionDTO createSession(SessionDTO sessionDTO, String username) {
        User user = findUserByUsername(username);
        Session session = new Session();
        session.setUser(user);
        session.setExerciseType(sessionDTO.getExerciseType());
        session.setDuration(sessionDTO.getDuration());
        session.setFeelingAfter(sessionDTO.getFeelingAfter());
        session.setCreatedAt(sessionDTO.getCreatedAt() != null ? sessionDTO.getCreatedAt() : LocalDateTime.now());
        Session saved = sessionRepository.save(session);
        return toDTO(saved);
    }

    public SessionDTO updateSession(Long id, SessionDTO sessionDTO) {
        Session session = sessionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        if (sessionDTO.getExerciseType() != null) session.setExerciseType(sessionDTO.getExerciseType());
        if (sessionDTO.getDuration() != null) session.setDuration(sessionDTO.getDuration());
        if (sessionDTO.getFeelingAfter() != null) session.setFeelingAfter(sessionDTO.getFeelingAfter());
        if (sessionDTO.getCreatedAt() != null) session.setCreatedAt(sessionDTO.getCreatedAt());
        Session updated = sessionRepository.save(session);
        return toDTO(updated);
    }

    public StatisticsSummary getStatisticsSummary(String username) {
        User user = findUserByUsername(username);
        Long userId = user.getId();
        Long totalSessions = sessionRepository.countByUserId(userId);
        Long totalDuration = Optional.ofNullable(sessionRepository.getTotalDurationByUserId(userId)).orElse(0L);
        Double averageFeeling = sessionRepository.getAverageFeelingByUserId(userId);
        // compute most used exercise
        List<Session> sessions = sessionRepository.findByUserId(userId);
        String mostUsed = sessions.stream()
            .filter(s -> s.getExerciseType() != null)
            .collect(Collectors.groupingBy(Session::getExerciseType, Collectors.counting()))
            .entrySet().stream()
            .max(Comparator.comparingLong(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse(null);
        StatisticsSummary summary = new StatisticsSummary();
        summary.setTotalSessions(totalSessions);
        summary.setTotalDuration(totalDuration);
        summary.setAverageFeeling(averageFeeling);
        summary.setMostUsedExercise(mostUsed);
        return summary;
    }

    public List<SessionDTO> getSessionsByPeriod(String username, LocalDateTime start, LocalDateTime end) {
        User user = findUserByUsername(username);
        return sessionRepository.findByUserIdAndCreatedAtBetween(user.getId(), start, end)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public void clearAllSessions(String username) {
        User user = findUserByUsername(username);
        sessionRepository.deleteByUserId(user.getId());
    }

    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Session not found with id: " + id);
        }
        sessionRepository.deleteById(id);
    }

    private SessionDTO toDTO(Session s) {
        if (s == null) return null;
        SessionDTO dto = new SessionDTO();
        dto.setId(s.getId());
        dto.setUserId(s.getUser() != null ? s.getUser().getId() : null);
        dto.setExerciseType(s.getExerciseType());
        dto.setDuration(s.getDuration());
        dto.setFeelingAfter(s.getFeelingAfter());
        dto.setCreatedAt(s.getCreatedAt());
        return dto;
    }
}