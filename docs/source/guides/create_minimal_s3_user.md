### Restrict Amazon S3 Bucket Access to a Specific AWS user

#### 1. Create the user.

Follow the step to create a new user from [Creating an IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html)

- Make sure you allow only programmatic access of the user
- Make sure you downloaded the Access key and Secret Access Key


#### 2. Create S3 bucket

- Sign in to the AWS Management Console and open the Amazon S3 console at https://console.aws.amazon.com/s3/.

- Choose Create bucket.

- In Bucket name, enter a DNS-compliant name for your bucket.

- In Region, choose the AWS Region where you want the bucket to reside.

- In the "Block Public Access settings for this bucket" section, uncheck the `Block all public access` checkbox.

- Choose Create bucket.

(For more details on creating s3 bucket check [Creating a bucket](https://docs.aws.amazon.com/AmazonS3/latest/userguide/create-bucket-overview.html)


#### 3. Configure Bucket policy

- Go to the permissions tab of the bucket and select `Edit` on the Bucket policy section.

- Add the following policy

Replace

`123455678910` with your account id

`S3-user` with the user name you created on step 1

`bucket-name` with the name of the bucket you created on step 2
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Deny",
            "NotPrincipal": {
                "AWS": [
                    "arn:aws:iam::123455678910:root",
                    "arn:aws:iam::123455678910:user/S3-user"
                ]
            },
            "Action": [
                "s3:PutObject",
                "s3:DeleteObject"
            ],
            "Resource": [
                "arn:aws:s3:::bucket-name",
                "arn:aws:s3:::bucket-name/*"
            ]
        },
        {
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                    "arn:aws:iam::123455678910:root",
                    "arn:aws:iam::123455678910:user/S3-user"
                ]
            },
            "Action": [
                "s3:PutObject",
                "s3:ListBucket"
            ],
            "Resource": [
                "arn:aws:s3:::bucket-name",
                "arn:aws:s3:::bucket-name/*"
            ]
        }
    ]
}
```

- If you want to give everyone read access to the bucket, add this on the `"Statement"` list of the above policy

```
        {
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::bucket-name/*"
        }
```

You can change the Principal to allow a specific AWS account or service.

- Any other permission other than writing/updating(`"s3:PutObject"`) and deleting (`"s3:DeleteObject"`) actions can be attached to any AWS user or AWS role using identity-based policy.  
  

- Attaching writing/updating/deleting policies will not take effect as it is explicitly denied on the bucket policy