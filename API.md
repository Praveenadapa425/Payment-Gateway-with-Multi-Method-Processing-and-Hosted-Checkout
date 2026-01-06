# API Documentation

## Base URL
`http://localhost:8000`

## Authentication
All private API endpoints require authentication using the following headers:
- `X-Api-Key`: Merchant API key
- `X-Api-Secret`: Merchant API secret

Default test credentials:
- API Key: `key_test_abc123`
- API Secret: `secret_test_xyz789`

## Health Check

### GET /health
Check system health and database connectivity.

**Response:**
```json
{
  "status": "healthy",
  "database": "connected",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Orders

### POST /api/v1/orders
Create a new payment order.

**Headers:**
- `X-Api-Key`: API key
- `X-Api-Secret`: API secret
- `Content-Type`: application/json

**Request Body:**
```json
{
  "amount": 50000,
  "currency": "INR",
  "receipt": "receipt_123",
  "notes": {
    "customer_name": "John Doe"
  }
}
```

**Response (201 Created):**
```json
{
  "id": "order_NXhj67fGH2jk9mPq",
  "merchant_id": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 50000,
  "currency": "INR",
  "receipt": "receipt_123",
  "notes": {
    "customer_name": "John Doe"
  },
  "status": "created",
  "created_at": "2024-01-15T10:30:00Z"
}
```

**Error Response (400):**
```json
{
  "error": {
    "code": "BAD_REQUEST_ERROR",
    "description": "amount must be at least 100"
  }
}
```

**Error Response (401):**
```json
{
  "error": {
    "code": "AUTHENTICATION_ERROR",
    "description": "Invalid API credentials"
  }
}
```

### GET /api/v1/orders/{order_id}
Get order details.

**Headers:**
- `X-Api-Key`: API key
- `X-Api-Secret`: API secret

**Response (200):**
```json
{
  "id": "order_NXhj67fGH2jk9mPq",
  "merchant_id": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 50000,
  "currency": "INR",
  "receipt": "receipt_123",
  "notes": {},
  "status": "created",
  "created_at": "2024-01-15T10:30:00Z",
  "updated_at": "2024-01-15T10:30:00Z"
}
```

## Payments

### POST /api/v1/payments
Process a payment for an order.

**Headers:**
- `X-Api-Key`: API key
- `X-Api-Secret`: API secret
- `Content-Type`: application/json

**Request Body (UPI):**
```json
{
  "order_id": "order_NXhj67fGH2jk9mPq",
  "method": "upi",
  "vpa": "user@paytm"
}
```

**Request Body (Card):**
```json
{
  "order_id": "order_NXhj67fGH2jk9mPq",
  "method": "card",
  "card": {
    "number": "4111111111111111",
    "expiry_month": "12",
    "expiry_year": "2025",
    "cvv": "123",
    "holder_name": "John Doe"
  }
}
```

**Response (201):**
```json
{
  "id": "pay_H8sK3jD9s2L1pQr",
  "order_id": "order_NXhj67fGH2jk9mPq",
  "amount": 50000,
  "currency": "INR",
  "method": "upi",
  "vpa": "user@paytm",
  "status": "processing",
  "created_at": "2024-01-15T10:31:00Z"
}
```

### GET /api/v1/payments/{payment_id}
Get payment details.

**Headers:**
- `X-Api-Key`: API key
- `X-Api-Secret`: API secret

**Response (200):**
```json
{
  "id": "pay_H8sK3jD9s2L1pQr",
  "order_id": "order_NXhj67fGH2jk9mPq",
  "amount": 50000,
  "currency": "INR",
  "method": "upi",
  "vpa": "user@paytm",
  "status": "success",
  "created_at": "2024-01-15T10:31:00Z",
  "updated_at": "2024-01-15T10:31:10Z"
}
```

## Public Endpoints (for Checkout Page)

### GET /api/v1/orders/{order_id}/public
Get order details for checkout page (no authentication required).

**Response (200):**
```json
{
  "id": "order_NXhj67fGH2jk9mPq",
  "amount": 50000,
  "currency": "INR",
  "status": "created",
  "created_at": "2024-01-15T10:30:00Z"
}
```

### POST /api/v1/payments/public
Process payment from checkout page (no authentication required, validates order exists).

**Request Body:**
```json
{
  "order_id": "order_NXhj67fGH2jk9mPq",
  "method": "upi",
  "vpa": "user@paytm"
}
```

## Test Endpoint

### GET /api/v1/test/merchant
Get test merchant details for evaluation (no authentication required).

**Response (200):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "test@example.com",
  "api_key": "key_test_abc123",
  "seeded": true
}
```

## Error Codes

- `AUTHENTICATION_ERROR` - Invalid API credentials
- `BAD_REQUEST_ERROR` - Validation errors
- `NOT_FOUND_ERROR` - Resource not found
- `PAYMENT_FAILED` - Payment processing failed
- `INVALID_VPA` - VPA format invalid
- `INVALID_CARD` - Card validation failed
- `EXPIRED_CARD` - Card expiry date invalid