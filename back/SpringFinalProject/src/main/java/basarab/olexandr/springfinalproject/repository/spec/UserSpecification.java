package basarab.olexandr.springfinalproject.repository.spec;

import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.enums.Interests;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserSpecification implements Specification<User> {

    private String username;

    private Integer socialRating;

    private String nationality;

    private String motherTongue;

    private Set<String> strInterests = new HashSet<>();

    public UserSpecification(String username, Integer socialRating, String nationality, String motherTongue, Set<String> strInterests) {
        this.username = username;
        this.socialRating = socialRating;
        this.nationality = nationality;
        this.motherTongue = motherTongue;
        this.strInterests = strInterests;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isBlank(username)) {
            predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
        }
        if (socialRating != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("social_rating"), socialRating));
        }
        if (!StringUtils.isBlank(nationality)) {
            predicates.add(criteriaBuilder.like(root.get("nationality"), "%" + nationality + "%"));
        }
        if (!StringUtils.isBlank(motherTongue)) {
            predicates.add(criteriaBuilder.like(root.get("motherTongue"), "%" + motherTongue + "%"));
        }
        if (this.strInterests != null) {
            for (String interest : strInterests) {
                switch (interest) {
                    case "anime" ->
                        predicates.add(criteriaBuilder.equal(root.join("interests").get("interest"), Interests.ANIME));
                    case "computer games" ->
                        predicates.add(criteriaBuilder.equal(root.join("interests").get("interest"), Interests.COMPUTER_GAMES));
                    case "football" ->
                        predicates.add(criteriaBuilder.equal(root.join("interests").get("interest"), Interests.FOOTBALL));
                    case "basketball" ->
                            predicates.add(criteriaBuilder.equal(root.join("interests").get("interest"), Interests.BASKETBALL));
                    case "tennis" ->
                            predicates.add(criteriaBuilder.equal(root.join("interests").get("interest"), Interests.TENNIS));

                    default -> predicates.add(criteriaBuilder.equal(root.join("interests").get("interest"), Interests.NOTHING));
                }
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
