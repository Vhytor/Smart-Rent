package com.Vhytor.SmartRent.services.serviceimpls;

import com.Vhytor.SmartRent.exceptions.PropertyNotFoundException;
import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.repositories.HomeRepository;
import com.Vhytor.SmartRent.services.HomeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HomeServiceImpl implements HomeService {
    private final HomeRepository homeRepository;

    public HomeServiceImpl(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }
    @Override
    @Cacheable(value = "all-homes")
    public List<Home> getAllHomes() {
        return homeRepository.findAll();
    }

    @Override
    @Cacheable(value = "home", key = "#homeId")
    public Home getHomeById(Long homeId) {
        return homeRepository.findById(homeId)
                .orElseThrow(()-> new PropertyNotFoundException(homeId));
    }

    /**
     * Returns all properties within the given radius of a coordinate.
     * Results are ordered by distance — closest first.
     * Not cached since coordinates change per search.
     */
    @Override
    public List<Home> searchNearby(double lat, double lng, double radiusMetres) {
        return homeRepository.findWithinRadius(lat, lng, radiusMetres);
    }
}
