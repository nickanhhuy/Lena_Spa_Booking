# Lena Beauty Spa Management System

A comprehensive spa appointment booking and management platform built with Java Spring Boot and Angular, featuring customer scheduling, service management, and administrative operations. The system implements clean architecture principles with JWT authentication and AWS cloud deployment.

## Demo

[![Application Demo](https://img.youtube.com/vi/2Y7xkMZs48A/0.jpg)](https://youtu.be/2Y7xkMZs48A)

*Click to view the complete application demonstration*

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Java Spring Boot, Spring Security, Spring Data JPA, PostgreSQL |
| **Frontend** | Angular 18, TypeScript, Angular Material, RxJS |
| **Authentication** | JWT Bearer Tokens |
| **Email** | Resend API |
| **Infrastructure** | AWS (EC2, RDS, S3, CloudFront, Route 53, Load Balancer) |
| **Monitoring** | AWS CloudWatch |

## System Architecture

![AWS Cloud Architecture](LenaSpa_AWS.architecture.jpg)

The application follows a three-tier architecture pattern deployed on Amazon Web Services, ensuring scalability, reliability, and security.

## Features

### Core Spa Management

- **Appointment Booking**: Create, view, and manage spa appointments with conflict detection
- **Service Management**: 
  - Facial treatments
  - Body massages
  - Nail services
  - Hair styling
- **Customer Management**: Full customer profile and booking history
- **Time Slot Management**: Dynamic availability with automated scheduling
- **Email Notifications**: Automated booking confirmations via Resend API

### Customer Features

- **Account Management**: User registration and profile management
- **Appointment Booking**: Browse services and book appointments
- **Booking History**: View past and upcoming appointments
- **Email Confirmations**: Receive booking confirmations and reminders
- **Multi-language Support**: English and Vietnamese language options
- **Responsive Design**: Seamless experience across desktop and mobile devices

### Admin Portal

- **Dashboard**: Overview of bookings, services, and business metrics
- **User Management**: Manage customer accounts and role assignments
- **Booking Management**: View, modify, and cancel appointments
- **Service Configuration**: Add, edit, and remove spa services
- **Time Slot Configuration**: Set available appointment times
- **Analytics**: Track booking trends and business performance

### Security Features

| Category | Implementation |
|----------|---------------|
| **Authentication** | JWT Bearer Tokens with secure token validation |
| **Authorization** | Role-Based Access Control (RBAC) - Admin and User roles |
| **Password Security** | BCrypt hashing with secure password policies |
| **Transport Security** | HTTPS/TLS via AWS CloudFront and Load Balancer |
| **API Security** | CORS policy, SQL injection prevention via Spring Data JPA |

### Permission Policies

- **ROLE_ADMIN** - Full system access and administrative operations
- **ROLE_USER** - Customer booking and profile management

## Technical Features

- Responsive web interface with Angular Material
- RESTful API architecture
- Environment-based configuration
- Automated email notifications via Resend
- JWT-based stateless authentication
- Role-based route protection
- Multi-language support (English/Vietnamese)
- Real-time booking conflict detection
- Automated time slot management

## Architecture

The application follows a clean architecture pattern with clear separation of concerns:

### Clean Architecture Layers

- **Presentation**: Angular SPA (Components, Services, Guards, Interceptors)
- **Application**: Business logic, services, use cases, DTOs
- **Domain**: Core entities, value objects, business rules
- **Infrastructure**: Spring Boot, PostgreSQL, Resend, AWS integrations

## Configuration

### Environment Variables

Create a `.env` file in the `backend_Lena/backend_Lena` directory with the following variables:

```bash
# JWT Configuration
JWT_SECRET_KEY=your-secret-key-here

# Resend Email Configuration
RESEND_API_KEY=your-resend-api-key
RESEND_FROM_EMAIL=noreply@yourdomain.com
RESEND_FROM_NAME=Lena Spa

# Application Settings
APP_BASE_URL=https://lenaspabooking.site
```

### Database Connection

The application uses PostgreSQL. Configure in `application.properties`:

**Development:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lena_spa
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

**Production:**
```properties
spring.datasource.url=jdbc:postgresql://your-rds-endpoint:5432/lena_spa
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
```

### AWS CloudWatch Monitoring

The application includes AWS CloudWatch integration for performance monitoring and logging.

**Setup:**
1. Configure AWS credentials with CloudWatch access
2. Application logs are automatically sent to CloudWatch Logs
3. Custom metrics track booking operations and API performance

**Monitored Metrics:**
- API response times
- Booking creation success/failure rates
- User authentication attempts
- Email notification delivery status

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL 14+
- Maven 3.8+
- Node.js 18+ (for frontend development)
- AWS Account (for deployment)

### Quick Start

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/lena-spa-booking
cd lena-spa-booking
```

**2. Configure environment variables**

Create a `.env` file in `backend_Lena/backend_Lena` directory (see Configuration → Environment Variables).

**3. Setup Database**
```bash
# Create PostgreSQL database
createdb lena_spa

# Update application.properties with your database credentials
```

**4. Run Backend**
```bash
cd backend_Lena/backend_Lena

# Build and run
./mvnw clean install
./mvnw spring-boot:run
```

**5. Run Frontend**
```bash
cd frontend_Lena/frontend_lena

# Install dependencies
npm install

# Development server
npm start
```

**6. Access the application**

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080/api
- **Admin Portal**: http://localhost:4200/admin

## Deployment

### AWS Deployment Architecture

The application is deployed on AWS with the following components:

**Frontend Deployment:**
1. Build Angular application: `npm run build`
2. Upload to S3 bucket configured for static website hosting
3. CloudFront CDN distribution for global content delivery
4. Route 53 DNS configuration: https://lenaspabooking.site

**Backend Deployment:**
1. Package Spring Boot application: `./mvnw clean package`
2. Deploy JAR to EC2 instances behind Application Load Balancer
3. Auto Scaling Group for dynamic scaling (2-10 instances)
4. Route 53 DNS configuration: https://api.lenaspabooking.site

**Database:**
- RDS PostgreSQL instance in private subnet
- Multi-AZ deployment for high availability
- Automated backups and point-in-time recovery

See `deployment-guide.md` for detailed deployment instructions.

### Production Environment

**Frontend:** https://lenaspabooking.site
**Backend API:** https://api.lenaspabooking.site
**Admin Portal:** https://lenaspabooking.site/admin

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/user` - Get current user profile
- `PUT /api/auth/user` - Update user profile

### Bookings
- `GET /api/bookings` - List all bookings (Admin)
- `GET /api/bookings/user` - Get user's bookings
- `POST /api/bookings` - Create new booking
- `PUT /api/bookings/{id}` - Update booking
- `DELETE /api/bookings/{id}` - Cancel booking

### Services
- `GET /api/services` - List all spa services
- `POST /api/services` - Create service (Admin)
- `PUT /api/services/{id}` - Update service (Admin)
- `DELETE /api/services/{id}` - Delete service (Admin)

### Admin
- `GET /api/admin/dashboard` - Dashboard statistics
- `POST /api/admin/setup` - Promote user to admin
- `GET /api/admin/users` - List all users
- `GET /api/admin/bookings` - All bookings with filters

### Health Check
- `GET /api/health` - Application health status

## AWS Infrastructure

### Compute & Networking

- **Amazon EC2**: Spring Boot backend hosting with auto-scaling
- **Application Load Balancer**: Traffic distribution and health checks
- **Auto Scaling Groups**: Dynamic scaling (2-10 instances based on CPU)
- **VPC**: Isolated network with public/private subnets
- **Security Groups**: Network-level firewall and access control
- **Internet Gateway**: Secure internet connectivity

### Storage & Database

- **Amazon RDS PostgreSQL**: Managed database with Multi-AZ deployment
- **Amazon S3**: Static website hosting for Angular frontend
- **CloudFront CDN**: Global content delivery with HTTPS
- **Automated Backups**: Daily snapshots with 7-day retention

### Monitoring & Management

- **CloudWatch**: Application and infrastructure monitoring
- **CloudWatch Logs**: Centralized logging for backend services
- **Route 53**: DNS management and domain routing
- **Auto Scaling Policies**: CPU utilization-based scaling triggers
- **CloudWatch Alarms**: Automated alerts for critical metrics

## Development Practices

- **Clean Architecture**: Separation of concerns and modular design
- **RESTful Design**: Industry-standard API design principles
- **Security First**: JWT authentication, RBAC, input validation
- **Responsive Design**: Mobile-first approach with Angular Material
- **Code Quality**: TypeScript strict mode, Java best practices
- **Documentation**: Comprehensive API documentation and guides

## Support

For issues and questions, please open an issue in the repository.

## About

Lena Beauty Spa Management System provides a complete solution for spa businesses to manage appointments, services, and customer relationships through a modern web-based platform.

**Live Site:** [lenaspabooking.site](https://lenaspabooking.site)

## Topics

`spa-management` `booking-system` `java-spring-boot` `angular` `aws-cloud` `jwt-authentication` `postgresql` `rest-api` `full-stack`

---

**Lena Beauty Spa Management System** - Professional, scalable, and secure spa management solution.

© 2026 Lena Spa. All rights reserved.
