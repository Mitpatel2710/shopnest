# ShopNest — Database Schema

## Tables

### users
| Column     | Type          | Constraints        |
|------------|---------------|--------------------|
| id         | BIGINT        | PK, AUTO_INCREMENT |
| first_name | VARCHAR(50)   | NOT NULL           |
| last_name  | VARCHAR(50)   |                    |
| email      | VARCHAR(100)  | NOT NULL, UNIQUE   |
| password   | VARCHAR(255)  | NOT NULL           |
| phone      | VARCHAR(15)   |                    |
| role       | VARCHAR(20)   | NOT NULL           |
| active     | BOOLEAN       | DEFAULT true       |
| street     | VARCHAR(255)  |                    |
| city       | VARCHAR(100)  |                    |
| state      | VARCHAR(100)  |                    |
| pincode    | VARCHAR(10)   |                    |
| created_at | TIMESTAMP     | NOT NULL           |
| updated_at | TIMESTAMP     | NOT NULL           |

### categories
| Column      | Type          | Constraints        |
|-------------|---------------|--------------------|
| id          | BIGINT        | PK, AUTO_INCREMENT |
| name        | VARCHAR(100)  | NOT NULL, UNIQUE   |
| slug        | VARCHAR(100)  | NOT NULL, UNIQUE   |
| description | TEXT          |                    |
| parent_id   | BIGINT        | FK → categories.id |
| image_url   | VARCHAR(500)  |                    |
| active      | BOOLEAN       | DEFAULT true       |
| created_at  | TIMESTAMP     | NOT NULL           |

### products
| Column      | Type          | Constraints           |
|-------------|---------------|-----------------------|
| id          | BIGINT        | PK, AUTO_INCREMENT    |
| name        | VARCHAR(200)  | NOT NULL              |
| description | TEXT          |                       |
| price       | DECIMAL(10,2) | NOT NULL              |
| stock_qty   | INT           | NOT NULL, DEFAULT 0   |
| image_url   | VARCHAR(500)  |                       |
| active      | BOOLEAN       | DEFAULT true          |
| type        | VARCHAR(50)   |                       |
| brand       | VARCHAR(100)  |                       |
| category_id | BIGINT        | FK → categories.id    |
| seller_id   | BIGINT        | FK → users.id         |
| created_at  | TIMESTAMP     | NOT NULL              |
| updated_at  | TIMESTAMP     | NOT NULL              |

### carts
| Column     | Type      | Constraints        |
|------------|-----------|--------------------|
| id         | BIGINT    | PK, AUTO_INCREMENT |
| user_id    | BIGINT    | FK → users.id, UNIQUE |
| created_at | TIMESTAMP | NOT NULL           |
| updated_at | TIMESTAMP | NOT NULL           |

### cart_items
| Column     | Type      | Constraints                          |
|------------|-----------|--------------------------------------|
| id         | BIGINT    | PK, AUTO_INCREMENT                   |
| cart_id    | BIGINT    | FK → carts.id                        |
| product_id | BIGINT    | FK → products.id                     |
| quantity   | INT       | NOT NULL                             |
| added_at   | TIMESTAMP | NOT NULL                             |
| UNIQUE (cart_id, product_id)                                  |

### orders
| Column           | Type          | Constraints        |
|------------------|---------------|--------------------|
| id               | BIGINT        | PK, AUTO_INCREMENT |
| user_id          | BIGINT        | FK → users.id      |
| status           | VARCHAR(20)   | NOT NULL           |
| total_amount     | DECIMAL(10,2) | NOT NULL           |
| delivery_street  | VARCHAR(255)  | NOT NULL           |
| delivery_city    | VARCHAR(100)  | NOT NULL           |
| delivery_state   | VARCHAR(100)  | NOT NULL           |
| delivery_pincode | VARCHAR(10)   | NOT NULL           |
| payment_method   | VARCHAR(30)   |                    |
| payment_status   | VARCHAR(20)   |                    |
| placed_at        | TIMESTAMP     | NOT NULL           |
| updated_at       | TIMESTAMP     | NOT NULL           |

### order_items
| Column            | Type          | Constraints        |
|-------------------|---------------|--------------------|
| id                | BIGINT        | PK, AUTO_INCREMENT |
| order_id          | BIGINT        | FK → orders.id     |
| product_id        | BIGINT        | FK → products.id   |
| product_name      | VARCHAR(200)  | NOT NULL           |
| price_at_purchase | DECIMAL(10,2) | NOT NULL           |
| quantity          | INT           | NOT NULL           |

### payments
| Column           | Type          | Constraints           |
|------------------|---------------|-----------------------|
| id               | BIGINT        | PK, AUTO_INCREMENT    |
| order_id         | BIGINT        | FK → orders.id UNIQUE |
| transaction_id   | VARCHAR(100)  | UNIQUE                |
| amount           | DECIMAL(10,2) | NOT NULL              |
| method           | VARCHAR(30)   | NOT NULL              |
| status           | VARCHAR(20)   | NOT NULL              |
| gateway_response | TEXT          |                       |
| paid_at          | TIMESTAMP     |                       |
| created_at       | TIMESTAMP     | NOT NULL              |

### reviews
| Column     | Type         | Constraints                     |
|------------|--------------|---------------------------------|
| id         | BIGINT       | PK, AUTO_INCREMENT              |
| user_id    | BIGINT       | FK → users.id                   |
| product_id | BIGINT       | FK → products.id                |
| rating     | INT          | NOT NULL, CHECK (1-5)           |
| title      | VARCHAR(200) |                                 |
| comment    | TEXT         |                                 |
| verified   | BOOLEAN      | DEFAULT false                   |
| created_at | TIMESTAMP    | NOT NULL                        |
| UNIQUE (user_id, product_id)                                 |

## Relationships
- users → carts         : OneToOne
- users → orders        : OneToMany
- users → reviews       : OneToMany
- categories → products : OneToMany
- categories → categories : Self-referential (parent_id)
- products → cart_items : OneToMany
- products → order_items: OneToMany
- products → reviews    : OneToMany
- carts → cart_items    : OneToMany
- orders → order_items  : OneToMany
- orders → payments     : OneToOne

## Design Decisions
- Address embedded in users and orders — snapshot pattern
- price_at_purchase in order_items — snapshot pattern
- payments separate from orders — single responsibility
- cart separate from users — independent lifecycle