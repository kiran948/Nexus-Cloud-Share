package in.nexus.NexusClousShare.Document;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {

    @Id
    @Column(length = 36)
    private String id;
    private String clerkId;
    private String orderId;
    private String paymentId;
    private String planId;
    private int amount;
    private String currency;
    private int creditsAdded;
    private String status;
    private LocalDateTime transactionDate;

    private String userEmail;
    private String userName;

    @PrePersist
    public void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }
}
