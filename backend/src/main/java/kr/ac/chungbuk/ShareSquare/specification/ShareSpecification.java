package kr.ac.chungbuk.ShareSquare.specification;

import kr.ac.chungbuk.ShareSquare.entity.Share;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;


public class ShareSpecification {

    public static Specification<Share> Undeleted(){
        return new Specification<Share>() {
            @Override
            public Predicate toPredicate(Root<Share> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("is_deleted"), false);
            }
        };
    }

    public static Specification<Share> equalShareCategory(String category){
        return new Specification<Share>() {
            @Override
            public Predicate toPredicate(Root<Share> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("category"), category);
            }
        };
    }

    public static Specification<Share> LikeTitle(String search){
        return new Specification<Share>() {
            @Override
            public Predicate toPredicate(Root<Share> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("title"), "%"+search+"%");
            }
        };
    }

    public static Specification<Share> LikeContent(String search){
        return new Specification<Share>() {
            @Override
            public Predicate toPredicate(Root<Share> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("content"), "%"+search+"%");
            }
        };

    }


}
