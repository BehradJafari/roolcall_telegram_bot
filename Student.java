package RollCallTelBot;

public class Student {
    private long id;
    private int StudentId;
    private String name;

    public Student(long id, int studentId, String name) {
        this.id = id;
        StudentId = studentId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public int getStudentId() {
        return StudentId;
    }

    public String getName() {
        return name;
    }

    public Student(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return id == student.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
