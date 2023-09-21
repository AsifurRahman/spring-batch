package com.asif.batchprocessing.component;

import com.asif.batchprocessing.model.Student;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class StudentWriter implements ItemWriter<Student> {

    @Override
    public void write(Chunk<? extends Student> chunk) throws Exception {

    }
}
