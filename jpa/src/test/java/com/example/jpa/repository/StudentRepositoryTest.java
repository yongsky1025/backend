package com.example.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Student;
import com.example.jpa.entity.constant.Grade;


@SpringBootTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;


    @Test
    public void deleteTest(){
        // Student student = studentRepository.findById(1L).get();
        // studentRepository.delete(student);
        studentRepository.deleteById(1L);
    }

    // 학생 1명(id 2번)
    @Test
    public void readTest(){
        Student student = studentRepository.findById(2L).get();
        System.out.println(student);
    }
    // 전체 학생
    @Test
    public void readTest2(){
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            
            System.out.println(student);
        }
    }
    @Test
    public void updateTest(){
        // Entity
        // update student set 수정컬럼 = 값 where id=1;

        Optional<Student> result = studentRepository.findById(1L);
        Student student = result.get();
        // student.changeName("성춘향");
        student.changeGrade(Grade.FRESHMAN);

        studentRepository.save(student);
    }


    // @Test
    // public void insertTest(){
    //     // Entity
    //     Student student = Student.builder()
    //     .name("성춘향")
    //     .addr("서울")
    //     .gender("F")
    //     .grade(Grade.SENIOR)
    //     .build();
        
    //     // // insert(c), update(u) 작업 시 호출
    //     studentRepository.save(student);


    //     // // delete from ~ 호출
    //     // studentRepository.delete(student);
    //     // studentRepository.deleteById(student);

    //     // // select * from where id = 1;
    //     // studentRepository.findById(null);
    //     // // select * from ;
    //     // studentRepository.findAll();
    // }

    
}
