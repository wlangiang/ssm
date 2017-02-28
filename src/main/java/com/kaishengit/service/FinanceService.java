package com.kaishengit.service;

import com.kaishengit.dto.IncomeValue;
import com.kaishengit.pojo.Finance;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23.
 */
public interface FinanceService {


    Long count();

    Long fileCount(Map<String, String> param);

    List<Finance> findFinByParam(Map<String, String> param);

    void changeById(Integer id);

    List<Finance> findByCreateDate(String today);

    List<IncomeValue> findMoney(Map<String, String> param);
}
