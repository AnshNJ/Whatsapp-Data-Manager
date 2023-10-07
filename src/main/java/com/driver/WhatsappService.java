package com.driver;

import java.util.Date;
import java.util.List;

public class WhatsappService {

    WhatsappRepository whatsappRepo = new WhatsappRepository();
    public String createUser(String name, String mobile) {
        return whatsappRepo.createUser(name , mobile);
    }

    public Group createGroup(List<User> users) {
        return whatsappRepo.createGroup(users);
    }

    public int createMessage(String content) {
        return whatsappRepo.createMessage(content);
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        return whatsappRepo.sendMessage(message , sender , group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        return whatsappRepo.changeAdmin(approver , user , group);
    }

//    public int removeUser(User user) {
//        return whatsappRepo.removeUser(user);
//    }

    public String findMessage(Date start, Date end, int k) throws Exception{
        StringBuilder message = new StringBuilder();
        Boolean messageFound = whatsappRepo.findMessage(start , end , k , message);
        if(!messageFound) throw new Exception("K is greater than the number of messages");
        return message.toString();
    }
}
