# Lena Spa AWS Deployment Guide

## Architecture Overview
This guide will help you deploy the Lena Spa application on AWS using the multi-tier architecture.

## Prerequisites
- AWS Account
- AWS CLI installed and configured
- Domain name from Namecheap
- Resend account for email service

## Deployment Steps

### 1. VPC and Network Setup
- Create VPC with CIDR block (e.g., 10.0.0.0/16)
- Create 2 public subnets in different AZs
- Create 4 private subnets in different AZs (2 for EC2, 2 for RDS)
- Create Internet Gateway and attach to VPC
- Create NAT Gateway in public subnet
- Configure route tables

### 2. Security Groups
- **ALB Security Group**: Allow HTTP (80) and HTTPS (443) from 0.0.0.0/0
- **EC2 Security Group**: Allow traffic from ALB on port 5000
- **RDS Security Group**: Allow PostgreSQL (5432) from EC2 security group

### 3. RDS PostgreSQL Setup
- Create RDS PostgreSQL instance (Multi-AZ for high availability)
- Instance type: db.t3.micro (or larger based on needs)
- Storage: 20GB minimum
- Enable automated backups
- Note down endpoint, username, and password

### 4. Backend Deployment (Elastic Beanstalk)
- Package Spring Boot application as JAR
- Create Elastic Beanstalk application
- Configure environment variables
- Deploy to private subnets
- Configure health checks

### 5. Frontend Deployment (S3 + CloudFront)
- Build Angular application for production
- Create S3 bucket for static hosting
- Enable static website hosting
- Upload built files to S3
- Create CloudFront distribution (optional, for CDN)

### 6. Application Load Balancer
- Create ALB in public subnets
- Configure target groups for EC2 instances
- Set up health checks
- Configure listeners (HTTP/HTTPS)

### 7. Auto Scaling
- Create launch template for EC2 instances
- Configure Auto Scaling group
- Set min/max/desired capacity
- Configure scaling policies

### 8. Domain Configuration
- Point Namecheap domain to ALB DNS name
- Configure SSL certificate using AWS Certificate Manager
- Update ALB listener to use HTTPS

### 9. Monitoring and Logging
- Enable CloudWatch logs for EC2 and RDS
- Set up CloudWatch alarms for CPU, memory, disk
- Configure SNS for alerts

### 10. Email Service Configuration
- Verify domain in Resend
- Update application.properties with production values
- Test email functionality

## Cost Estimation (Monthly)
- EC2 (t3.small x 2): ~$30
- RDS (db.t3.micro): ~$15
- ALB: ~$20
- NAT Gateway: ~$35
- S3: ~$1
- Data Transfer: ~$10
**Total: ~$111/month**

## Security Best Practices
- Use IAM roles for EC2 instances
- Enable encryption for RDS
- Use AWS Secrets Manager for sensitive data
- Enable VPC Flow Logs
- Regular security updates
- Implement WAF rules on ALB

## Backup Strategy
- RDS automated backups (7-day retention)
- Manual RDS snapshots before major changes
- S3 versioning for frontend files
- Database backup to S3 weekly
