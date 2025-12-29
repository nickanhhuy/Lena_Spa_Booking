# Lena Spa Backend

## Environment Setup

This application uses environment variables for sensitive configuration. 

### Local Development Setup

1. Copy the `.env.example` file to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Update the `.env` file with your actual credentials:
   - Database credentials
   - JWT secret key
   - Email credentials

3. **Important**: Never commit the `.env` file to Git. It's already in `.gitignore`.

### Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_HOSTNAME` | Database host | `localhost` |
| `DB_PORT` | Database port | `5432` |
| `DB_NAME` | Database name | `lenaspadb` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `your_password` |
| `JWT_SECRET` | Secret key for JWT tokens | `your_secret_key` |
| `JWT_EXPIRATION` | JWT token expiration (ms) | `3600000` |
| `MAIL_HOST` | SMTP server host | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP server port | `587` |
| `MAIL_USERNAME` | Email username | `your_email@gmail.com` |
| `MAIL_PASSWORD` | Email app password | `your_app_password` |
| `PORT` | Server port | `5000` |

### Running the Application

```bash
./mvnw spring-boot:run
```

The application will automatically load environment variables from the `.env` file.

### AWS Deployment

For AWS deployment, set environment variables in your EC2 instance or use AWS Systems Manager Parameter Store.
