package com.Vhytor.SmartRent.services.serviceimpls;

import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.model.ViewingRecord;
import com.Vhytor.SmartRent.services.LandlordService;

import java.util.List;

public class LandlordServiceImpl implements LandlordService {
    @Override
    public List<Home> getMyProperties(User landlord) {
        return List.of();
    }

    @Override
    public List<ViewingRecord> getMyViewingRecords(long homeId, User landlord) {
        return List.of();
    }
}
