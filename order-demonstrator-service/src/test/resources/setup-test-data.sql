-- Customers
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('first-name-1', 'last-name-1', 'email1@test.com', '+12345678910');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('first-name-2', 'last-name-2', 'email2@test.com', '+22345678910');

-- Products
INSERT INTO products (name, article, unit) VALUES ('name-1', '111article111', 'UNIT');
INSERT INTO products (name, article, unit) VALUES ('name-2', '222article222', 'KG');
INSERT INTO products (name, article, unit) VALUES ('name-3', '333article333', 'M');

-- Orders
INSERT INTO orders (customer_id, payment_method, order_status, created_at) VALUES (1, 'PAYPAL', 'PAID', TO_TIMESTAMP('2025-03-27 12:15:12', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES (1, 3, 2.3, 5.67);
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES (1, 1, 5.1, 3.12);

INSERT INTO orders (customer_id, payment_method, order_status, created_at) VALUES (1, 'CREDIT_CARD', 'PAID', TO_TIMESTAMP('2025-03-28 15:30:56', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES (2, 1, 6.3, 5.0);
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES (2, 2, 1.2, 3.10);
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES (2, 3, 6.0, 2.25);

INSERT INTO orders (customer_id, payment_method, order_status, created_at) VALUES (2, 'PAYPAL', 'CANCELLED', TO_TIMESTAMP('2025-03-29 13:45:55', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES (3, 1, 0.3, 1.45);
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES (3, 2, 7.6, 8.67);
