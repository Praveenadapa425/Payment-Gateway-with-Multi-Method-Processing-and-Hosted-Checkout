# Payment Gateway Architecture

## System Components

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Dashboard     │    │   Checkout       │    │   Customer      │
│   (React)       │    │   (React)        │    │                 │
│                 │    │                  │    │                 │
│  - Login        │    │  - Order Summary │    │  - Selects      │
│  - API Keys     │    │  - Payment Form  │    │    payment      │
│  - Transactions │    │  - Status Polling│    │    method       │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                        │
         │                       │                        │
         └───────────────────────┼────────────────────────┘
                                 │
         ┌───────────────────────┼────────────────────────┐
         │                       ▼                        │
         │              ┌──────────────────┐              │
         │              │   API Server     │              │
         │              │  (Spring Boot)   │              │
         │              │                  │              │
         │              │  - Authentication│              │
         │              │  - Validation    │              │
         │              │  - Payment Logic │              │
         │              │  - Order Mgmt    │              │
         │              └──────────────────┘              │
         │                       │                        │
         │                       │                        │
         │              ┌──────────────────┐              │
         │              │   PostgreSQL     │              │
         │              │                  │              │
         │              │  - merchants     │              │
         │              │  - orders        │              │
         │              │  - payments      │              │
         │              └──────────────────┘              │
         └─────────────────────────────────────────────────┘
```

## Data Flow

1. Merchant creates order via API
2. Customer redirected to checkout page with order ID
3. Customer enters payment details
4. Payment processed with validation
5. Status updated in database
6. Checkout page polls for status
7. Result displayed to customer
8. Transaction visible in dashboard

## Services

- **Dashboard (Port 3000)**: Merchant interface for managing payments
- **Checkout (Port 3001)**: Customer payment interface
- **API (Port 8000)**: Business logic and data management
- **PostgreSQL (Port 5432)**: Data persistence

## Key Features

- API authentication with API key/secret
- UPI and Card payment processing
- VPA validation with regex
- Card validation with Luhn algorithm
- Card network detection
- Expiry date validation
- Payment status polling
- Test mode for deterministic evaluation
- Dockerized deployment