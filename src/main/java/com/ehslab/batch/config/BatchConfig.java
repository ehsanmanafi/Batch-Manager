package com.ehslab.batch.config;

import com.ehslab.batch.entity.Inventory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job inventoryJob(JobRepository jobRepository, Step inventoryStep) {
        return new JobBuilder("inventoryJob", jobRepository)
                .start(inventoryStep)
                .build();
    }

    @Bean
    public Step inventoryStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              ItemReader<Inventory> reader, ItemProcessor<Inventory, Inventory> processor,
                              ItemWriter<Inventory> writer) {
        return new StepBuilder("inventoryStep", jobRepository)
                .<Inventory, Inventory>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Inventory> reader() {
        return new FlatFileItemReaderBuilder<Inventory>()
                .name("inventoryReader")
                .resource(new ClassPathResource("input/inventory.csv"))
                .delimited()
                .names("itemId", "itemName", "quantity", "unitPrice")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Inventory.class);
                }})
                .build();
    }


    @Bean
    public ItemProcessor<Inventory, Inventory> processor() {
        return item -> {
            item.setTotalPrice(item.getQuantity() * item.getUnitPrice());
            return item;
        };
    }


    @Bean
    public JdbcBatchItemWriter<Inventory> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Inventory>()
                .dataSource(dataSource)
                .sql("INSERT INTO inventory (item_id, item_name, quantity, unit_price, total_price) VALUES (:itemId, :itemName, :quantity, :unitPrice, :totalPrice)")
                .beanMapped()
                .build();
    }
}
