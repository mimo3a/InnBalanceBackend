package com.mimozalab.innbalance.dto;

import java.time.LocalDateTime;

public class SessionDTO {

    private Long id;
    private Long userId;
    private String exerciseType;
    private Long duration; // duration in seconds (or chosen unit)
    private Integer feelingAfter;
    private LocalDateTime createdAt;

    public SessionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getFeelingAfter() {
        return feelingAfter;
    }

    public void setFeelingAfter(Integer feelingAfter) {
        this.feelingAfter = feelingAfter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
