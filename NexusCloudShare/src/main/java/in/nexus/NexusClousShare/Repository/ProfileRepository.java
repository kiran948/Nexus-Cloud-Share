package in.nexus.NexusClousShare.Repository;

import in.nexus.NexusClousShare.Document.ProfileDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileDocument,String> {
    Optional<ProfileDocument> findByEmail(String email);
    ProfileDocument findByClerkId(String clerkId);

    Boolean existsByClerkId(String clerkId);
}
