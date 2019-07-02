# SplitWise:

Client-server application with functionality similar to [Splitwise](https://www.splitwise.com/).

Splitwise цели улесняване на поделянето на сметки между приятели и съквартиранти и намаляване на споровете от тип "само аз купувам бира в това общежитие".

## Requirement


Build client console application which takes user's commands, sends them to the server, accepts server's response and show to the user in readable format.

### Functional requirements:

- Registration of user with username and password; Registered users are persist in file in the Server, it serves as a database. When stopped and re-running, the server can load already registered users into the memory.

- Login;
- A registrated users can:
    - add already registrated users to 'Friend List' based on 'username'. For example:
        ```bash
        $ add-friend <username>
        ```
    - сreate group, consisting of several already registered users:

        ```bash
        $ create-group <group_name> <username> <username> ... <username>
        ```
        groups are created by one user, each group includes three or more users. You can imagine that "friendly" relationships are a group of two people. 

    - adds an amount paid by him to the debt of:

        - another user from his 'Friend List':
        ```bash
        $ split <amount> <username> <reason_for_payment>
        ```
        - group in which he is included:

        ```bash
        $ split-group <amount> <group_name> <reason_for_payment>
        ```

    - he gets his status - the sums he owes to his friends and in his groups and the sums owed to him. For example:

        ```bash
        $ get-status
        Friends:
        * Pavel Petrov (pavel97): Owes you 10 LV

        Groups
        * 8thDecember
        - Pavel Petrov (pavel97): Owes you 25 LV
        - Hristo Hristov (ico_h): Owes you 25 LV
        - Harry Gerogiev (harryharry): You owe 5 LV
        ```


- A newly entered amount is shared equally between all participants in the group or in half if shared with a 'Friend List' user.


- Когато един потребител А дължи пари на друг потребител B, задължението може да бъде "погасено" (с подходяща команда) само от потребител B.
- When a user A owes money to another user B, the debt may be "extinguished" (with an appropriate command) only by user B.

    ```bash
    $ payed <amount> <username>
    ```
    For example:
    ```bash
    $ get-status
    Friends:
    * Pavel Petrov (pavel97): Owes you 10 LV
    * Hristo Hristov (ico_h): You owe 5 LV

    $ payed 5 pavel97
    Pavel Petrov (pavel97) payed you 5 LV.
    Current status: Owes you 5 LV

    $ get-status
    Friends:
    * Pavel Petrov (pavel97): Owes you 5 LV
    * Hristo Hristov (ico_h): You owe 5 LV
    ```

- When a user A owes an amount to user B (for example $ 5), but before returning it to B adds another amount he has paid (for example, $ 5) then the amounts due to both are recalculated (the amount due to A will $ 2.50, B still owes nothing, but has $ 2.50 to get).
    ```bash
    $ get-status
    Friends:
    * Pavel Petrov (pavel97): Owes you 10 LV
    * Hristo Hristov (ico_h): You owe 5 LV

    $ split 5 ico_h limes and oranges
    Splitted 5 LV between you and Hristo Hristov.
    Current status: You owe 2.50 LV

    $ get-status
    Friends:
    * Pavel Petrov (pavel97): Owes you 5 LV
    * Hristo Hristov (ico_h): You owe 2.50 LV
    ```

- Every time a user enters the system, he receives notifications if his friends have added amounts or "repaid" debts. For example:
    ```bash
    $ login alex alexslongpassword
    Successful login!
    No notifications to show.
    ```
    or
    ```bash
    $ login alex alexslongpassword
    Successful login!
    *** Notifications ***
    Friends:
    Misho approved your payment 10 LV [Mixtape beers].

    Groups:
    * Roomates:
    You owe Gery 20 LV [Tanya Bday Present].

    * Family:
    You owe Alex 150 LV [Surprise trip for mom and dad]
    ```
- The user can see a history of payments made by him / her. This history is kept in a file on the server.

*******
Command switch-currency do not work correctly(has bug)
*******

- The server provides currency conversion capabilities. The default currency is Bulgarian leva, and the user can change it at any time during the program execution by a suitable command (for example, switch-currency EUR). All amounts that the user owes and owes to him at that point in time pass to the selected currency.

	Through an HTTP request to a public API ( https://exchangeratesapi.io/), take the current currency values and process the response-a.

### Non-functional requirement

- The server can serve multiple users





