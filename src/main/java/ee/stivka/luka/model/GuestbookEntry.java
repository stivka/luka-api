package ee.stivka.luka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "guestbook_entries")
public class GuestbookEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 500)
    private String message;
    private LocalDateTime date;

    public GuestbookEntry(String name, String message, LocalDateTime date) {
        this.name = name;
        this.message = message;
        this.date = date;
    }

}
