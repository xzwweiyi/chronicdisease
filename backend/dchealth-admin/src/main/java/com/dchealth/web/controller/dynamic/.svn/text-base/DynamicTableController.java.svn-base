package com.dchealth.web.controller.dynamic;

import com.dchealth.dynamic.exception.DynamicException;
import com.dchealth.dynamic.service.DynamicTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dynamic")
public class DynamicTableController {

    @Autowired
    private DynamicTableService dynamicTableService;


    /**
     *
     * @return
     */
    @PostMapping("/createTable")
    public boolean createTable() {
        dynamicTableService.createTable();
        return true;
    }

    @PostMapping("/createTableById")
    public boolean createTableById(int id) throws DynamicException {
        dynamicTableService.createTableBySuffix(id);
        return true;
    }

}
