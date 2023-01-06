package com.example.excel.excel.controller;

import com.example.excel.excel.entity.User;
import com.example.excel.excel.exception.ResourseNotFoundException;
import com.example.excel.excel.exporter.UserExcelExporter;
import com.example.excel.excel.repository.UserRepository;
import com.example.excel.excel.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserServices service;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = service.listAll();

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

        excelExporter.export(response);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userRepository.findAll(Sort.by("fullName").descending()),HttpStatus.ACCEPTED);
    }

    @PostMapping("/saveUsers")
    public ResponseEntity<User> createUsers(@RequestBody User user){
      return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);

    }

    @DeleteMapping("/deleteUsers")
    public String deleteUsers(){

         userRepository.deleteAll();
        return "User List Deleted";
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable ("id") Integer id)
    {
        User existing = this.userRepository.findById(id).orElseThrow(()-> new ResourseNotFoundException("User not found with id : " +id));
        this.userRepository.delete(existing);
        return ResponseEntity.ok().build();
    }

}
