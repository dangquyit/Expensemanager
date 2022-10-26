package com.junior.expensemanager.service;

import com.junior.expensemanager.dto.ExpenseDTO;
import com.junior.expensemanager.entity.Expense;
import com.junior.expensemanager.repository.ExpenseRepository;
import com.junior.expensemanager.util.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<ExpenseDTO> listExpenseDTO = listExpense.stream().map(this::mapToDTO).collect(Collectors.toList());
        return listExpenseDTO;
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
}
