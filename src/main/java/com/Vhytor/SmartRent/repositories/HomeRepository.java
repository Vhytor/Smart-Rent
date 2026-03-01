package com.Vhytor.SmartRent.repositories;

import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {


}
