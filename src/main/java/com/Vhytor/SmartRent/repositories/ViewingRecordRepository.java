package com.Vhytor.SmartRent.repositories;

import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.model.ViewingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ViewingRecordRepository extends JpaRepository<ViewingRecord, Long> {
    List<ViewingRecord> findByHomeHomeIdAndAccessCode(Long homeId, String code);
}
