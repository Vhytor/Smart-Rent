package com.Vhytor.SmartRent.repositories;

import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.model.ViewingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewingRecordRepository extends JpaRepository<ViewingRecord, Long> {
}
