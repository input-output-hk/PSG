# PSG Services - New User guide

### As a new user, you need to perform the following steps to start working with PSG Services:

1. Open [PSG Services Login](https://psg.iog.services/) page
2. Enter username, password and select the "Register as new user" button
3. Re-enter password in the confirmation field and click the "Register as new user" button again  
   
   ![Login](./pictures/login.png)  
   

4. You will be navigated to the View Package page by default
5. Go to the Deposit page by clicking a link in the left menu bar
   ![RequestDeposit](./pictures/request-address.png)  
   

6. Select "Request Deposit Address". Wait up to one minute and update the page content by clicking the refresh button on the page.  
   You should get a generated wallet address for funding on the page.  
   ![AddressAdded](./pictures/address-added.png)
   
   
7. Purchase PSG Services [package](#psg-services-package-description).
   
   You can navigate to the View Package page from the menu bar on the left side to check the package status.  
   ![PackagePurchased](./pictures/package-purchased.png)
   

8. Generate a new token at the [API Token](https://psg.iog.services/apitokens) page. Do not forget to save the token value, as it will not be displayed on the page after the page refreshes.  
   You can always replace an existing token with a new one by selecting the "I want to replace existing API Token" option.
   ![GeneratedToken](./pictures/generated-token.png)  
   

9. Set your email and PGP public key at the [PGP Public Key](https://psg.iog.services/pgppublickey) page.
   ![KeySet](./pictures/key-set.png)

10. **Now, you are ready to use PSG Services!**

### PSG Services package description:
Deposit **10 Ada** (or multiples of 10) to purchase credits.

Each **10 ADA** purchases **250 credits** and **30 days** in which to use them.

If you have 0 credits, then after depositing 10 ADA on May 1 you will have 250 credits that will expire on May 31.

If you have 3 credits that expire on May 1, then after depositing 10 ADA you will have 253 credits that will expire on May 31.

**API call prices include all network transaction fees.**

**Price List:**

- SubmitMetadata: 10 credits
- ListTransactions: 1 credit
- AuthenticatedEmail: 15 credits
- HashAndStore: free!


