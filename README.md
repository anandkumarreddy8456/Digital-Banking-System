

-- Customer Service End Points --

| Method | Endpoint                         | Description           |             
| ------ | -------------------------------- | --------------------- |
| POST   | `/api/customers/register`        | Register new customer |
| POST   | `/api/customers/login`           | Customer login        |
| GET    | `/api/customers/{customerId}`    | Get customer details  |
| PUT    | `/api/customers/{customerId}`    | Update profile        |
| DELETE | `/api/customers/{customerId}`    | Delete customer       |
| GET    | `/api/customers/all`             | List all customers    |
| GET    | `/api/customers/email/{email}`   | Get by email          |
| PUT    | `/api/customers/change-password` | Change password       |
 sample JSON
{
"name": "Ravi Kumar",
"email": "ravi@gmail.com",
"mobile": "9876543210",
"password": "pass123",
"address": "Bangalore"
}

-- Account Service -- 

| Method | Endpoint                              | Description                  |
| ------ | ------------------------------------- | ---------------------------- |
| POST   | `/api/accounts/create`                | Create bank account          |
| GET    | `/api/accounts/{accountNo}`           | Get account details          |
| GET    | `/api/accounts/customer/{customerId}` | Get all accounts of customer |
| PUT    | `/api/accounts/deposit`               | Deposit money                |
| PUT    | `/api/accounts/withdraw`              | Withdraw money               |
| GET    | `/api/accounts/balance/{accountNo}`   | Check balance                |
| PUT    | `/api/accounts/block/{accountNo}`     | Block account                |
| PUT    | `/api/accounts/unblock/{accountNo}`   | Unblock account              |

{
"customerId": 101,
"accountType": "SAVINGS",
"initialDeposit": 5000
}



-- Transaction Service ---

| Method | Endpoint                                  | Description                 |
| ------ | ----------------------------------------- | --------------------------- |
| POST   | `/api/transactions/transfer`              | Transfer funds              |
| GET    | `/api/transactions/{txnId}`               | Get transaction details     |
| GET    | `/api/transactions/account/{accountNo}`   | Get transactions by account |
| GET    | `/api/transactions/customer/{customerId}` | Transactions by customer    |
| GET    | `/api/transactions/date-range`            | Filter by date              |
| POST   | `/api/transactions/reverse/{txnId}`       | Reverse transaction         |
| GET    | `/api/transactions/statement/{accountNo}` | Mini statement              |

{
"fromAccount": "10001",
"toAccount": "10002",
"amount": 2000
}


-- Loan Service -- 

| Method | Endpoint                           | Description       |
| ------ | ---------------------------------- | ----------------- |
| POST   | `/api/loans/apply`                 | Apply for loan    |
| GET    | `/api/loans/{loanId}`              | Get loan details  |
| GET    | `/api/loans/customer/{customerId}` | Loans by customer |
| PUT    | `/api/loans/approve/{loanId}`      | Approve loan      |
| PUT    | `/api/loans/reject/{loanId}`       | Reject loan       |
| GET    | `/api/loans/status/{loanId}`       | Check loan status |
| GET    | `/api/loans/calculate-emi`         | EMI calculator    |
| PUT    | `/api/loans/pay-emi`               | Pay EMI           |


{
"customerId": 101,
"amount": 500000,
"tenure": 24,
"loanType": "PERSONAL"
}



-- Notification Service -- 

| Method | Endpoint                          | Description         |
| ------ | --------------------------------- | ------------------- |
| POST   | `/api/notifications/email`        | Send email          |
| POST   | `/api/notifications/sms`          | Send SMS            |
| GET    | `/api/notifications/{customerId}` | Get notifications   |
| GET    | `/api/notifications/all`          | All notifications   |
| DELETE | `/api/notifications/{id}`         | Delete notification |
Used For Events Like:
Money debited
Money credited
Loan approved
Loan rejected


-- Auth Service -- 

| Method | Endpoint                    | Purpose            |
| ------ | --------------------------- | ------------------ |
| POST   | `/api/auth/register`        | Register user      |
| POST   | `/api/auth/login`           | Generate JWT token |
| POST   | `/api/auth/validate`        | Validate token     |
| POST   | `/api/auth/refresh-token`   | Refresh JWT        |
| POST   | `/api/auth/logout`          | Invalidate token   |
| GET    | `/api/auth/user/{id}`       | Get user details   |
| PUT    | `/api/auth/change-password` | Change password    |

{
"username": "ravi",
"password": "pass123"
}
