package com.mimozalab.innbalance.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mimozalab.innbalance.exception.ResourceNotFoundException;
import com.mimozalab.innbalance.model.User;
import com.mimozalab.innbalance.repository.UserRepository;
import com.mimozalab.innbalance.dto.PlaceDTO;
import com.mimozalab.innbalance.service.PlaceService;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;
    private final UserRepository userRepository;

    public PlaceController(
        PlaceService placeService,
        UserRepository userRepository
    ) {
        this.placeService = placeService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my")
    public List<PlaceDTO> getMyPlaces(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return placeService.getUserPlaces(user);
    }

    @PostMapping
    public PlaceDTO addPlace(
            @RequestBody PlaceDTO placeDTO,
            HttpServletRequest request
    ) {
        User user = getUserFromRequest(request);
        return placeService.addPlace(placeDTO, user);
    }

    // ✏️ UPDATE place
    @PutMapping("/{id}")
    public PlaceDTO updatePlace(
            @PathVariable Long id,
            @RequestBody PlaceDTO placeDTO
    ) {
        return placeService.updatePlace(id, placeDTO);
    }

    // ❌ DELETE place
    @DeleteMapping("/{id}")
    public void deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
    }

    // 🔧 helper
    private User getUserFromRequest(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
