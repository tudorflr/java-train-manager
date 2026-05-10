# Java Train Manager

This project is a small console application for train ticket booking. The idea was to keep it simple, but still organize it in a proper OOP way, with separate classes for the main parts of the application.

The application has some predefined trains and routes. A normal user can search trips and book tickets. An admin can manage trains and routes, see bookings, and set train delays.

Emails are sent through SMTP. The SMTP details are not written directly in the code, because passwords should not be committed in a project. They are read from environment variables instead.

## How to run

From the project folder:

```bash
javac -d out $(find trainManagerJ/src -name "*.java")
java -cp out Main
```

## Email setup

Before running the app, set these environment variables in the terminal:

```bash
export SMTP_HOST="smtp.gmail.com"
export SMTP_PORT="587"
export SMTP_USER="your_email@gmail.com"
export SMTP_PASSWORD="your_app_password"
export SMTP_FROM="your_email@gmail.com"
```

Then run:

```bash
java -cp out Main
```

For most email providers, port `587` with STARTTLS is the normal option. That is what the app uses by default.

For Gmail, the password should be an app password, not usually your normal account password. For other providers, use their SMTP host, username, and password.

If an email provider uses SSL on port `465`, also set:

```bash
export SMTP_SSL="true"
export SMTP_STARTTLS="false"
```

If the SMTP variables are missing or wrong, the booking is still saved, but the app prints that the email was not sent.

## Project structure

The project is split into packages:

```text
model
business_layer
data_access
presentation
```

I used this structure so the responsibilities are not all mixed in one file.

`model` contains the objects used by the app:

- `Train`
- `Route`
- `RouteStop`
- `Station`
- `Booking`
- `Customer`
- `TravelOption`

`data_access` contains simple DAO classes. They store data in memory with `ArrayList`. There is no database, because for this assignment the focus is on the Java logic.

`business_layer` contains the main rules:

- booking tickets
- checking seat availability
- searching routes
- admin operations
- real email sending through SMTP

`presentation` contains the console menu and reads input from the user.

This also shows the OOP idea of the project: each class has its own job, and the business logic works with objects like `Train`, `Route`, and `Booking` instead of using loose variables everywhere.

## What the app supports

- predefined trains and routes
- booking one or more tickets
- preventing overbooking
- confirmation email after booking
- searching trips between two stations
- direct trips
- trips with one train change
- message when no route exists
- admin add, remove, and edit trains
- admin add, remove, and edit routes/stations
- admin show bookings for a train
- admin set train delay
- delay email sent to booked customers

## Predefined data

Trains:

```text
Train 1 - IR-100 | seats 5
Train 2 - R-220 | seats 4
Train 3 - IC-330 | seats 6
```

Routes:

```text
Route 1 on train IR-100: Cluj -> Alba Iulia -> Sibiu -> Brasov
Route 2 on train R-220: Cluj -> Oradea -> Arad
Route 3 on train IC-330: Sibiu -> Pitesti -> Bucuresti
```

The route output in the app also shows arrival and departure times for each station.

## Main menu

When the app starts:

```text
===== Train ticketing =====
1. User menu
2. Admin menu
0. Exit
Choose:
```

## User examples

Show trains:

```text
Input:
1
1

Output:
Train 1 - IR-100 | seats 5
Train 2 - R-220 | seats 4
Train 3 - IC-330 | seats 6
```

Find a direct trip:

```text
Input:
1
3
Cluj
Brasov

Output:
Direct trip: train IR-100 leaves Cluj at 08:00 and gets to Brasov at 14:00
```

Find a trip with a change:

```text
Input:
1
3
Cluj
Bucuresti

Output:
With a change: train IR-100 leaves Cluj at 08:00 and reaches Sibiu at 11:00, then train IC-330 leaves Sibiu at 12:00 and gets to Bucuresti at 16:30
```

No route found:

```text
Input:
1
3
Arad
Brasov

Output:
I couldn't find a trip between those stations
```

Book tickets:

```text
Input:
1
4
1
Ana Pop
ana@example.com
Cluj
Brasov
2

Output:
Email sent to ana@example.com
Booked, you're all set
```

If SMTP is not configured, the booking is still stored, but the output is:

```text
Email not sent, SMTP settings are missing
Set SMTP_HOST, SMTP_USER, SMTP_PASSWORD and optionally SMTP_PORT / SMTP_FROM
Booked, but the confirmation email did not go out
```

Overbooking example:

If all 5 seats on `IR-100` are already booked for the same part of the route, another booking is rejected.

```text
Output:
Not enough seats for that part of the trip, only 0 left
```

## Admin examples

Add train:

```text
Input:
2
1
4
IR-400
8

Output:
Train added
```

Edit train:

```text
Input:
2
3
4
IR-401
10

Output:
Train updated
```

Remove train:

```text
Input:
2
2
4

Output:
Train removed
```

Add route:

```text
Input:
2
4
4
1
3
Cluj
-
18:00
Dej
18:45
18:50
Bistrita
20:00
-

Output:
Route added
```

Add stop to route:

```text
Input:
2
6
1
Medias
12:10
12:15

Output:
Stop added
```

Edit route stop:

```text
Input:
2
8
1
Medias
Medias Nord
12:12
12:18

Output:
Route stop updated
```

Remove route stop:

```text
Input:
2
7
1
Medias Nord

Output:
Stop removed
```

Change the train used by a route:

```text
Input:
2
9
1
2

Output:
Route train updated
```

Remove route:

```text
Input:
2
5
4

Output:
Route removed
```

Show bookings for a train:

```text
Input:
2
10
1

Output:
Booking 1 | Ana Pop <ana@example.com> | train IR-100 | Cluj to Brasov | tickets 2
```

Set train delay:

```text
Input:
2
11
1
25

Output:
Email sent to ana@example.com
Delay saved and the booked customers were emailed
```

If one or more emails fail, the app prints how many were actually sent.

If there are no customers booked on that train:

```text
Delay saved, no booked customers to email yet
```

## Small notes

The data is kept only while the program is running. If the app is closed, the bookings and new admin changes are lost.

Station names are compared case-insensitively, so `cluj` and `Cluj` work the same.

The route search supports direct routes and one changeover. This matches the assignment requirement where a changeover between trains may be needed.
