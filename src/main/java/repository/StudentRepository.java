package repository;

import model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
   Iterable<Student> findAllByOrderByScoreDesc();
   Iterable<Student> findAllByOrderByScoreAsc();
   Iterable<Student> findByNameContaining(String name);
   Iterable<Student> findByName(String name);
}
