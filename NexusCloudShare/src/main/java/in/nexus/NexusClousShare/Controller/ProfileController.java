package in.nexus.NexusClousShare.Controller;

import in.nexus.NexusClousShare.DTO.ProfileDTO;
import in.nexus.NexusClousShare.Services.ProfileServices;
import in.nexus.NexusClousShare.Services.UserCreditsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServices profileServices;
    private final UserCreditsService userCreditsService;

    @PostMapping("/register")
    public ResponseEntity<?> registerProfile(@RequestBody ProfileDTO profileDTO){
        HttpStatus status = profileServices.existsByClerkId(profileDTO.getClerkId()) ?
                HttpStatus.OK : HttpStatus.CREATED;
        ProfileDTO savedProfile = profileServices.createProfile(profileDTO);

        // create initial credits for newly created profile (idempotent if profile existed)
        try {
            userCreditsService.createInitialCredits(savedProfile.getClerkId());
        } catch (Exception ignored) {
            // if credits already exist or creation fails, ignore here — will be created lazily elsewhere
        }

        return ResponseEntity.status(status).body(savedProfile);
    }
}
