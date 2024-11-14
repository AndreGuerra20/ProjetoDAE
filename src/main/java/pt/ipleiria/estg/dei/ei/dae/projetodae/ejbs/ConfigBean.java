package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class ConfigBean {

    @EJB
    private StudentBean studentBean;
    @PostConstruct
    public void populateDB() {
        studentBean.create("student1", "password1", "Student 1", "estudante1@gmail.com");
        studentBean.create("student2", "password2", "Student 2", "estudante2@gmail.com");
        studentBean.create("student3", "password3", "Student 3", "estudante3@gmail.com");
    }
}
