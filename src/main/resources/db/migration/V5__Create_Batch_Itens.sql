CREATE TABLE tb_message_Batch_itens(
id UUID PRIMARY KEY,
status VARCHAR NOT NULL,
processed_at TIMESTAMP WITH TIME ZONE NULL,
batch_id BIGSERIAL NOT NULL,

CONSTRAINT fk_batch FOREIGN KEY (batch_id) REFERENCES tb_message_batch (id)
);