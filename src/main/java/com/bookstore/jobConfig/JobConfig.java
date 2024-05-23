package com.bookstore.jobConfig;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import com.bookstore.bookRepository.BookRepository;
import com.bookstore.model.Book;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

	private final BookRepository bookRepository;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	 
	
	
	
	@Bean
	public Job job() {
		return new JobBuilder("job",jobRepository)
				.start(step())
				.build();
			
		
	}
	
	@Bean
	public Step step() {
		return new StepBuilder("setp",jobRepository)
				.<Book,Book>chunk(10,transactionManager)
				.reader(itemReder())
                .writer(itemWriter())
                .allowStartIfComplete(true) 

                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(200)
                .build();
                
		
		
	}

	@Bean
	public FlatFileItemReader<Book> itemReder() {
		return new FlatFileItemReaderBuilder<Book>().resource(new ClassPathResource("book.csv"))
				 .name("itemReder")
				.delimited()
			    .names("id", "name", "author", "pageCount")
				.targetType(Book.class)
				.build();
				

	}

	@Bean
	public RepositoryItemWriter<Book>  itemWriter() {
       
		return new RepositoryItemWriterBuilder<Book>()
				.methodName("save")
				.repository(bookRepository)
				.build();
		}

}
