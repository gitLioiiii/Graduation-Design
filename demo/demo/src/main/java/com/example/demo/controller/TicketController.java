package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.TicketEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidateFailedException;
import com.example.demo.service.TicketService;
import com.example.demo.utils.ResultTemplate;
import com.example.demo.validator.TicketValidateGroup;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public ResultTemplate index(
        @RequestParam(name = "page", required = false) Integer page, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize, 
        @RequestParam(name = "ticketName", required = false) String ticketName,
        @RequestParam(name = "ticketType", required = false) String ticketType
    ) {
        ResultTemplate result = new ResultTemplate();
        
        // 使用 MyBatis-Plus 分页查询
        IPage<TicketEntity> pageResult = this.ticketService.page(page, pageSize, ticketName, ticketType);
        
        // 将分页结果放入响应
        result.putPayload("tickets", pageResult.getRecords());  // 当前页的数据列表

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("total", pageResult.getTotal());              
        pagination.put("pageSize", pageResult.getSize());           
        pagination.put("currentPage", pageResult.getCurrent());     
        pagination.put("pageCount", pageResult.getPages());         
        result.putPayload("pagination", pagination);

        return result;
    }

    @PostMapping("/create")
    public ResultTemplate create(
        @RequestBody @Validated(TicketValidateGroup.Create.class) TicketEntity ticket,
        BindingResult bindingResult
    ) throws ValidateFailedException {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        this.ticketService.create(ticket);

        return result;
    }

    @GetMapping("/{id}")
    public ResultTemplate fetch(
        @PathVariable Integer id
    ) throws NotFoundException {
        ResultTemplate result = new ResultTemplate();

        // 使用 Optional 的 orElseThrow 方法，如果门票不存在则抛出异常
        // 这种方式更加简洁和安全，避免了空指针检查
        TicketEntity ticket = this.ticketService.fetch(id)
            .orElseThrow(() -> new NotFoundException());

        result.putPayload("ticket", ticket);

        return result;
    }

    @PostMapping("/update")
    public ResultTemplate update(
        @RequestBody @Validated(TicketValidateGroup.Update.class) TicketEntity fields,
        BindingResult bindingResult
    ) throws ValidateFailedException, NotFoundException {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        // 使用 Optional 的 orElseThrow 方法，如果门票不存在则抛出异常
        TicketEntity ticket = this.ticketService.fetch(fields.getId()).orElseThrow(
                () -> new NotFoundException());

        ticket.setTicketName(fields.getTicketName());
        ticket.setTicketType(fields.getTicketType());
        ticket.setStatus(fields.getStatus());
        ticket.setTimeType(fields.getTimeType());
        ticket.setDayPrice(fields.getDayPrice());
        ticket.setWeekendPrice(fields.getWeekendPrice());

        // 如果提供了新的门票图片，则更新图片
        if (fields.getTicketImage() != null) {
            ticket.setTicketImage(fields.getTicketImage());
        }

        this.ticketService.update(ticket);

        return result;
    }

    @PostMapping("/remove")
    public ResultTemplate remove(
        @RequestBody @Validated(TicketValidateGroup.Remove.class) TicketEntity fields,
        BindingResult bindingResult
    ) throws ValidateFailedException, NotFoundException {
        ResultTemplate result = new ResultTemplate();
        
        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        // 使用 Optional 的 orElseThrow 方法，如果门票不存在则抛出异常
        TicketEntity ticket = this.ticketService.fetch(fields.getId()).orElseThrow(
            () -> new NotFoundException()
        );

        // 软删除：设置删除时间戳，而不是物理删除记录
        ticket.setDeletedAt(LocalDateTime.now());
        this.ticketService.remove(ticket);

        return result;
    }
}

