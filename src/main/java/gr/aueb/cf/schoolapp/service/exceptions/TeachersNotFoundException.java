package gr.aueb.cf.schoolapp.service.exceptions;

import gr.aueb.cf.schoolapp.model.Teacher;

import java.io.Serial;

public class TeachersNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public TeachersNotFoundException(Teacher teacher) {
        super("Teacher with id: " + teacher.getId() + " was not found.");
    }

    public TeachersNotFoundException(String s) {
        super(s);
    }

}
