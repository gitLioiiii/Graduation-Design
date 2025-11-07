package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.StockEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.ValidateFailedException;
import com.example.demo.service.StockService;
import com.example.demo.utils.ResultTemplate;
import com.example.demo.validator.StockValidateGroup;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("")
    public ResultTemplate index(
        @RequestParam(name = "page", required = false) Integer page, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        ResultTemplate result = new ResultTemplate();
        
        // 使用 MyBatis-Plus 分页查询
        IPage<StockEntity> pageResult = this.stockService.page(page, pageSize);
        
        // 将分页结果放入响应
        result.putPayload("stocks", pageResult.getRecords());  // 当前页的数据列表
        
        // 构建分页信息
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
        @RequestBody @Validated(StockValidateGroup.Create.class) StockEntity stock,
        BindingResult bindingResult
    ) throws ValidateFailedException {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        this.stockService.create(stock);

        return result;
    }

    @GetMapping("/{id}")
    public ResultTemplate fetch(
        @PathVariable Integer id
    ) throws NotFoundException {
        ResultTemplate result = new ResultTemplate();

        // 使用 Optional 的 orElseThrow 方法，如果库存不存在则抛出异常
        StockEntity stock = this.stockService.fetch(id)
            .orElseThrow(() -> new NotFoundException());

        result.putPayload("stock", stock);

        return result;
    }

    @PostMapping("/update")
    public ResultTemplate update(
        @RequestBody @Validated(StockValidateGroup.Update.class) StockEntity fields,
        BindingResult bindingResult
    ) throws ValidateFailedException, NotFoundException {
        ResultTemplate result = new ResultTemplate();

        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        // 使用 Optional 的 orElseThrow 方法，如果库存不存在则抛出异常
        StockEntity stock = this.stockService.fetch(fields.getId()).orElseThrow(
                () -> new NotFoundException());

        // 更新字段
        stock.setTotalStock(fields.getTotalStock());
        stock.setDayStock(fields.getDayStock());
        stock.setTicketId(fields.getTicketId());

        this.stockService.update(stock);

        return result;
    }

    @PostMapping("/remove")
    public ResultTemplate remove(
        @RequestBody @Validated(StockValidateGroup.Remove.class) StockEntity fields,
        BindingResult bindingResult
    ) throws ValidateFailedException, NotFoundException {
        ResultTemplate result = new ResultTemplate();
        
        if (bindingResult.hasErrors()) {
            throw new ValidateFailedException();
        }

        // 使用 Optional 的 orElseThrow 方法，如果库存不存在则抛出异常
        StockEntity stock = this.stockService.fetch(fields.getId()).orElseThrow(
            () -> new NotFoundException()
        );

        // 物理删除库存记录
        this.stockService.remove(stock);

        return result;
    }
}

