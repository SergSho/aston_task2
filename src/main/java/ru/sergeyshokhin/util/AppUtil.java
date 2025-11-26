package ru.sergeyshokhin.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sergeyshokhin.entity.User;

public class AppUtil {
    public static SessionFactory getSessionFactory (){
        return new Configuration()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
}
