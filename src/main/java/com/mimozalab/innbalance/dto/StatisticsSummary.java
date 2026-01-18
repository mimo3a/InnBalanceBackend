package com.mimozalab.innbalance.dto;

public class StatisticsSummary {
    private Long totalSessions;
    private Long totalDuration;  // в секундах
    private Double averageFeeling;
    private String mostUsedExercise;
    // getters/setters
	public Long getTotalSessions() {
		return totalSessions;
	}
	public void setTotalSessions(Long totalSessions) {
		this.totalSessions = totalSessions;
	}
	public Long getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(Long totalDuration) {
		this.totalDuration = totalDuration;
	}
	public Double getAverageFeeling() {
		return averageFeeling;
	}
	public void setAverageFeeling(Double averageFeeling) {
		this.averageFeeling = averageFeeling;
	}
	public String getMostUsedExercise() {
		return mostUsedExercise;
	}
	public void setMostUsedExercise(String mostUsedExercise) {
		this.mostUsedExercise = mostUsedExercise;
	}
    
}