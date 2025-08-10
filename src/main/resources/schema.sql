CREATE TABLE expenses (
    expense_id binary(16) not null primary key,
    user_id binary(16) not null,
    expense_amount VARCHAR(255),
    category VARCHAR(255),
    description VARCHAR(255),
    record_created_date_time TIMESTAMP,
    record_updated_date_time TIMESTAMP
);