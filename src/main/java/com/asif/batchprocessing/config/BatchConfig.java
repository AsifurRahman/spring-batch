package com.asif.batchprocessing.config;

import com.asif.batchprocessing.component.StudentProcessor;
import com.asif.batchprocessing.model.Student;
import com.asif.batchprocessing.repository.StudentRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private StudentRepository studentRepository;

    @Bean
    public FlatFileItemReader<Student> itemReader() {
        FlatFileItemReader<Student> studentReader = new FlatFileItemReader<>();
        studentReader.setName("student-file-reader");
        studentReader.setLinesToSkip(1);
//        studentReader.setResource(new FileSystemResource("src/main/resources/student_data.csv"));
        studentReader.setResource(new FileSystemResource("src/main/resources/student2.csv"));
        studentReader.setLineMapper(studentLineMapper());
        return studentReader;
    }


    @Bean
    public LineMapper<Student> studentLineMapper() {

        //extract csv data from header and field
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(true);
        lineTokenizer.setNames("name", "gender", "country", "email", "age");


        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();
        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        //map csv data into entity
        fieldSetMapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }


    public StudentProcessor studentProcessor() {
        return new StudentProcessor();
    }


    @Bean
    RepositoryItemWriter<Student> itemWriter() {
        RepositoryItemWriter<Student> itemWriter = new RepositoryItemWriter<>();
        itemWriter.setMethodName("save");
        itemWriter.setRepository(studentRepository);
        return itemWriter;
    }


    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("csv-step", jobRepository).
                <Student, Student>chunk(100, transactionManager)
                .reader(itemReader())
                .processor(studentProcessor())
                .writer(itemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("importStudent", jobRepository)
                .flow(step1(jobRepository, transactionManager)).end().build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(5);
        return asyncTaskExecutor;
    }


}
