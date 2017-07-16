package com.vc.service.controller;

import com.vc.model.User;
import com.vc.repository.UserRepository;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacationPlannerUserService
{
  @Autowired
  private UserRepository userRepository;
  
  @RequestMapping({"/getuser"})
  public String index()
  {
    return "Welcome to vacation planner!";
  }
  
  @PostMapping(path={"/adduser"})
  @ResponseBody
  public int addNewUser(@RequestBody User user)
  {
    try
    {
      System.out.println("Inside add user method:" + user);
      User createduser = (User)this.userRepository.save(user);
      System.out.println("Inside add user method:" + createduser);
      return createduser.getID();
    }
    catch (Exception e) {}
    return 0;
  }
  
  @GetMapping(path={"/getuser/{id}"})
  @ResponseBody
  public User getUser(@PathVariable("id") String userid)
  {
    List<User> users = (List)this.userRepository.findAll();
    Iterator userIterator = users.iterator();
    while (userIterator.hasNext())
    {
      User user = (User)userIterator.next();
      if (user.getUserid().equals(userid)) {
        return user;
      }
    }
    return null;
  }
}
