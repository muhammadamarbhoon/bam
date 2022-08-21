# bam

Bank Account Management

## Authnetication
All endpoints are protected by Basic Authentication.

## Actions

- Create Account: Create a new account ( POST /api/v1/accounts )
- Update Account: Update existing account ( PUT /api/v1/accounts/{account-id} )
- Delete Account: Delete existing account ( DELETE /api/v1/accounts/{account-id} )
- Get Account list: Get list of all accounts ( GET /api/v1/accounts )
- Get Account: Get existing account ( GET /api/v1/accounts/{account-id} )

## Privileges

- ADMIN: All actions are allowed ( POST, PUT, DELETE, GET )
- USER: Only Get actions are allowed ( GET )
