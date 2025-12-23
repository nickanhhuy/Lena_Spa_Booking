# 🌸 Lena Beauty Spa

**Lena Beauty Spa** is a cloud-native web application for easy spa appointment booking and management, built on AWS infrastructure with high availability and scalability.

---

## 🏗️ AWS Architecture

![AWS Architecture Diagram](LenaAWS-_drawio.png)

The application is deployed on AWS with a robust, scalable architecture:

### Network Layer
- **VPC (Virtual Private Cloud):** Isolated network environment for secure resource deployment
- **Internet Gateway:** Enables communication between the VPC and the internet
- **Multi-AZ Deployment:** Resources distributed across multiple Availability Zones for high availability

### Frontend Layer
- **Amazon S3:** Hosts the static Angular frontend website
- **CloudFront (Optional):** CDN for faster global content delivery

### Application Layer
- **Application Load Balancer (ALB):** Distributes incoming traffic across multiple EC2 instances
- **Auto Scaling Group:** Automatically scales EC2 instances (4 instances) based on demand
- **EC2 Instances:** Deployed in public subnets across multiple Availability Zones
- **Spring Boot Backend:** RESTful API services running on EC2 instances

### Database Layer
- **Amazon RDS MySQL:** Managed relational database service
- **Multi-AZ RDS:** Primary and standby database instances in private subnets for high availability
- **Private Subnet Deployment:** Enhanced security with database isolated from public internet

### Monitoring
- **CloudWatch:** Monitors application performance, logs, and triggers auto-scaling events

---

## ✨ Application Features

### User Management
- **Role-based Access Control:** Separate interfaces and permissions for Admins and Users
- **User Registration & Authentication:** Secure account creation and login system
- **Profile Management:** Users can update their personal information

### Booking System
- **Smart Time Slot Checker:** Real-time availability checking to prevent double bookings
- **Appointment Scheduling:** Users can book spa services at available time slots
- **Booking History:** View past and upcoming appointments
- **Booking Cancellation:** Users can cancel their appointments

### Admin Dashboard
- **Service Management:** Create, update, and delete spa services
- **Booking Management:** View, approve, and manage all customer bookings
- **Announcement System:** Send email notifications to all users
- **User Management:** View and manage registered users

### Communication
- **Email Announcements:** Important updates and promotions sent directly to users' inboxes
- **Booking Confirmations:** Automated email notifications for booking confirmations

---

## 🛠️ Tech Stack

### Frontend
- **Angular:** Modern TypeScript-based web framework
- **Angular Material:** UI component library for consistent design
- **RxJS:** Reactive programming for handling asynchronous operations
- **TypeScript:** Type-safe JavaScript development

### Backend
- **Spring Boot:** Java-based framework for building RESTful APIs
- **Spring Security:** Authentication and authorization
- **Spring Data JPA:** Database access and ORM
- **MySQL:** Relational database for data persistence
- **JavaMail:** Email sending functionality

### Cloud Infrastructure (AWS)
- **EC2:** Compute instances for backend hosting
- **RDS MySQL:** Managed database service
- **S3:** Static website hosting for frontend
- **Application Load Balancer:** Traffic distribution
- **Auto Scaling:** Dynamic resource scaling
- **CloudWatch:** Monitoring and logging
- **VPC:** Network isolation and security

---

## 🚀 Key Technical Skills Demonstrated

- **Cloud Architecture Design:** Multi-tier AWS architecture with high availability
- **Microservices Deployment:** Scalable backend services on EC2
- **Database Management:** RDS MySQL with Multi-AZ deployment
- **Load Balancing:** ALB for traffic distribution across instances
- **Auto Scaling:** Dynamic resource management based on demand
- **Security Best Practices:** Private subnets for databases, public subnets for application servers
- **Frontend Development:** Modern Angular SPA with responsive design
- **Backend Development:** RESTful API design with Spring Boot
- **DevOps:** Infrastructure deployment and management on AWS
- **Monitoring & Logging:** CloudWatch integration for application observability

---

## 📊 Architecture Highlights

- **High Availability:** Multi-AZ deployment ensures 99.9% uptime
- **Scalability:** Auto Scaling Group automatically adjusts capacity based on traffic
- **Security:** Database in private subnets, application in public subnets with security groups
- **Performance:** Load balancer distributes traffic efficiently across multiple instances
- **Cost Optimization:** Auto Scaling reduces costs during low-traffic periods
- **Disaster Recovery:** Multi-AZ RDS provides automatic failover capability

---

Lena Beauty Spa makes spa scheduling simple, smart, and scalable.
