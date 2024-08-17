package zikirdinova.mvc.entities;

import jakarta.persistence.*;
import lombok.*;
import zikirdinova.mvc.enums.Currency;

@Entity
@Table(name = "cashes")
@Data

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor
public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_gen")
    @SequenceGenerator(name = "cash_gen", sequenceName = "cash_seq", allocationSize = 1)
    private Long id;
    private String cashName;
    private String password;
    private double balance;
    private Currency currency;


    }
