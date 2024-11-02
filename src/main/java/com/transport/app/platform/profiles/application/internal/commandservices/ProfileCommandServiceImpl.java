package com.transport.app.platform.profiles.application.internal.commandservices;

import com.transport.app.platform.profiles.domain.model.aggregates.Profile;
import com.transport.app.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.transport.app.platform.profiles.domain.model.valueobjects.EmailAddress;
import com.transport.app.platform.profiles.domain.services.ProfileCommandService;
import com.transport.app.platform.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {
    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(CreateProfileCommand command) {
        var emailAddress = new EmailAddress(command.email());
        if (profileRepository.existsByEmail(emailAddress))
            throw new IllegalArgumentException("Profile with email "+ command.email() +"already exists");
        var profile = new Profile(command);
        profileRepository.save(profile);
        return Optional.of(profile);
    }
}
