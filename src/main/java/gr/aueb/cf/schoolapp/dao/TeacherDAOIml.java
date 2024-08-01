package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.model.Teacher;

import java.util.List;

public class TeacherDAOIml implements ITeacherDAO {
    @Override
    public Teacher insert(Teacher teacher) throws TeacherDAOException {
        return null;
    }

    @Override
    public Teacher update(Teacher teacher) throws TeacherDAOException {
        return null;
    }

    @Override
    public void delete(Integer id) throws TeacherDAOException {

    }

    @Override
    public Teacher getById(Integer id) throws TeacherDAOException {
        return null;
    }

    @Override
    public List<Teacher> getByLastname(String lastname) throws TeacherDAOException {
        return List.of();
    }
}
