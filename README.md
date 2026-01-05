# Payment Gateway

A comprehensive payment gateway system similar to Razorpay or Stripe, built with merchant onboarding, payment order management, and multi-method payment processing.

## Features

- RESTful API with merchant authentication using API key and secret
- Support for UPI and Card payment methods
- Hosted checkout page for customer payments
- Dockerized deployment with single command setup
- Proper validation for payment methods (VPA format, Luhn algorithm, card network detection)
- Database persistence with PostgreSQL
- Dashboard for merchants to view transactions
- Automated test merchant seeding

## Architecture

- Backend API: Java Spring Boot application
- Database: PostgreSQL
- Dashboard: React frontend
- Checkout Page: React frontend

## Prerequisites

- Docker
- Docker Compose

## Setup Instructions

1. Clone the repository
2. Copy `.env.example` to `.env` and configure environment variables if needed
3. Run the application using Docker Compose:

```bash
docker-compose up -d
```

## Services

- API Server: http://localhost:8000
- Dashboard: http://localhost:3000
- Checkout Page: http://localhost:3001
- Database: PostgreSQL on port 5432

## Default Test Merchant

A test merchant is automatically created on startup with the following credentials:
- Email: test@example.com
- API Key: key_test_abc123
- API Secret: secret_test_xyz789

## API Endpoints

### Health Check
- `GET /health` - Check system health

### Orders
- `POST /api/v1/orders` - Create a new order
- `GET /api/v1/orders/{order_id}` - Get order details

### Payments
- `POST /api/v1/payments` - Process a payment
- `GET /api/v1/payments/{payment_id}` - Get payment details

### Test Endpoints
- `GET /api/v1/test/merchant` - Get test merchant details (for evaluation)

## Environment Variables

- `DATABASE_URL` - PostgreSQL connection string
- `PORT` - API server port
- `TEST_MERCHANT_EMAIL` - Email for test merchant
- `TEST_API_KEY` - API key for test merchant
- `TEST_API_SECRET` - API secret for test merchant
- `UPI_SUCCESS_RATE` - Success rate for UPI payments (0.90 = 90%)
- `CARD_SUCCESS_RATE` - Success rate for card payments (0.95 = 95%)
- `PROCESSING_DELAY_MIN` - Minimum processing delay in milliseconds
- `PROCESSING_DELAY_MAX` - Maximum processing delay in milliseconds
- `TEST_MODE` - Enable test mode for deterministic evaluation
- `TEST_PAYMENT_SUCCESS` - Force payment success/failure in test mode
- `TEST_PROCESSING_DELAY` - Fixed processing delay in test mode