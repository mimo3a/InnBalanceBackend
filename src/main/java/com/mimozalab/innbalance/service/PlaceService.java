package com.mimozalab.innbalance.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mimozalab.innbalance.dto.PlaceDTO;
import com.mimozalab.innbalance.exception.ResourceNotFoundException;
import com.mimozalab.innbalance.model.Place;
import com.mimozalab.innbalance.model.User;
import com.mimozalab.innbalance.repository.PlaceRepository;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    // Получить места пользователя
    public List<PlaceDTO> getUserPlaces(User user) {
        return placeRepository.findByUserId(user.getId())
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Добавить новое место
   public PlaceDTO addPlace(PlaceDTO dto, User user) {
        Place place = new Place();
        place.setName(dto.getName());
        place.setDescription(dto.getDescription());
        place.setLatitude(dto.getLatitude());
        place.setLongitude(dto.getLongitude());
        place.setImageUrl(dto.getImageUrl());
        place.setRating(0);
        place.setIsDefault(false);
        place.setUser(user);
        place.setCreatedAt(LocalDateTime.now());

        return convertToDTO(placeRepository.save(place));
    }

    // Обновить место
    public PlaceDTO updatePlace(Long id, PlaceDTO placeDTO) {
        Place place = placeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Place not found"));

        if (placeDTO.getRating() != null) {
            place.setRating(placeDTO.getRating());
        }
        if (placeDTO.getName() != null) {
            place.setName(placeDTO.getName());
        }
        if (placeDTO.getDescription() != null) {
            place.setDescription(placeDTO.getDescription());
        }

        return convertToDTO(placeRepository.save(place));
    }

    // Удалить место
    public void deletePlace(Long id) {
        Place place = placeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Place not found"));
        placeRepository.delete(place);
    }

    // Entity → DTO
    private PlaceDTO convertToDTO(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setDescription(place.getDescription());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setImageUrl(place.getImageUrl());
        dto.setRating(place.getRating());
        dto.setIsDefault(place.getIsDefault());
        return dto;
    }
}
