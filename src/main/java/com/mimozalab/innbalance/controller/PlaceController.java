package com.mimozalab.innbalance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mimozalab.innbalance.dto.PlaceDTO;
import com.mimozalab.innbalance.service.PlaceService;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    
    @Autowired
    private PlaceService placeService;
    
    // GET /api/places
    @GetMapping
    public List<PlaceDTO> getAllPlaces(@RequestHeader("Authorization") String token) {
        return placeService.getUserPlaces(token);
    }
    
    // POST /api/places
    @PostMapping
    public PlaceDTO createPlace(@RequestBody PlaceDTO place, @RequestHeader("Authorization") String token) {
        return placeService.addPlace(place, token);
    }
    
    // PUT /api/places/{id}
    @PutMapping("/{id}")
    public PlaceDTO updatePlace(@PathVariable Long id, @RequestBody PlaceDTO place) {
        return placeService.updatePlace(id, place);
    }
    
    // DELETE /api/places/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}