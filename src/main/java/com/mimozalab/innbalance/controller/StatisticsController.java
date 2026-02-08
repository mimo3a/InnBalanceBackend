package com.mimozalab.innbalance.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mimozalab.innbalance.dto.SessionDTO;
import com.mimozalab.innbalance.dto.StatisticsSummary;
import com.mimozalab.innbalance.exception.ResourceNotFoundException;
import com.mimozalab.innbalance.model.User;
import com.mimozalab.innbalance.repository.UserRepository;
import com.mimozalab.innbalance.service.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final UserRepository userRepository;

    public StatisticsController(
        StatisticsService statisticsService,
        UserRepository userRepository
    ) {
        this.statisticsService = statisticsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/sessions")
    public List<SessionDTO> getSessions(HttpServletRequest request) {
        User user = getUser(request);
        return statisticsService.getUserSessions(user);
    }

    @PostMapping("/sessions")
    public SessionDTO addSession(
        @RequestBody SessionDTO dto,
        HttpServletRequest request
    ) {
        User user = getUser(request);
        return statisticsService.createSession(dto, user);
    }

    @GetMapping("/summary")
    public StatisticsSummary getSummary(HttpServletRequest request) {
        User user = getUser(request);
        return statisticsService.getSummary(user);
    }

    private User getUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
