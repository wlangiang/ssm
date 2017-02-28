package com.kaishengit.service.impl;

import com.kaishengit.dto.IncomeValue;
import com.kaishengit.mapper.FinanceMapper;
import com.kaishengit.pojo.Finance;
import com.kaishengit.service.FinanceService;
import com.kaishengit.shiro.ShiroUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/23.
 */
@Service
public class FinanceServiceImpl implements FinanceService {
    @Autowired
    private FinanceMapper financeMapper;

    @Override
    public Long count() {
        return financeMapper.count();
    }

    @Override
    public Long fileCount(Map<String, String> param) {

        return financeMapper.fileCount(param);
    }

    @Override
    public List<Finance> findFinByParam(Map<String, String> param) {
        return financeMapper.findByParam(param);
    }

    @Override
    public void changeById(Integer id) {
        Finance finance = financeMapper.findById(id);
        if(finance!=null){
            finance.setState(Finance.STATE_SUCCESS);
            finance.setConfirmUser(ShiroUtil.getCurrentUserName());
            finance.setConfirmDate(DateTime.now().toString("YYYY-MM-dd"));
            financeMapper.updateState(finance);
        }
    }

    @Override
    public List<Finance> findByCreateDate(String today) {

        return financeMapper.findByDate(today);
    }

    @Override
    public List<IncomeValue> findMoney(Map<String, String> param) {
        List<IncomeValue> financeList = financeMapper.findMoney(param);


        return financeList;
    }
}
