# Lena Spa AWS Deployment Checklist

## Pre-Deployment Preparation

### 1. AWS Account Setup
- [ ] Create AWS account
- [ ] Set up billing alerts
- [ ] Install and configure AWS CLI
- [ ] Create IAM user with appropriate permissions
- [ ] Configure AWS CLI with credentials: `aws configure`

### 2. Domain Setup
- [ ] Purchase domain from Namecheap
- [ ] Have domain credentials ready

### 3. Email Service Setup
- [ ] Create Resend account
- [ ] Get API key from Resend dashboard
- [ ] Prepare to verify domain in Resend

### 4. Application Preparation
- [ ] Update backend application.properties for production
- [ ] Create production environment file for Angular
- [ ] Test application locally
- [ ] Generate strong passwords for database and JWT

## Deployment Steps

### Phase 1: Network Infrastructure (30 minutes)
- [ ] Run `01-create-vpc.sh` to create VPC and subnets
- [ ] Run `02-create-security-groups.sh` to create security groups
- [ ] Verify VPC and subnets in AWS Console
- [ ] Save `vpc-config.txt` file securely

### Phase 2: Database Setup (20 minutes)
- [ ] Update database password in `03-create-rds.sh`
- [ ] Run `03-create-rds.sh` to create RDS instance
- [ ] Wait for RDS to be available (10-15 minutes)
- [ ] Note down RDS endpoint
- [ ] Test database connection from local machine (optional)

### Phase 3: Backend Deployment (30 minutes)
- [ ] Update environment variables in `04-deploy-backend.sh`
- [ ] Build Spring Boot application locally to test
- [ ] Run `04-deploy-backend.sh` to deploy backend
- [ ] Wait for Elastic Beanstalk environment (10-15 minutes)
- [ ] Update environment variables in EB console:
  - [ ] DB_PASSWORD
  - [ ] JWT_SECRET
  - [ ] RESEND_API_KEY
  - [ ] RESEND_FROM_EMAIL
  - [ ] ADMIN_NOTIFICATION_EMAIL
- [ ] Test backend API endpoints
- [ ] Check CloudWatch logs for errors

### Phase 4: Frontend Deployment (20 minutes)
- [ ] Create `environment.prod.ts` with backend URL
- [ ] Update bucket name in `05-deploy-frontend.sh`
- [ ] Build Angular application locally to test
- [ ] Run `05-deploy-frontend.sh` to deploy frontend
- [ ] Test S3 website URL
- [ ] Verify CloudFront distribution

### Phase 5: SSL and Domain Setup (30 minutes)
- [ ] Update domain name in `06-setup-ssl.sh`
- [ ] Run `06-setup-ssl.sh` to request SSL certificate
- [ ] Add DNS validation records to Namecheap
- [ ] Wait for certificate validation (5-30 minutes)
- [ ] Add custom domain to CloudFront distribution
- [ ] Update Namecheap DNS to point to CloudFront
- [ ] Test HTTPS access

### Phase 6: Email Service Configuration (15 minutes)
- [ ] Log in to Resend dashboard
- [ ] Go to Domains section
- [ ] Add your domain
- [ ] Add DNS records to Namecheap for domain verification
- [ ] Wait for domain verification
- [ ] Update RESEND_FROM_EMAIL in Elastic Beanstalk
- [ ] Test email functionality (registration, booking)

### Phase 7: Final Configuration (20 minutes)
- [ ] Update CORS settings in backend for production domain
- [ ] Configure Auto Scaling policies
- [ ] Set up CloudWatch alarms:
  - [ ] High CPU usage
  - [ ] High memory usage
  - [ ] RDS storage space
  - [ ] Application errors
- [ ] Configure SNS for alert notifications
- [ ] Enable VPC Flow Logs
- [ ] Set up S3 bucket for application logs

### Phase 8: Testing (30 minutes)
- [ ] Test user registration
- [ ] Test user login
- [ ] Test booking creation
- [ ] Test profile management
- [ ] Test email notifications
- [ ] Test on mobile devices
- [ ] Test all API endpoints
- [ ] Load testing (optional)

### Phase 9: Monitoring Setup (15 minutes)
- [ ] Set up CloudWatch dashboard
- [ ] Configure log retention policies
- [ ] Set up RDS automated backups
- [ ] Create manual RDS snapshot
- [ ] Document backup/restore procedures

### Phase 10: Documentation (15 minutes)
- [ ] Document all AWS resource IDs
- [ ] Document all passwords and secrets (use password manager)
- [ ] Create runbook for common operations
- [ ] Document rollback procedures
- [ ] Create disaster recovery plan

## Post-Deployment

### Immediate (Day 1)
- [ ] Monitor CloudWatch logs for errors
- [ ] Check application performance
- [ ] Verify all features working
- [ ] Test email delivery
- [ ] Monitor costs in AWS Billing

### Week 1
- [ ] Review CloudWatch metrics
- [ ] Optimize Auto Scaling settings
- [ ] Review and adjust RDS instance size if needed
- [ ] Check security group rules
- [ ] Review access logs

### Monthly
- [ ] Review AWS costs
- [ ] Check for AWS service updates
- [ ] Review security patches
- [ ] Test backup restoration
- [ ] Review CloudWatch alarms

## Rollback Plan

If deployment fails:
1. Check CloudWatch logs for errors
2. Verify all environment variables
3. Check security group rules
4. Verify RDS connectivity
5. If needed, delete Elastic Beanstalk environment and redeploy
6. Restore from RDS snapshot if database issues

## Cost Optimization Tips

- Use Reserved Instances for predictable workloads
- Enable S3 Intelligent-Tiering
- Use CloudFront caching effectively
- Right-size EC2 and RDS instances
- Delete unused resources
- Use AWS Cost Explorer

## Security Checklist

- [ ] All passwords are strong and unique
- [ ] Secrets stored in AWS Secrets Manager
- [ ] Security groups follow least privilege
- [ ] RDS encryption enabled
- [ ] S3 buckets not publicly writable
- [ ] CloudFront using HTTPS only
- [ ] Regular security updates applied
- [ ] IAM roles follow least privilege
- [ ] MFA enabled on AWS account
- [ ] CloudTrail enabled for audit logging

## Support Contacts

- AWS Support: https://console.aws.amazon.com/support/
- Namecheap Support: https://www.namecheap.com/support/
- Resend Support: https://resend.com/support

## Useful Commands

```bash
# Check Elastic Beanstalk status
aws elasticbeanstalk describe-environments --application-name lenaspa-backend

# View CloudWatch logs
aws logs tail /aws/elasticbeanstalk/lenaspa-backend-prod/var/log/eb-engine.log --follow

# Invalidate CloudFront cache
aws cloudfront create-invalidation --distribution-id YOUR_DIST_ID --paths "/*"

# Check RDS status
aws rds describe-db-instances --db-instance-identifier lenaspa-db

# Update Elastic Beanstalk environment variables
aws elasticbeanstalk update-environment --environment-name lenaspa-backend-prod --option-settings Namespace=aws:elasticbeanstalk:application:environment,OptionName=KEY,Value=VALUE
```
