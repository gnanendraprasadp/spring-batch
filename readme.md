## Item Readers
1. AbstractItemCountingItemStreamItemReader
2. AggregateItemReader
3. AmqpItemReader
4. KafkaItemReader
5. FlatFileItemReader
6. HibernateCursorItemReader
7. HibernatePagingItemReader
8. ItemReaderAdapter
9. JdbcCursorItemReader
10. JdbcPagingItemReader
11. JmsItemReader
12. JpaPagingItemReader
13. ListItemReader
14. MongoItemReader
15. Neo4jItemReader
16. RepositoryItemReader
17. StoredProcedureItemReader
18. StaxEventItemReader
19. JsonItemReader

## Item Writers
1. AbstractItemStreamItemWriter
2. AmqpItemWriter
3. CompositeItemWriter
4. FlatFileItemWriter
5. HibernateItemWriter
6. ItemWriterAdapter
7. JdbcBatchItemWriter
8. JmsItemWriter
9. JpaItemWriter
10. KafkaItemWriter
11. MimeMessageItemWriter
12. MongoItemWriter
13. Neo4jItemWriter
14. PropertyExtractingDelegatingItemWriter
15. RepositoryItemWriter
16. StaxEventItemWriter
17. JsonFileItemWriter

Spring batch creates 6 Tables. They are:
#### 1. BATCH_JOB_INSTANCE Table
The BATCH_JOB_INSTANCE table holds all information relevant to a JobInstance and serves as the top of the overall hierarchy.

`CREATE TABLE BATCH_JOB_INSTANCE  (
JOB_INSTANCE_ID BIGINT  PRIMARY KEY ,
VERSION BIGINT,
JOB_NAME VARCHAR(100) NOT NULL ,
JOB_KEY VARCHAR(32) NOT NULL
);`

#### 2. BATCH_JOB_EXECUTION_PARAMS Table
The BATCH_JOB_EXECUTION_PARAMS table holds all information relevant to the JobParameters object. It contains 0 or more key/value pairs passed to a Job and serves as a record of the parameters with which a job was run. For each parameter that contributes to the generation of a job’s identity, the IDENTIFYING flag is set to true.

`CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
JOB_EXECUTION_ID BIGINT NOT NULL ,
PARAMETER_NAME VARCHAR(100) NOT NULL ,
PARAMETER_TYPE VARCHAR(100) NOT NULL ,
PARAMETER_VALUE VARCHAR(2500) ,
IDENTIFYING CHAR(1) NOT NULL ,
constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);`

#### 3. BATCH_JOB_EXECUTION Table
The BATCH_JOB_EXECUTION table holds all information relevant to the JobExecution object. Every time a Job is run, there is always a new called JobExecution and a new row in this table.

`CREATE TABLE BATCH_JOB_EXECUTION  (
JOB_EXECUTION_ID BIGINT  PRIMARY KEY ,
VERSION BIGINT,
JOB_INSTANCE_ID BIGINT NOT NULL,
CREATE_TIME TIMESTAMP NOT NULL,
START_TIME TIMESTAMP DEFAULT NULL,
END_TIME TIMESTAMP DEFAULT NULL,
STATUS VARCHAR(10),
EXIT_CODE VARCHAR(20),
EXIT_MESSAGE VARCHAR(2500),
LAST_UPDATED TIMESTAMP,
constraint JOB_INSTANCE_EXECUTION_FK foreign key (JOB_INSTANCE_ID)
references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;`

#### 4. BATCH_STEP_EXECUTION Table
The BATCH_STEP_EXECUTION table holds all information relevant to the StepExecution object. This table is similar in many ways to the BATCH_JOB_EXECUTION table, and there is always at least one entry per Step for each JobExecution created.

`CREATE TABLE BATCH_STEP_EXECUTION  (
STEP_EXECUTION_ID BIGINT  PRIMARY KEY ,
VERSION BIGINT NOT NULL,
STEP_NAME VARCHAR(100) NOT NULL,
JOB_EXECUTION_ID BIGINT NOT NULL,
START_TIME TIMESTAMP NOT NULL ,
END_TIME TIMESTAMP DEFAULT NULL,
STATUS VARCHAR(10),
COMMIT_COUNT BIGINT ,
READ_COUNT BIGINT ,
FILTER_COUNT BIGINT ,
WRITE_COUNT BIGINT ,
READ_SKIP_COUNT BIGINT ,
WRITE_SKIP_COUNT BIGINT ,
PROCESS_SKIP_COUNT BIGINT ,
ROLLBACK_COUNT BIGINT ,
EXIT_CODE VARCHAR(20) ,
EXIT_MESSAGE VARCHAR(2500) ,
LAST_UPDATED TIMESTAMP,
constraint JOB_EXECUTION_STEP_FK foreign key (JOB_EXECUTION_ID)
references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;`

#### 5. BATCH_JOB_EXECUTION_CONTEXT Table
The BATCH_JOB_EXECUTION_CONTEXT table holds all information relevant to the ExecutionContext of a Job. There is exactly one Job ExecutionContext for each JobExecution, and it contains all the job-level data that is needed for a particular job execution. This data typically represents the state that must be retrieved after a failure, so that a JobInstance can “start where it left off”.

`CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
JOB_EXECUTION_ID BIGINT PRIMARY KEY,
SHORT_CONTEXT VARCHAR(2500) NOT NULL,
SERIALIZED_CONTEXT CLOB,
constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;`

#### 6. BATCH_STEP_EXECUTION_CONTEXT Table
The BATCH_STEP_EXECUTION_CONTEXT table holds all information relevant to the ExecutionContext of a Step. There is exactly one ExecutionContext per StepExecution, and it contains all the data that needs to be persisted for a particular step execution. This data typically represents the state that must be retrieved after a failure so that a JobInstance can “start where it left off”.

`CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
STEP_EXECUTION_ID BIGINT PRIMARY KEY,
SHORT_CONTEXT VARCHAR(2500) NOT NULL,
SERIALIZED_CONTEXT CLOB,
constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;`