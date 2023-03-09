package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    //stores group and its list of users
    private HashMap<Group, List<User>> groupUserMap;
    //stores group and its list of messages
    private HashMap<Group, List<Message>> groupMessageMap;
    //stores message and user that sent that message
    private HashMap<Message, User> senderMap;
    //stores group and admin
    private HashMap<Group, User> adminMap;
    //Stored mobile number of every user
    private HashSet<String> userMobile;


    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }


    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }else {
            userMobile.add(mobile);
            return "SUCCESS";
        }
    }

    public Group createGroup(List<User> users) {

        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.

        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.

        int groupsize = users.size();
        String admin = users.get(0).getName();
        Group group;
        String groupName;

        if(groupsize == 2){
            groupName = users.get(1).getName();
            group = new Group(groupName,groupsize);
            adminMap.put(group,users.get(0));
//            groupUserMap.put(group,users);
            return group;
        }else {
            customGroupCount++;
            groupName = "";
            groupName += "Group " + customGroupCount;
            group = new Group(groupName,groupsize);
            adminMap.put(group,users.get(0));
            groupUserMap.put(group,users);
            return group;
        }
    }


    public int createMessage(String content) {
        Message msg = new Message(messageId++,content);
        return messageId;
    }


    public int sendMessage(Message message, User sender, Group group) throws Exception {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.
        boolean doesGroupExist = true;
        if (!adminMap.containsKey(group)){
            doesGroupExist = false;
            throw new Exception ("Group does not exist");
        }

        List<User> users = groupUserMap.get(group);
        HashSet<User>  listOfUsers = new HashSet<>();
        for (User user :users){
            listOfUsers.add(user);
        }
        if (doesGroupExist && !listOfUsers.contains(sender)){
            throw new Exception("You are not allowed to send message");
        }

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        senderMap.put(message,sender);
        groupMessageMap.put(group,messages);
        return groupMessageMap.get(group).size();
    }


    public String changeAdmin(User approver, User user, Group group) throws Exception {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

        if (!adminMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if (!adminMap.get(group).equals(approver)){
            throw new Exception("Approver does not have rights");
        }
        List<User> users = groupUserMap.get(group);
        HashSet<User> userHashSet = new HashSet<>();
        for (User user1 :users){
            userHashSet.add(user1);
        }
        if (!userHashSet.contains(user)){
            throw new Exception("User is not a participant");
        }
        adminMap.remove(group,approver);
        adminMap.put(group,user);
        return "SUCCESS";
    }


    public int removeUser(User user) {
        return 0;
    }

    public String findMessage(Date start, Date end, int k) {
        return "MIL GAYA";
    }
}