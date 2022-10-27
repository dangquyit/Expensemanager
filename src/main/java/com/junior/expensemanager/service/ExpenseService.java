package com.junior.expensemanager.service;

import com.junior.expensemanager.dto.ExpenseDTO;
import com.junior.expensemanager.entity.Expense;
import com.junior.expensemanager.repository.ExpenseRepository;
import com.junior.expensemanager.util.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ExpenseDTO> getAllExpenseList() {
        List<Expense> listExpense = expenseRepository.findAll();
        return listExpense.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private ExpenseDTO mapToDTO(Expense expense) {
//        ExpenseDTO expenseDTO = new ExpenseDTO();
//        expenseDTO.setId(expense.getId());
//        expenseDTO.setExpenseId(expense.getExpenseId());
//        expenseDTO.setAmount(expense.getAmount());
//        expenseDTO.setName(expense.getName());
//        expenseDTO.setDescription(expense.getDescription());
//        expenseDTO.setDate(expense.getDate());
//        return expenseDTO;

         ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
         expenseDTO.setDateString(DateTimeUtil.convertToString(expense.getDate()));
         return expenseDTO;
    }

    private Expense mapToEntity(ExpenseDTO expenseDTO) throws ParseException {
        // map the DTO to entity
        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        // TODO: generate the expense id

        // TODO: set the expense date
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDTO.getDateString()));

        // Return expense entity
        return expense;
    }

    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) throws ParseException {
        Expense expense = mapToEntity(expenseDTO);
        expense =  expenseRepository.save(expense);
        return mapToDTO(expense);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public ExpenseDTO findById(Long id) throws ParseException {
        Expense expense =  expenseRepository.findById(id).get();
        return mapToDTO(expense);
    }

    public List<ExpenseDTO> getFilterExpenses(String keyword, String sortBy, Date formDate, Date toDate) throws ParseException {
        List<Expense> expenses=  expenseRepository.findByNameContainingAndDateBetween(keyword, formDate, toDate);
        List<ExpenseDTO> expensesDTO = expenses.stream().map(this::mapToDTO).collect(Collectors.toList());
        if(sortBy != null) {
            if(sortBy.equals("date")) {
                expensesDTO.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            } else {
                expensesDTO.sort((o1, o2) -> o2.getAmount().compareTo(o1.getAmount()));
            }
        }
        return expensesDTO;
    }

    public String totalExpenses(List<ExpenseDTO> expenses) throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        BigDecimal sum = new BigDecimal(0);
        for(ExpenseDTO o : expenses) {
            sum = sum.add(o.getAmount());
        }
        return decimalFormat.format(sum);
    }
}
