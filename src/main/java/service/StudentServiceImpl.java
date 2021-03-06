package service;

import model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.StudentRepository;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void remove(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Iterable<Student> findAllByOrderByScoreDesc() {
        return studentRepository.findAllByOrderByScoreDesc();
    }

    @Override
    public Iterable<Student> findAllByOrderByScoreAsc() {
        return studentRepository.findAllByOrderByScoreAsc();
    }

    @Override
    public Iterable<Student> findByNameContaining(String name) {
        return studentRepository.findByNameContaining(name);
    }

    @Override
    public Iterable<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }
}



