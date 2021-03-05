import com.github.moboxs.firstweb.dao.JpaUtils;
import com.github.moboxs.firstweb.entity.Student;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.junit.Test;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

public class StudentDAOTest {

    @Test
    public void persist() {
        //获得实体管理类
        EntityManager manager = JpaUtils.getEntityManager();
        //获得事务管理器
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        //insert
        Student student = new Student();
        student.setStuName("张三");
        student.setStuAge(18);
        manager.persist(student);

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
//        Expression<Set<Object>> keys = cb.keys(new HashMap<>());
//        cq.select();
        //from
        Root<Student> from = cq.from(Student.class);
        cq.select(from);

        //where
        List<Predicate> predicates = new ArrayList<>();
        Predicate like = cb.like(from.get("stuName"), "张三");
        predicates.add(like);
        CriteriaBuilder.In<Object> stuId = cb.in(from.get("stuId")).value(1).value(2);
        predicates.add(stuId);
        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Student> q = manager.createQuery(cq);
        List<Student> items = q.getResultList();

        items.forEach(System.out::println);

        transaction.commit();
        manager.close();

    }
}
