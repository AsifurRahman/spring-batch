package com.asif.batchprocessing.config;

import com.asif.batchprocessing.model.Student;
import org.springframework.batch.item.ItemProcessor;


    @Override
    public Student process(Student student) throws Exception {
        return student;
    }
}
