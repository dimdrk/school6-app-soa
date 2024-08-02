package gr.aueb.cf.schoolapp.dto;

public class TeacherReadOnlyDTO extends BaseDTO {

    private Integer id;

    public TeacherReadOnlyDTO() {}

    public TeacherReadOnlyDTO(Integer id) {
        this.id = id;
    }

    public TeacherReadOnlyDTO(String firstname, String lastname, Integer id) {
        super(firstname, lastname);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" + id + ", " + getFirstname() + ", " + getLastname() + "}";
    }
}