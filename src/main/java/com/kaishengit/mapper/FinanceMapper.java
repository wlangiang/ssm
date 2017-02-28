package com.kaishengit.mapper;

import com.kaishengit.dto.IncomeValue;
import com.kaishengit.pojo.Finance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23.
 */
public interface FinanceMapper {

    Long count();

    void save(Finance finance);


    Long fileCount(Map<String, String> param);


    List<Finance> findByParam(Map<String, String> param);

    Finance findById(Integer id);

    void updateState(Finance finance);

    List<Finance> findByDate(String today);

    List<IncomeValue> findMoney(Map<String, String> param);
}
