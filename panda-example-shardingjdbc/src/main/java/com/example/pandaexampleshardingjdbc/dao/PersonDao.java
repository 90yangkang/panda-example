package com.example.pandaexampleshardingjdbc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pandaexampleshardingjdbc.entity.Person;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface PersonDao extends BaseMapper<Person> {


}
