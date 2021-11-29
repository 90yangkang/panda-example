package com.example.pandaexampleshardingjdbc;



import com.example.pandaexampleshardingjdbc.dao.PersonDao;
import com.example.pandaexampleshardingjdbc.dao.RoomDao;
import com.example.pandaexampleshardingjdbc.entity.Person;
import com.example.pandaexampleshardingjdbc.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private RoomDao roomDao;

    @GetMapping("/getuser")
    public Object getUser(String tid) {
        TradeUtils.setLoginUser(tid);

        Person user = personDao.selectById(1);


        return user;
    }


    @GetMapping("/getroom")
    public Object getroom(String tid) {
        TradeUtils.setLoginUser(tid);

        Room user = roomDao.selectById(1);


        return user;
    }



}


