# Database Access Guide - AWS Systems Manager

## Quick Access Steps

### 1. Open AWS CloudShell
- Go to AWS Console
- Click the CloudShell icon (terminal icon) in the top navigation
- Wait for it to initialize (~30 seconds)

### 2. Connect to EC2 Instance
```bash
aws ssm start-session --target i-0af51646408e104f3
```

### 3. Connect to PostgreSQL Database
```bash
psql -h lenaspa-db.c03ak824wyj7.us-east-1.rds.amazonaws.com -U postgres -d lenaspadb
```
- Enter your database password when prompted

## Useful PostgreSQL Commands

### Database Navigation
```sql
-- List all databases
SELECT datname FROM pg_database WHERE datistemplate = false;

-- Switch to different database
\c database_name

-- List all tables
\dt

-- Describe table structure
\d table_name

-- Exit PostgreSQL
\q
```

### Data Queries
```sql
-- View all users
SELECT * FROM users;

-- View all bookings
SELECT * FROM bookings;

-- Count total users
SELECT COUNT(*) FROM users;

-- Find admin users
SELECT username, email, role FROM users WHERE role = 'ROLE_ADMIN';

-- Recent bookings
SELECT * FROM bookings ORDER BY created_by DESC LIMIT 10;

-- Bookings by service
SELECT service, COUNT(*) FROM bookings GROUP BY service;
```

## Your Database Details
- **RDS Endpoint**: `lenaspa-db.c03ak824wyj7.us-east-1.rds.amazonaws.com`
- **EC2 Instance ID**: `i-0af51646408e104f3`
- **Database Name**: `lenaspadb`
- **Username**: `postgres`
- **Tables**: `users`, `bookings`

## Exit Commands
```bash
# Exit PostgreSQL
\q

# Exit EC2 session (back to CloudShell)
exit

# Close CloudShell
# Just close the browser tab
```

## Troubleshooting

### If connection fails:
1. Check EC2 instance is running
2. Verify IAM role has `AmazonSSMManagedInstanceCore` policy
3. Ensure security groups allow PostgreSQL port 5432

### If database connection fails:
1. Verify RDS endpoint is correct
2. Check database credentials
3. Ensure RDS security group allows connection from EC2

## Alternative: AWS Console Session Manager
1. Go to Systems Manager → Session Manager
2. Click "Start session"
3. Select instance `i-0af51646408e104f3`
4. Click "Start session"
5. Follow same PostgreSQL connection steps