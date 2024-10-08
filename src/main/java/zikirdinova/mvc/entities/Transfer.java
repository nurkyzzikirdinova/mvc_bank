package zikirdinova.mvc.entities;

import jakarta.persistence.*;
import lombok.Data;
import zikirdinova.mvc.enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Data
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfer_gen")
    @SequenceGenerator(name = "transfer_gen", sequenceName = "transfer_seq", allocationSize = 1)
    private Long id;

    private double amount;

    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String comment;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "from_cash_id")
    private Cash fromCash;

    @ManyToOne
    @JoinColumn(name = "to_cash_id")
    private Cash toCash;

    private String senderFullName;
    private String receiverFullName;
    private String senderPhoneNumber;
    private String receiverPhoneNumber;
}
