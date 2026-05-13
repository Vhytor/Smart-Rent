package com.Vhytor.SmartRent.services.serviceimpls;

import com.Vhytor.SmartRent.dtos.request.CreateHomeRequest;
import com.Vhytor.SmartRent.exceptions.PropertyNotFoundException;
import com.Vhytor.SmartRent.exceptions.UnauthorizedAccessException;
import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.model.ViewingRecord;
import com.Vhytor.SmartRent.repositories.HomeRepository;
import com.Vhytor.SmartRent.repositories.ViewingRecordRepository;
import com.Vhytor.SmartRent.services.LandlordService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LandlordServiceImpl implements LandlordService {

    private final HomeRepository homeRepository;
    private final ViewingRecordRepository viewingRecordRepository;

    public LandlordServiceImpl(HomeRepository homeRepository, ViewingRecordRepository viewingRecordRepository) {
        this.homeRepository = homeRepository;
        this.viewingRecordRepository = viewingRecordRepository;
    }
    @Override
    @Cacheable(value = "landlord-properties", key = "#landlord.userId")
    public List<Home> getMyProperties(User landlord) {
        // We'll need to add findByLandlord to HomeRepository
        return homeRepository.findByLandlord(landlord);
    }

    @Override
    @Cacheable(value = "viewing-records", key = "#homeId")
    public List<ViewingRecord> getMyViewingRecords(long homeId, User landlord) {
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new PropertyNotFoundException(homeId));

        if (!home.getLandlord().getUserId().equals(landlord.getUserId())) {
            throw new UnauthorizedAccessException("You do not own this property" + homeId);
        }
        return viewingRecordRepository.findByHomeHomeId(homeId);
    }

    @Override
    @CacheEvict(value = "landlord-properties", key = "#landlord.userId")
    public Home createProperty(CreateHomeRequest request, User landlord) {
        Home home = new Home();
        home.setLandlord(landlord);
        home.setAddress(request.getAddress());
        home.setDescription(request.getDescription());
        home.setPricePerMonth(request.getPricePerMonth());
        home.setViewingFee(request.getViewingFee());
        home.setLatitude(request.getLatitude());
        home.setLongitude(request.getLongitude());
        return homeRepository.save(home);
    }
}
