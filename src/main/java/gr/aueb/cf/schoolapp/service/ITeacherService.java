package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.exceptions.TeachersNotFoundException;

import java.util.List;

public interface ITeacherService {
    Teacher insertTeacher(TeacherInsertDTO dto) throws TeacherDAOException;
    Teacher updateTeacher(TeacherUpdateDTO dto) throws TeachersNotFoundException, TeacherDAOException;
    void deleteTeacher(Integer id) throws TeacherDAOException, TeachersNotFoundException;
    Teacher getTeacherById(Integer id) throws TeachersNotFoundException, TeacherDAOException;
    List<Teacher> getTeachersByLastname(String lastname) throws TeacherDAOException;
}
