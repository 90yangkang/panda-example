package com.example.shardingjdbc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shardingjdbc.entity.Person;
import com.example.shardingjdbc.entity.Room;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface RoomDao extends BaseMapper<Room> {


}
