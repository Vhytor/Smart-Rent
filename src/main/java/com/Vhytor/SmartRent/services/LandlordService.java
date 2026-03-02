package com.Vhytor.SmartRent.services;

import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.model.ViewingRecord;

import java.util.List;

public interface LandlordService {
    List<Home> getMyProperties(User landlord);
    List<ViewingRecord> getMyViewingRecords(long homeId, User landlord);
}
