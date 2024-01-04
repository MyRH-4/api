package com.jobinow.services.impl;

import com.jobinow.exceptions.ResourceNotFoundException;
import com.jobinow.model.dto.requests.OfferRequest;
import com.jobinow.model.dto.responses.OfferResponse;
import com.jobinow.model.entities.*;
import com.jobinow.model.mapper.OfferMapper;
import com.jobinow.repositories.OfferRepository;
import com.jobinow.services.spec.JobSeekerService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link JobSeekerService} interface for job seeker-related operations.
 */

@RequiredArgsConstructor
public class jobSeekerServiceImpl extends _ServiceImp<UUID, OfferRequest, OfferResponse, Offer, OfferRepository, OfferMapper> implements JobSeekerService{
    private final OfferRepository OfferRepository;

    /**
     * Apply to a job by submitting an application for a specific job offer.
     *
     * @param Offer The job offer to apply to.
     * @return The application object representing the submitted application.
     */
    @Override
    public Apply applyToJob(Offer Offer) {
        return null;
    }

    /**
     * Get a list of job offers to which the job seeker has applied.
     *
     * @return A list of job offers for which the job seeker has submitted applications.
     */
    @Override
    public List<Offer> getAppliedJobs(User jobSeeker) {
        List<Offer> appliedJobs = OfferRepository.findJobSeekerAppliedToOffers(jobSeeker.getId());
        if (appliedJobs.isEmpty()) throw new ResourceNotFoundException("Job Seeker haven't applied to any offers with id " + jobSeeker.getId());
        return appliedJobs;
    }

    /**
     * Get a list of job offers that the job seeker has saved for future reference.
     *
     * @return A list of saved job offers.
     */
    @Override
    public List<Offer> getSavedJobs() {
        return null;
    }

    /**
     * Get a list of recommended job offers based on the job seeker's profile and preferences.
     *
     * @return A list of recommended job offers.
     */
    @Override
    public List<Offer> getRecommendedJobs() {
        return null;
    }

    /**
     * Get a list of job offers based on the job seeker's profile.
     *
     * @param profil The job seeker's profile.
     * @return A list of job offers that to match the job seeker's profile.
     */
    @Override
    public List<Offer> getJobsByProfile(Profil profil) {
        return null;
    }

    /**
     * Get a list of job offers based on a specific location.
     *
     * @param location The location for which to retrieve a job offers.
     * @return A list of job offers available in the specified location.
     */
    @Override
    public List<Offer> getJobsByLocation(String location) {
        return null;
    }

    /**
     * Get a list of job offers based on a specified salary range.
     *
     * @param salary The salary range for which to retrieve job offers.
     * @return A list of job offers within the specified salary range.
     */
    @Override
    public List<Offer> getJobsBySalary(double salary) {
        return null;
    }

    /**
     * Get a list of job offers based on a specific job title.
     *
     * @param title The job title for which to retrieve job offers.
     * @return A list of job offers with the specified job title.
     */
    @Override
    public List<Offer> getJobsByTitle(String title) {
        return null;
    }

    /**
     * Get a list of job offers associated with a specific company.
     *
     * @param company The company for which to retrieve job offers.
     * @return A list of job offers associated with the specified company.
     */
    @Override
    public List<Offer> getJobsByCompany(Company company) {
        return null;
    }
}
