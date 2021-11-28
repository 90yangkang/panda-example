package com.example.shardingjdbc;


import com.example.shardingjdbc.dao.PersonDao;
import com.example.shardingjdbc.dao.RoomDao;
import com.example.shardingjdbc.entity.Person;
import com.example.shardingjdbc.entity.Room;
import org.apache.shardingsphere.api.hint.HintManager;
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


