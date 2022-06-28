package basarab.olexandr.springfinalproject.repository.spec;

import basarab.olexandr.springfinalproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter @Setter
@AllArgsConstructor
public class FindTeammateSpecification implements Specification<User> {

    private Integer socialRating;

    private String nationality;

    private String motherTongue;

//    private String gameName;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.socialRating != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("socialRating"), this.socialRating));
        }
        if (!StringUtils.isBlank(this.nationality)) {
            predicates.add(criteriaBuilder.equal(root.get("nationality"), this.nationality));
        }
        if (!StringUtils.isBlank(this.motherTongue)) {
            predicates.add(criteriaBuilder.equal(root.get("motherTongue"), this.motherTongue));
        }
//        if (!StringUtils.isBlank(this.gameName)) {
//            predicates.add(criteriaBuilder.equal(root.join("game").get("gameName"), this.gameName.toUpperCase(Locale.ROOT)));
//        }
//        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isLookingForMatch"), true)));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
