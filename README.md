# Sprint3

The submission for Sprint 3 is contained within its own separate branch.
The purpose of this follow-up project is to show competency in the following areas:

## MySQL

Understand and Use a Relational Database in MySQL and MySQL Workbench

- SELECT, WHERE, and ORDER BY clauses
- Single Row Functions
- Aggregate Functions
- Joins
- Subqueries
- Data Modification Language (DML)
- Transaction Control Language (TCL)

## Node.js, Express, and MongoDB

Node.js, Express, and MongoDB used to create a RESTful API for a basic banking system featuring:

- User Account Management
  - Create account (name, email, initial balance) 
  - Get account details 
  - Update account info 
  - Delete account 
- Transactions 
  - Deposit money 
  - Withdraw money 
  - Transfer between accounts 
  - View transaction history 
- Security & Validation 
  - Input validation 
  - Basic authentication (optional) 

## Setup Instructions

### Prerequisites

Before running this project, ensure you have the following installed on your local machine:

- **Node.js** and **npm** ([Node.js](https://nodejs.org/))
- **MySQL** ([MySQL Workbench](https://www.mysql.com/products/workbench/) recommended)
- **MongoDB** ([MongoDB Atlas](https://www.mongodb.com/cloud/atlas) or local installation)
- **Postman** ([Postman](https://www.postman.com/)) or a similar tool for testing API endpoints

### Installation Steps

1. **Clone the repository**

   ```bash
   git clone https://git.fdmgroup.com/Duncan.Patel/bank-account-customer-mgmt.git
   cd bank-account-customer-mgmt
   ```

2. **Set up the database for SQL Stories**
   - The SQL script for tables and user stories can be found in the `sql-user-stories/` folder
   - Execute the SQL script in MySQL Workbench or your MySQL client

3. **Install dependencies**

   ```bash
   npm install
   ```

4. **Set up Environment Variables**
   Create a `.env` file in the root directory of the project and add the following:

   ```env
   PORT=3000
   MONGODB_URI=your_mongodb_connection_string
   JWT_SECRET_KEY=your_secret_key_here
   ```

   _**Note:** Replace the placeholders with your actual MongoDB connection URI and a unique string for `JWT_SECRET_KEY`. For local development, using a literal value like `JWT_SECRET_KEY` is allowed, as the system provides endpoints to register and log in to generate valid tokens for subsequent authenticated requests._

5. **Start the application**
   ```bash
   npm run dev
   ```
   This will start the Node.js Express server using nodemon for development.
   The API will be accessible at `http://localhost:3000`
