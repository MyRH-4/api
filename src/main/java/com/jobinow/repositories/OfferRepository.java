package com.jobinow.repositories;

import com.jobinow.model.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link Offer} entity.
 */
@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    @Query("SELECT Offer FROM Offer Offer " +
            "JOIN Apply apply ON apply.Offer = Offer.id " +
            "JOIN User jobSeeker ON apply.jobSeeker = jobSeeker.id " +
            "WHERE jobSeeker.id = :jobSeekerUuid")
    List<Offer> findJobSeekerAppliedToOffers(UUID jobSeekerUuid);
}