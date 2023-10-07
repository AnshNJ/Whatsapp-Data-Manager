package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<User> users;
    private HashMap<Integer , Message> messageMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.users = new HashSet<>();
        this.messageMap = new HashMap<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) {
        User currUser = new User(name , mobile);
        if(users.contains(currUser)) return null;
        users.add(currUser);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        int groupSize = users.size();
        User admin = null;
        String groupName = "";
        if(groupSize == 2){
            groupName = users.get(1).getName();
            admin = users.get(1);
        } else {
            customGroupCount++;
            groupName = "Group "+customGroupCount;
            admin = users.get(0);
        }
        Group newGroup = new Group(groupName , groupSize);
        groupUserMap.put(newGroup, users); //Update group with their name and users
        adminMap.put(newGroup , admin); //set admin
        return newGroup;
    }

    public int createMessage(String content) {
        messageId++;
        messageMap.put(messageId , new Message(messageId , content , new Date()));
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if(!groupUserMap.get(group).contains(sender)) throw new Exception("You are not allowed to send message");

        List<Message> messageList = new ArrayList<>();
        if( groupMessageMap.containsKey(group) ){
            messageList = groupMessageMap.get(group);
        }
        messageList.add(message);

        groupMessageMap.put(group , messageList); //add message to group message list

        senderMap.put(message , sender); //Update sender

        return messageList.size(); //return number of messages sent in this group
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if(adminMap.get(group) != approver) throw new Exception("Approver does not have rights");
        if(!groupUserMap.get(group).contains(user)) throw new Exception("User is not a participant");

        adminMap.put(group , user); //update admin mapped to given group
        return "SUCCESS";
    }

//    public int removeUser(User user) {
//
//    }

    public Boolean findMessage(Date start, Date end, int k , StringBuilder message) {
        int right = messageMap.size();
        if(k > right) return false;
        for(int i=right; i>=0; i--){
            Message currMessage = messageMap.get(i);
            while(i>=0 && k!=0 && currMessage.getTimestamp().before(end)){
                k--;
                i--;
            }
            if(k==0){
                message.append(currMessage.getContent());
                return true;
            }
        }
        return false;
    }
}
