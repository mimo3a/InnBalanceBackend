package com.mimozalab.innbalance.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mimozalab.innbalance.dto.SessionDTO;
import com.mimozalab.innbalance.dto.StatisticsSummary;
import com.mimozalab.innbalance.model.Session;
import com.mimozalab.innbalance.model.User;
import com.mimozalab.innbalance.repository.SessionRepository;

@Service
public class StatisticsService {
    @Autowired
    private SessionRepository sessionRepository;
    
    public List <SessionDTO> getUserSessions(User user) {
    return sessionRepository.findByUserId(user.getId())
        .stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
}

@Transactional
public SessionDTO createSession(SessionDTO dto, User user) {
    Session s = new Session();
    s.setUser(user);
    s.setExerciseType(dto.getExerciseType());
    s.setDuration(dto.getDuration());
    s.setCreatedAt(LocalDateTime.now());
    return toDTO(sessionRepository.save(s));
}

public StatisticsSummary getSummary(User user) {
    Long userId = user.getId();

    StatisticsSummary summary = new StatisticsSummary();
    summary.setTotalSessions(sessionRepository.countByUserId(userId));
    summary.setTotalDuration(
        Optional.ofNullable(sessionRepository.getTotalDurationByUserId(userId)).orElse(0L)
    );

    return summary;
}

private SessionDTO toDTO(Session s) {
    if (s == null) return null;

    SessionDTO dto = new SessionDTO();
    dto.setId(s.getId());
    dto.setUserId(s.getUser() != null ? s.getUser().getId() : null);
    dto.setExerciseType(s.getExerciseType());
    dto.setDuration(s.getDuration());
    dto.setCreatedAt(s.getCreatedAt());

    return dto;
}

}