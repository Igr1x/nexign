CREATE TABLE cdr (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    call_type VARCHAR(2) NOT NULL,
    calling BIGINT NOT NULL,
    receiving BIGINT NOT NULL,
    start_call TIMESTAMP NOT NULL,
    end_call TIMESTAMP NOT NULL,
    CONSTRAINT fk_calling FOREIGN KEY (calling) REFERENCES subscriber(id),
    CONSTRAINT fk_receiving FOREIGN KEY (receiving) REFERENCES subscriber(id)
);