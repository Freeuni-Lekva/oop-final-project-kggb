package DAOs;

import java.sql.SQLException;

public class MainInsertIntoTables {

    public static void main(String[] args) throws SQLException {
        UserDAO.createUser("tgela23", "Tamar", "Gelashvili", "root",
                "https://media.istockphoto.com/id/2171382633/vector/user-profile-icon-anonymous-person-symbol-blank-avatar-graphic-vector-illustration.jpg?s=612x612&w=0&k=20&c=ZwOF6NfOR0zhYC44xOX06ryIPAUhDvAajrPsaZ6v1-w=");
        UserDAO.createUser("ekali23", "Elene", "Kalinichenko", "root",
                "https://media.istockphoto.com/id/2171382633/vector/user-profile-icon-anonymous-person-symbol-blank-avatar-graphic-vector-illustration.jpg?s=612x612&w=0&k=20&c=ZwOF6NfOR0zhYC44xOX06ryIPAUhDvAajrPsaZ6v1-w=");
        UserDAO.createUser("lbati23", "Luka", "Batilashvili", "root",
                "https://media.istockphoto.com/id/2171382633/vector/user-profile-icon-anonymous-person-symbol-blank-avatar-graphic-vector-illustration.jpg?s=612x612&w=0&k=20&c=ZwOF6NfOR0zhYC44xOX06ryIPAUhDvAajrPsaZ6v1-w=");

        AdminUserDAO.addAdminUser("tgela23");
        AdminUserDAO.addAdminUser("lbati23");
        AdminUserDAO.addAdminUser("ekali23");

        AnnouncementDAO.addAnnouncement("new quiz", "Check out our new absolutely challenging and show-stopping quiz",
                "NEW QUIZ ANNOUNCEMENT", "lbati23");


    }

}
