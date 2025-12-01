package pe.edu.upc.recommendations_service.recommendations.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.recommendations_service.recommendations.domain.model.aggregates.Recommendation;

import java.util.List;

@Entity
@Table(name = "recommendation_templates")
@Getter
@Setter
@NoArgsConstructor
public class RecommendationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String category; // Nuevo campo

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Recommendation> recommendations;

    public RecommendationTemplate(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}