CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       phone VARCHAR(50) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       is_verified BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE services (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          base_price DECIMAL(15, 2) NOT NULL,
                          image_url VARCHAR(255),
                          is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE bookings (
                          id UUID PRIMARY KEY,
                          user_id UUID NOT NULL,
                          service_id UUID NOT NULL,
                          start_date DATE NOT NULL,
                          end_date DATE NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          total_price DECIMAL(15, 2) NOT NULL,
                          notes TEXT,
                          CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id),
                          CONSTRAINT fk_booking_service FOREIGN KEY (service_id) REFERENCES services(id)
);

CREATE TABLE payments (
                          id UUID PRIMARY KEY,
                          booking_id UUID NOT NULL UNIQUE,
                          proof_image_url VARCHAR(255) NOT NULL,
                          payment_status VARCHAR(50) NOT NULL,
                          paid_at TIMESTAMP,
                          CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

CREATE TABLE system_configs (
                                key VARCHAR(100) PRIMARY KEY,
                                value TEXT NOT NULL
);

CREATE TABLE admins (
                        id UUID PRIMARY KEY,
                        username VARCHAR(100) UNIQUE NOT NULL,
                        password_hash VARCHAR(255) NOT NULL,
                        role VARCHAR(50) NOT NULL
);