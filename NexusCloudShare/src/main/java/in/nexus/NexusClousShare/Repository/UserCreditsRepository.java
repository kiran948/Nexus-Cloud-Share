package in.nexus.NexusClousShare.Repository;

import in.nexus.NexusClousShare.Document.UserCredits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCreditsRepository extends JpaRepository<UserCredits, String> {

    Optional<UserCredits> findByClerkId(String clerkId);
}
