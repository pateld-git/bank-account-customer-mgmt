# Sprint 5

The purpose of this epic is to create a frontend application that consumes RESTful APIs from the backend created during the Sprint 4 epic.

## Home Page

The home page displays a welcome message. It also includes the standard header, navigation subheader, and the pop-out side bar standard to all dashboard pages.

![Home Page](<README Images/home_page.png>)

![Home Page: Side Bar Pop Out](<README Images/home_page_side_bar_pop_out.png>)

## Add Page

The add page has functionality to add a new customer or a new account to the database.

![Add Page](<README Images/add_page.png>)

### Add Customer

Selecting Add Cutomer will display form fields for a user input. Hitting submit will send the post request to the backend.

![Add Customer](<README Images/add_customer.png>)

### Add Account

Selecting Add Account will display form fields to add an account. Hitting submit will send the post request to the backend.

![Add Account](<README Images/add_account.png>)

## Update Page

The update page has functionality to add a new customer or a new account to the database.

![Update Page](<README Images/update_page.png>)

### Update Customer

Selecting Update Customer will display fields to update an account. Hitting submit will send the put request to the backend.

![Update Customer](<README Images/update_customer.png>)

### Update Account

Selecting Update Account will display fields to update an account. Hitting submit will send the put request to the backend.

![Update Customer](<README Images/update_account.png>)

## Customers Page

Customers Page queries the backend for all customers.

- Displays message when connecting to backend.
  ![Sync Message](<README Images/customers_syncing.png>)

- Displays error if there is a connection issue.
  ![Error Message](<README Images/customers_error.png>)

- Displays a table of customers if any exist in the database.
  ![Customer Table](<README Images/customers_table.png>)

## Find By ID

Find By ID page queries the backend for a specific customer or account.
![Find By ID Page](<README Images/find_by_id.png>)

- Record found with id:
  ![Found Recird By Its ID](<README Images/find_by_id_found.png>)
