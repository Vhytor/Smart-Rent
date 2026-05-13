package com.Vhytor.SmartRent.services;

import com.Vhytor.SmartRent.model.Home;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface HomeService {

    List<Home> getAllHomes();
    Home getHomeById(Long homeId);

    /**
     * Searches for properties within a given radius of a coordinate.
     * Used by the map search feature on the tenant dashboard.
     *
     * @param lat          latitude of the searched location
     * @param lng          longitude of the searched location
     * @param radiusMetres search radius in metres
     */
    List<Home> searchNearby(double lat, double lng, double radiusMetres);
}
