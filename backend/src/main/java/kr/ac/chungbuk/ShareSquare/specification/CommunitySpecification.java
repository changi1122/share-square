package kr.ac.chungbuk.ShareSquare.specification;

import kr.ac.chungbuk.ShareSquare.entity.Community;
import kr.ac.chungbuk.ShareSquare.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class CommunitySpecification {

    public static Specification<Community> Undeleted(){
        return new Specification<Community>() {
            @Override
            public Predicate toPredicate(Root<Community> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("is_deleted"), false);
            }
        };
    }

    public static Specification<Community> LikeTitle(String search){
        return new Specification<Community>() {
            @Override
            public Predicate toPredicate(Root<Community> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("title"), "%"+search+"%");
            }
        };
    }

    public static Specification<Community> LikeContent(String search){
        return new Specification<Community>() {
            @Override
            public Predicate toPredicate(Root<Community> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("content"), "%"+search+"%");
            }
        };
    }
}