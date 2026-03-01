package com.Vhytor.SmartRent.services;

import com.Vhytor.SmartRent.model.User;

import java.util.Map;

public interface ViewingService {
    // Contract: Give me a homeId and a User, I'll give you access details
    Map<String, Object> processViewingRequest(Long homeId, User user);
}
