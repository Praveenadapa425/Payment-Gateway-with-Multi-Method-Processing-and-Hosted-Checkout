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
- Comprehensive payment status flow (processing → success/failed)
- Secure card data handling (never stores full card numbers or CVV)
- Real-time payment polling mechanism
- Professional UI with responsive design
- Indian Rupee (₹) currency formatting

## Architecture

- Backend API: Java Spring Boot application
- Database: PostgreSQL
- Dashboard: React frontend
- Checkout Page: React frontend
- Reverse Proxy: Nginx for static asset serving

## Prerequisites

- Docker (version 20.10 or higher)
- Docker Compose (version 2.0 or higher)
- Git

## Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd payment-gateway
   ```

2. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```

3. (Optional) Modify environment variables in `.env` if needed

## Configuration

### Environment Variables

The following environment variables can be configured in `.env`:

**Backend API:**
- `PORT` - API server port (default: 8000)
- `DATABASE_URL` - PostgreSQL connection string
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password

**Test Merchant:**
- `TEST_MERCHANT_EMAIL` - Email for test merchant (default: test@example.com)
- `TEST_API_KEY` - API key for test merchant (default: key_test_abc123)
- `TEST_API_SECRET` - API secret for test merchant (default: secret_test_xyz789)

**Payment Processing:**
- `TEST_MODE` - Enable test mode for deterministic evaluation (default: false)
- `TEST_PAYMENT_SUCCESS` - Force payment success/failure in test mode (default: true)
- `TEST_PROCESSING_DELAY` - Fixed processing delay in test mode (default: 1000ms)
- `UPI_SUCCESS_RATE` - Success rate for UPI payments (default: 0.90)
- `CARD_SUCCESS_RATE` - Success rate for card payments (default: 0.95)
- `PROCESSING_DELAY_MIN` - Minimum processing delay in milliseconds (default: 5000)
- `PROCESSING_DELAY_MAX` - Maximum processing delay in milliseconds (default: 10000)

## Running the Application

Start all services with Docker Compose:

```bash
docker-compose up -d
```

The application will be available at:
- API Server: http://localhost:8000
- Dashboard: http://localhost:3000
- Checkout Page: http://localhost:3001
- Database: PostgreSQL on port 5432

## Services

The system consists of four main services:

1. **PostgreSQL Database** - Persistent storage for merchants, orders, and payments
2. **API Server** - Java Spring Boot backend handling all business logic
3. **Dashboard** - React frontend for merchant administration
4. **Checkout Page** - React frontend for customer payments

### Service Dependencies

- PostgreSQL starts first with health checks
- API server starts after PostgreSQL is healthy
- Dashboard and checkout pages start after API server is available

## Default Test Merchant

A test merchant is automatically created on startup with the following credentials:
- Email: test@example.com
- API Key: key_test_abc123
- API Secret: secret_test_xyz789

These credentials can be used to authenticate API requests and access the dashboard.

## API Documentation

### Health Check
- `GET /health` - Returns system health status
  Response: `{"status": "healthy", "database": "connected", "timestamp": "2023-01-01T00:00:00"}`

### Orders
- `POST /api/v1/orders` - Create a new order (Requires authentication)
  Headers: `X-Api-Key`, `X-Api-Secret`
  Request: `{"amount": 50000, "currency": "INR", "receipt": "order_rcpt_123", "notes": {}}`
  Response: `{"id": "order_xxx", "amount": 50000, "status": "created", ...}`
  Status: 201 Created

- `GET /api/v1/orders` - Get all orders for authenticated merchant
  Headers: `X-Api-Key`, `X-Api-Secret`
  Response: `[...]`
  Status: 200 OK

- `GET /api/v1/orders/{orderId}` - Get specific order details
  Headers: `X-Api-Key`, `X-Api-Secret`
  Status: 200 OK

### Payments
- `POST /api/v1/payments` - Process a payment (Requires authentication)
  Headers: `X-Api-Key`, `X-Api-Secret`
  Request: `{"order_id": "order_xxx", "method": "upi", "vpa": "user@bank"}` or `{"order_id": "order_xxx", "method": "card", "card": {...}}`
  Response: `{"id": "pay_xxx", "order_id": "order_xxx", "status": "processing", ...}`
  Status: 201 Created

- `GET /api/v1/payments` - Get all payments for authenticated merchant
  Headers: `X-Api-Key`, `X-Api-Secret`
  Status: 200 OK

- `GET /api/v1/payments/{paymentId}` - Get specific payment details
  Headers: `X-Api-Key`, `X-Api-Secret`
  Status: 200 OK

### Public Endpoints (No Authentication Required)
- `GET /api/v1/orders/{orderId}/public` - Get order details for checkout
  Response: `{"id": "order_xxx", "amount": 50000, "status": "created"}`

- `POST /api/v1/payments/public` - Process payment from checkout page
  Request: `{"order_id": "order_xxx", "method": "upi", "vpa": "user@bank"}`
  Status: 201 Created

- `GET /api/v1/payments/{paymentId}/public` - Get payment status for checkout
  Response: `{"id": "pay_xxx", "status": "success", ...}`

## Database Schema

The application uses PostgreSQL with the following tables:

### merchants
- `id` - UUID primary key
- `name` - Merchant name
- `email` - Unique email address
- `api_key` - Unique API key
- `api_secret` - API secret
- `webhook_url` - Webhook callback URL
- `is_active` - Active status
- `created_at`, `updated_at` - Timestamps

### orders
- `id` - VARCHAR(64) primary key (format: order_xxxxxxxxxxxxxxxx)
- `merchant_id` - Foreign key to merchants
- `amount` - Amount in paise (smallest currency unit)
- `currency` - Currency code (default: INR)
- `receipt` - Receipt identifier
- `notes` - JSONB field for additional data
- `status` - Order status (default: created)
- `created_at`, `updated_at` - Timestamps

### payments
- `id` - VARCHAR(64) primary key (format: pay_xxxxxxxxxxxxxxxx)
- `order_id` - Foreign key to orders
- `merchant_id` - Foreign key to merchants
- `amount` - Amount in paise
- `currency` - Currency code (default: INR)
- `method` - Payment method (upi, card)
- `status` - Payment status (processing, success, failed)
- `vpa` - Virtual Payment Address for UPI
- `card_network` - Detected card network
- `card_last4` - Last 4 digits of card
- `error_code`, `error_description` - Error details for failed payments
- `created_at`, `updated_at` - Timestamps

## Payment Processing Flow

1. **Order Creation**: Merchant creates an order via API with amount and other details
2. **Checkout Redirection**: Customer is redirected to checkout page with order_id
3. **Public Order Retrieval**: Checkout page fetches order details via public API
4. **Payment Processing**: Customer enters payment details and submits
5. **Payment Validation**: System validates payment details (VPA format, Luhn algorithm)
6. **Status Transition**: Payment starts with 'processing' status
7. **Bank Simulation**: System simulates bank processing with 5-10 second delay
8. **Final Status**: Payment transitions to 'success' or 'failed'
9. **Status Polling**: Checkout page polls for status updates every 2 seconds
10. **Result Display**: Final success or failure is shown to customer

## Security Measures

- API authentication with X-Api-Key and X-Api-Secret headers
- No storage of full card numbers or CVV codes
- Card validation using Luhn algorithm
- VPA format validation
- Input sanitization and validation
- SQL injection prevention through JPA repositories
- Rate limiting considerations (can be added with Spring Cloud Gateway)

## Frontend Applications

### Dashboard (Port 3000)
- Professional dashboard with merchant API credentials
- Real-time transaction statistics (total transactions, amount, success rate)
- Transaction history table with search and filter capabilities
- Clean, responsive design with modern UI elements
- Data-test-id attributes for automated testing
- Indian Rupee (₹) currency formatting

### Checkout Page (Port 3001)
- Secure payment processing interface
- Support for UPI and card payments
- Real-time payment status updates
- Processing state with animated indicators
- Success and failure states with clear messaging
- Form validation for payment details
- Mobile-responsive design
- Data-test-id attributes for automated testing

## Validation Logic

The system includes comprehensive validation:

- **VPA Validation**: Uses regex pattern `^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+$` for UPI IDs
- **Luhn Algorithm**: Validates card numbers using the standard algorithm
- **Card Network Detection**: Identifies Visa, Mastercard, Amex, and RuPay
- **Card Expiry Validation**: Checks month/year format and compares with current date
- **Amount Validation**: Ensures minimum amount of 100 paise (₹1)
- **Order Ownership**: Verifies orders belong to authenticated merchants

## Test Mode

The application supports test mode for deterministic evaluation:

- `TEST_MODE=true` enables deterministic payment outcomes
- `TEST_PAYMENT_SUCCESS=true/false` forces payment success/failure
- `TEST_PROCESSING_DELAY` sets fixed processing delay for predictable testing

## Error Handling

Standardized error codes are used throughout the API:

- `AUTHENTICATION_ERROR` - Invalid API credentials
- `BAD_REQUEST_ERROR` - Validation errors
- `NOT_FOUND_ERROR` - Resource not found
- `PAYMENT_FAILED` - Payment processing failed
- `INVALID_VPA` - VPA format invalid
- `INVALID_CARD` - Card validation failed

HTTP status codes follow REST conventions:
- 201 - Resource created (orders, payments)
- 200 - Successful GET requests
- 400 - Validation errors
- 401 - Authentication failures
- 404 - Resource not found

## Deployment

The application is fully containerized and production-ready:

1. Build all services with `docker-compose build`
2. Start with `docker-compose up -d`
3. All services are automatically configured and interconnected
4. Database schema is initialized automatically
5. Test merchant is created automatically
6. Health checks ensure service readiness

## Troubleshooting

### Common Issues

1. **Services not starting**: Ensure Docker and Docker Compose are installed and running
2. **Port conflicts**: Check if ports 8000, 3000, 3001, or 5432 are in use
3. **Database connection errors**: Verify PostgreSQL service is running and healthy
4. **API authentication failures**: Use the default test merchant credentials
5. **Checkout page showing 'No order ID provided'**: Ensure order_id parameter is passed in URL

### Debugging Tips

1. Check Docker logs: `docker-compose logs -f`
2. Verify service health: `docker-compose ps`
3. Access individual containers: `docker-compose exec <service> bash`
4. Test API endpoints directly: `curl http://localhost:8000/health`

### Environment-specific Issues

- On Windows: Use Git Bash or PowerShell for Docker commands
- On Linux: May need to run Docker with sudo
- Ensure sufficient memory allocation for Docker (at least 4GB)

## Development

### Local Development

For local development without Docker:

1. **Backend**: Run `mvn spring-boot:run` in the backend directory
2. **Frontend**: Run `npm start` in the frontend directory
3. **Checkout**: Run `npm start` in the checkout-page directory

### Building Production Images

Build all services:
```bash
docker-compose build
```

Rebuild without cache if needed:
```bash
docker-compose build --no-cache
```

### Stopping Services

Stop all services:
```bash
docker-compose down
```

Stop and remove volumes:
```bash
docker-compose down -v
```

## Monitoring and Maintenance

### Health Checks

The system includes health endpoints for monitoring:
- `/health` - Overall system health
- Docker health checks monitor PostgreSQL readiness

### Performance Considerations

- Connection pooling is configured for database access
- Static assets are served through Nginx for optimal performance
- Caching strategies can be added as needed

### Security Updates

- Keep Docker base images updated
- Monitor for security vulnerabilities in dependencies
- Regular security audits of authentication mechanisms

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.