package com.example.avialine.repo;

import com.example.avialine.enums.Currency;
import com.example.avialine.enums.PaxCode;
import com.example.avialine.model.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TariffRepo extends JpaRepository<Tariff, Integer> {

    @Query(
            """
        SELECT t FROM Tariff t
        WHERE t.flight.id = :flightId
        AND t.paxCode = :paxCode
        AND t.currency = :currency
""")
    Optional<Tariff> findTariff(
            @Param("flightId") Integer flightId,
            @Param("paxCode") PaxCode paxCode,
            @Param("currency") Currency currency
    );
}
