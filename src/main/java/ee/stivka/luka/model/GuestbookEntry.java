package ee.stivka.luka.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "guestbook_entries")
@Data
public class GuestbookEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String message;

}
