package ee.stivka.luka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "visitor_count")
public class VisitorCount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long count;

}
