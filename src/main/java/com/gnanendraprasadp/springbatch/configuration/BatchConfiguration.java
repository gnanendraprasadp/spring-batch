package com.gnanendraprasadp.springbatch.configuration;

import com.gnanendraprasadp.springbatch.exception.BatchException;
import com.gnanendraprasadp.springbatch.exception.ErrorListener;
import com.gnanendraprasadp.springbatch.model.Customer;
import com.gnanendraprasadp.springbatch.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfiguration {
    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private CustomerRepository customerRepository;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("Job 1")
                .flow(Step())
                .end().build();
    }

    @Bean
    public Step Step() {
        return stepBuilderFactory.get("Step 1").<Customer, Customer>chunk(500)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                //.taskExecutor(taskExecutor())
                .faultTolerant()
                //.skipLimit(10)
                //.skip(NumberFormatException.class)
                //.noSkip(IllegalArgumentException.class)
                .listener(skipListener())//need to be before skipPolicy
                .skipPolicy(skipPolicy())
                .build();
    }

    // custom method for skipPolicy
    @Bean
    public SkipPolicy skipPolicy() {
        return new BatchException();
    }

    @Bean
    public SkipListener<Customer,Number> skipListener(){
        return new ErrorListener();
    }

    @Bean
    public FlatFileItemReader<Customer> itemReader() {
        FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        flatFileItemReader.setName("CSV Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob", "age");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

    @Bean
    public BatchItemProcessor itemProcessor() {
        return new BatchItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<Customer> itemWriter() {
        RepositoryItemWriter repositoryItemWriter = new RepositoryItemWriter();
        repositoryItemWriter.setRepository(customerRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }

    //As spring batch is synchronous we are using this to process data more quickly and unordered
    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
