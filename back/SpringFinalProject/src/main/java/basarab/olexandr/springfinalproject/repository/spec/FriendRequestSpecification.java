package basarab.olexandr.springfinalproject.repository.spec;

import basarab.olexandr.springfinalproject.entity.FriendRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestSpecification implements Specification<FriendRequest> {

    private String username;

    private Integer socialRating;

    private String nationality;

    private String motherTongue;

    public FriendRequestSpecification(String username, Integer socialRating, String nationality, String motherTongue) {
        this.username = username;
        this.socialRating = socialRating;
        this.nationality = nationality;
        this.motherTongue = motherTongue;
    }

    @Override
    public Predicate toPredicate(Root<FriendRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isBlank(username)) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.join("userSender").get("username"), "%" + username + "%"),
                    criteriaBuilder.like(root.join("userReceiver").get("username"), "%" + username + "%"))
                    );
        }
        if (socialRating != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.greaterThanOrEqualTo(root.join("userSender").get("socialRating"), socialRating),
                    criteriaBuilder.greaterThanOrEqualTo(root.join("userReceiver").get("socialRating"), socialRating))
            );
        }
        if (!StringUtils.isBlank(nationality)) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.join("userSender").get("nationality"), "%" + nationality + "%"),
                    criteriaBuilder.like(root.join("userReceiver").get("nationality"), "%" + nationality + "%"))
            );
        }
        if (!StringUtils.isBlank(motherTongue)) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.join("userSender").get("motherTongue"), "%" + motherTongue + "%"),
                    criteriaBuilder.like(root.join("userReceiver").get("motherTongue"), "%" + motherTongue + "%"))
            );
        }
        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("accepted"), true)));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
